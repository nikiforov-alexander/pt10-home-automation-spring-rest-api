package com.teamtreehouse.home.controller;

import com.teamtreehouse.home.model.Room;
import com.teamtreehouse.home.service.RoomService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class RoomControllerTest {

    private final static String API_CONTEXT = "/api/v1";
    private final String BASE_URL = "http://localhost:8080";

    private MockMvc mockMvc;

    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomService roomService;

    private MediaType contentType =
            new MediaType(
                    MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype()
            );

    // test list with two rooms, used by findAll
    private List<Room> testListWithTwoRooms = new ArrayList<>();

    // test room
    private Room testRoom = new Room("room 1", 1);

    // used in setup and to mock returning rooms from findAll
    private void addTwoTestRoomsToTestList() {
        Room testRoom2 = new Room("room 2", 2);
        testRoom2.setId(2L);
        testListWithTwoRooms.add(testRoom);
        testListWithTwoRooms.add(testRoom2);
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
        addTwoTestRoomsToTestList();
        testRoom.setId(1L);
        System.out.println(API_CONTEXT);
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllRoomsPageShouldReturnsAllRooms() throws Exception {
        // Arrange:
        // when room service will be called, then test list with two rooms
        // will be returned, see addTwoTestRoomsToTestList for more on test
        // list
        when(roomService.findAll()).thenReturn(testListWithTwoRooms);

        // Arrange and Assert
        // When GET request is made to /api/v1/rooms
        // Then:
        // - status should be OK
        // - JSON with two rooms should be returned
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(API_CONTEXT + "/rooms")
                ).andDo(print());
    }

    @Test
    public void getRoomByIdPageShouldReturnSingleRoomPage() throws Exception {
        // Arrange:
        // when room service findOne method will be called,
        // then test room
        // will be returned
        when(roomService.findOne(1L)).thenReturn(testRoom);

        // Arrange and Assert
        // When GET request is made to /api/v1/rooms
        // Then:
        // - status should be OK
        // - JSON with room should be returned
        // - all member variables should be same as in test room:
        //  name and square footage
        // - links with "rel" should be equal to self
        // - link with href should contain "/rooms/1"
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(API_CONTEXT + "/rooms/1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(
                        jsonPath("$.name", is(testRoom.getName()))
                )
                .andExpect(
                        jsonPath("$.squareFootage",
                                is(testRoom.getSquareFootage())
                        )
                )
                .andExpect(
                        jsonPath("$.links[0].rel",
                                is("self"))
                )
                .andExpect(
                jsonPath("$.links[0].href",
                        containsString(API_CONTEXT + "/rooms/1")
                )
        );
    }
}