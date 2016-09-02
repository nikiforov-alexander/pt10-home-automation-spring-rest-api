package com.teamtreehouse.home;

import com.teamtreehouse.home.dao.ControlDao;
import com.teamtreehouse.home.dao.DeviceDao;
import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.model.Control;
import com.teamtreehouse.home.model.Device;
import com.teamtreehouse.home.model.Room;
import com.teamtreehouse.home.service.CustomUserDetailsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

// run with Spring junit runner class, so
@RunWith(SpringJUnit4ClassRunner.class)
// adding spring configuration from application class
@SpringApplicationConfiguration(classes = {
    Application.class
})
// About the rest annotations I'm not sure anymore:
// I tried to put WebIntegrationTest, because
// I believe that What I do
@WebIntegrationTest
// now file in src/test/resources: test.properties
// will separate our database from real app db
@TestPropertySource("classpath:./test.properties")
public class ApplicationIntegrationTest {
    // message converter used in toJson() method here, to convert
    // POJOs like Room, and etc. to JSON
    // taken from Spring REST Project tutorial
    private HttpMessageConverter
        mappingJackson2HttpMessageConverter;

    // setting POJO -> JSON converters
    // taken from Spring REST Project tutorial
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter =
                Arrays.stream(converters)
                        .filter(
                            hmc -> hmc instanceof MappingJackson2HttpMessageConverter
                        )
                        .findAny()
                        .get();
        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    // setting content type to "application/json"
    // taken from Spring REST Project tutorial
    private MediaType contentType =
            new MediaType(
                    MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype()
            );

    // our MockMvc object that will be filled with Application context
    private MockMvc mockMvc;

    // server, base path, and base url setting
    private final int SERVER_PORT = 8081;
    private final String SPRING_DATA_REST_BASEPATH = "/api/v1";
    private final String BASE_URL =
            "http://localhost:"
            + SERVER_PORT
            + SPRING_DATA_REST_BASEPATH;

    // web application context to be injected into MockMvc
    @Autowired
    private WebApplicationContext webApplicationContext;

    // user details service is used to get users and pass them
    // with certain requests in tests, using
    // SecurityMockMvcRequestPostProcessors
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // autowire control dao to check later that newly created
    // control in controlDao should have logged on user in
    // its lastModifiedBy field.
    @Autowired
    private ControlDao controlDao;

    // autowire device dao
    @Autowired
    private DeviceDao deviceDao;

    // autowire room dao
    @Autowired
    private RoomDao roomDao;

    // set up : inject webAppContext into our mockMvc and build mockMvc
    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(
                webApplicationContext)
        .build();
    }

    // helpful method converting POJO to JSON,
    // taken from official Spring REST project
    private String toJson(Object o) throws IOException {
        MockHttpOutputMessage message =
                new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, message
        );
        return message.getBodyAsString();
    }

    //
    // Rooms
    //

    // My first integration tests: it can be disregarded it basically
    // checks that .../rooms page is OK with links and HATEOAS
    // compliant
    @Test
    public void getRequestToRoomsPageReturnsTwoRooms()
            throws Exception {
        // When request to BASE_URL/rooms is made
        // Then:
        // - status should be OK
        // - we should have _links.self and _links.href JSON parts
        // - we should have link to profile: _links.profile.href
        // - paging should be enabled:
        //   size: 20 per page,
        mockMvc.perform(get(BASE_URL + "/rooms"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                    jsonPath("$._links.self.href",
                            endsWith("/rooms"))
                )
                .andExpect(
                    jsonPath("$._links.profile.href",
                            endsWith("/profile/rooms"))
                )
                .andExpect(
                        jsonPath("$.page.size", equalTo(20))
                )
                .andExpect(
                        jsonPath("$.page.totalPages", equalTo(1))
                );
    }

    @Test
    public void roomDetailPageShouldHaveEtagHeader()
            throws Exception {
        // Arrange: mockMvc is arranged to perform requests
        // There are two rooms loaded
        //  { name : "room 1", area : 1 }
        //  { name : "room 2", area : 2 }

        // Act and Assert:
        // When GET request to room details page is made
        // Then:
        // - status should be OK
        // - response should have header "Etag" with some
        //   value
        mockMvc.perform(
                get(BASE_URL + "/rooms/1")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        header().string("Etag", anything())
                );
    }

    @Test
    public void roomsCanBeSearchedByName() throws Exception {
        // Arrange: mockMvc is arranged: all requests are allowed
        // There are two rooms
        //  { name : "room 1", area : 1 }
        //  { name : "room 2", area : 2 }

        // Act and Assert:
        // When GET request is made to:
        // BASE_URL/devices/search/has-name?name=room+1
        // or BASE_URL/devices/search/has-name?name=room%201
        // Then:
        // - status should be OK
        // - json should have "_embedded.rooms" array with size 1
        //   which means one result test room w name "room 1"
        //   and with area "1"
        mockMvc.perform(
                // here i use space between "room 1" because encoding is done
                // automatically
                get(BASE_URL + "/rooms/search/" +
                        "has-name?name=room 1" )
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$._embedded.rooms", hasSize(1))
                );
    }
    @Test
    public void roomsCanBeSearchedByAreaLessThan() throws Exception {
        // Arrange: mockMvc is arranged: all requests are allowed
        // There are two rooms
        //  { name : "room 1", area : 1 }
        //  { name : "room 2", area : 2 }

        // Act and Assert:
        // When GET request is made to:
        // BASE_URL/devices/search/has-area-less-than?&area=2
        // or BASE_URL/devices/search/has-area-less-than?&area=2
        // Then:
        // - status should be OK
        // - json should have "_embedded.rooms" array with size 1
        //   which means one result test room w name "room 1"
        //   and with area "1"
        mockMvc.perform(
                get(BASE_URL + "/rooms/search/" +
                        "has-area-less-than?area=2" )
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$._embedded.rooms", hasSize(1))
                );
    }

    //
    // Room post methods
    //
    @Test
    public void postMethodCreatingNewRoomShouldWorkWithAdminUser()
            throws Exception {
        // Arrange
        // create JSON from new Room object
        String roomJson = toJson(new Room("room3", 123));
        // create UsernamePasswordAuthenticationToken with
        // admin user "sa":
        UserDetails admin = customUserDetailsService.loadUserByUsername("sa");

        // Act and Assert:
        // When POST request to BASE_URL/rooms is made with:
        // 1. authenticated admin user
        // 2. JSON created from new room
        // Then:
        // - status should be 201 Created
        mockMvc.perform(
                    post(BASE_URL + "/rooms")
                            .with(
                                SecurityMockMvcRequestPostProcessors.user(
                                    admin
                            )
                    )
                   .contentType(contentType)
                   .content(roomJson)
              )
              .andDo(print())
              .andExpect(status().isCreated());
    }

    // In this test I unfortunately don't know how to figure out
    // what the problem is: Spring throws NestedServletException
    // with AccessDeniedException inside. I guess that is because
    // we don't have usual controllers and service layer is behind
    // the scenes.
    @Test(expected = NestedServletException.class)
    public void
    postMethodCreatingNewRoomShouldReturnAccessDeniedWithNormalUser()
            throws Exception {
        // Arrange
        // create JSON from new Room object
        String roomJson = toJson(new Room("room3", 123));
        // create UsernamePasswordAuthenticationToken with
        // simple user "jd":
        UserDetails admin = customUserDetailsService.loadUserByUsername("jd");


        // Act and Assert:
        // When POST request to BASE_URL/rooms is made with:
        // 1. authenticated simple user "jd"
        // 2. JSON created from new room
        // Then:
        // - status should be Denied ...
        mockMvc.perform(
                post(BASE_URL + "/rooms")
                        .with(
                                SecurityMockMvcRequestPostProcessors.user(
                                        admin
                                )
                        )
                        .contentType(contentType)
                        .content(roomJson)
                )
                .andDo(print());
    }

    @Test
    public void postMethodCreatingNewRoomWithBigAreaShouldReturnFriendlyError()
            throws Exception {
        // Arrange
        // create JSON from new Room object with area of 1000 sq.m.
        String roomJson = toJson(new Room("room3", 1234));
        // create UsernamePasswordAuthenticationToken with
        // admin user "sa":
        UserDetails admin = customUserDetailsService.loadUserByUsername("sa");

        // Act and Assert:
        // When POST request to BASE_URL/rooms is made with:
        // 1. authenticated admin user
        // 2. JSON created from new room
        // Then:
        // - status should be 400: Bad Request
        // - json should have "error" array with size 1
        // - error[0].entity should be Room
        // - error[0].message should contain "less than 1000"
        // - error[0].invalidValue should be "1234"
        // - error[0].property should be "area"
        mockMvc.perform(
                post(BASE_URL + "/rooms")
                        .with(
                                SecurityMockMvcRequestPostProcessors.user(
                                        admin
                                )
                        )
                        .contentType(contentType)
                        .content(roomJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.errors", hasSize(1))
                )
                .andExpect(
                        jsonPath("$.errors[0].entity", equalTo("Room"))
                )
                .andExpect(
                        jsonPath("$.errors[0].message",
                                containsString("less than 1000")
                        )
                )
                .andExpect(
                        jsonPath("$.errors[0].invalidValue",
                                equalTo("1234")
                        )
                )
                .andExpect(
                        jsonPath("$.errors[0].property",
                                equalTo("area")
                        )
                );
    }

    //
    // Devices tests
    //

    @Test
    public void devicesCanBeSearchedByNameContaining() throws Exception {
        // Arrange: mockMvc is arranged: all requests are allowed
        // There are two devices loaded by DataLoader class
        // { name : "device 1" }
        // { name : "device 2" }
        // Act and Assert:
        // When GET request is made to:
        // BASE_URL/devices/search/contains-name?name=1
        // Then:
        // - status should be OK
        // - json should have "_embedded.devices" array with size 1
        mockMvc.perform(
                get(BASE_URL + "/devices/search/contains-name?name=1" )
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$._embedded.devices", hasSize(1))
        );
    }



    @Test
    public void creatingDeviceWithoutRoomReturnsValidationMessage()
            throws Exception {
        // Arrange
        // create JSON from new Device object without Room
        String roomJson = toJson(new Device("device"));
        // create UsernamePasswordAuthenticationToken with
        // admin user "sa":
        UserDetails admin = customUserDetailsService.loadUserByUsername("sa");

        // Act and Assert:
        // When POST request to BASE_URL/devices is made with:
        // 1. authenticated admin user
        // 2. JSON created from new device without room
        // Then:
        // - status should be 400: Bad Request
        // - json should have "error" array with size 1
        // - error[0].entity should be Device
        // - error[0].message should contain "without Room"
        // - error[0].invalidValue should be "null"
        // - error[0].property should be "room"
        mockMvc.perform(
                post(BASE_URL + "/devices")
                        .with(
                                SecurityMockMvcRequestPostProcessors.user(
                                        admin
                                )
                        )
                        .contentType(contentType)
                        .content(roomJson)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.errors", hasSize(1))
                )
                .andExpect(
                        jsonPath("$.errors[0].entity", equalTo("Device"))
                )
                .andExpect(
                        jsonPath("$.errors[0].message",
                                containsString("without Room")
                        )
                )
                .andExpect(
                        jsonPath("$.errors[0].invalidValue",
                                equalTo("null")
                        )
                )
                .andExpect(
                        jsonPath("$.errors[0].property",
                                equalTo("room")
                        )
                );
    }


    @Test(expected = NestedServletException.class)
    public void creatingDeviceWithNonAdminAndNonRoomAdminUserShouldThrowAccessDeniedException()
            throws Exception {
        // Arrange
        // create JSON from new Device object manually
        String jsonFromDeviceWithRoom =
                "{\"name\":\"device\"," +
                        "\"room\":" +
                        "\"" +
                        BASE_URL + "/rooms/1" +
                        "\"" +
                        "}";
        // create UsernamePasswordAuthenticationToken with
        // admin user "jd": "ROLE_USER" and
        // he is not in room.administrators
        UserDetails admin =
                customUserDetailsService.loadUserByUsername("jd");

        // Act and Assert:
        // When POST request to BASE_URL/devices is made with:
        // 1. authenticated admin user
        // 2. JSON created from new device with room
        // Then:
        // AccessDeniedException Should be thrown
        // but actually NestedServletException will be
        mockMvc.perform(
                post(BASE_URL + "/devices")
                        .with(
                                SecurityMockMvcRequestPostProcessors.user(
                                        admin
                                )
                        )
                        .contentType(contentType)
                        .content(jsonFromDeviceWithRoom)
        )
        .andDo(print());
    }

    // Control tests

    @Test
    public void creatingControlWithoutDeviceReturnsValidationMessage()
            throws Exception {
        // Arrange
        // create JSON from new Control object without Device
        String roomJson = toJson(new Control("control", 1));
        // create UsernamePasswordAuthenticationToken with
        // admin user "sa":
        UserDetails admin = customUserDetailsService.loadUserByUsername("sa");

        // Act and Assert:
        // When POST request to BASE_URL/devices is made with:
        // 1. authenticated admin user
        // 2. JSON created from new control without device
        // Then:
        // - status should be 400: Bad Request
        // - json should have "error" array with size 1
        // - error[0].entity should be Control
        // - error[0].message should contain "without Device"
        // - error[0].invalidValue should be "null"
        // - error[0].property should be "device"
        mockMvc.perform(
                post(BASE_URL + "/controls")
                        .with(
                                SecurityMockMvcRequestPostProcessors.user(
                                        admin
                                )
                        )
                        .contentType(contentType)
                        .content(roomJson)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.errors", hasSize(1))
                )
                .andExpect(
                        jsonPath("$.errors[0].entity", equalTo("Control"))
                )
                .andExpect(
                        jsonPath("$.errors[0].message",
                                containsString("without Device")
                        )
                )
                .andExpect(
                        jsonPath("$.errors[0].invalidValue",
                                equalTo("null")
                        )
                )
                .andExpect(
                        jsonPath("$.errors[0].property",
                                equalTo("device")
                        )
                );
    }

    @Test
    public void afterCreationLoggedOnUserIsSetToLastModifiedByFieldInControl()
            throws Exception {
        // Arrange: mockMvc lets GET requests to be made
        // There are two controls loaded by DatabaseLoader:
        //  { name : "control 1", value : 1 }
        //  { name : "control 2", value : 2 }

        // create JSON from new Control object
        String jsonFromControlWithDevice =
                "{\"name\":\"control\"," +
                        "\"value\":\"1\"," +
                        "\"device\":" +
                        "\"" +
                        BASE_URL + "/devices/1" +
                        "\"" +
                        "}";

        // create UsernamePasswordAuthenticationToken with
        // admin user "sa":
        UserDetails admin = customUserDetailsService.loadUserByUsername("sa");

        // Act and Assert:
        // When POST request is made to create new control
        // Then status should be 201 created
        mockMvc.perform(
                post(BASE_URL + "/controls")
                        .with(
                                SecurityMockMvcRequestPostProcessors.user(
                                        admin
                                )
                        )
                        .contentType(contentType)
                        .content(jsonFromControlWithDevice)
        ).andDo(print())
                .andExpect(status().isCreated());
        // Then lastModifiedBy User of newly created Control should be
        // admin user
        assertThat(
                admin,
                equalTo(controlDao.findOne(3L).getLastModifiedBy())
        );
    }
}