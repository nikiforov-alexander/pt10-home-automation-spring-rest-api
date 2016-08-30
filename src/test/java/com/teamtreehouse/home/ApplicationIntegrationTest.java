package com.teamtreehouse.home;

import com.teamtreehouse.home.config.RestConfig;
import com.teamtreehouse.home.config.WebSecurityConfiguration;
import com.teamtreehouse.home.dao.ControlDao;
import com.teamtreehouse.home.dao.DeviceDao;
import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.model.Room;
import com.teamtreehouse.home.service.CustomUserDetailsService;
import org.junit.After;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private final int SERVER_PORT = 8080;
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
    PostMethodCreatingNewRoomShouldReturnAccessDeniedWithNormalUser()
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

}