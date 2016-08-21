package com.teamtreehouse.home.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class RoomControllerTest {
    private MockMvc mockMvc;
    private RoomController roomController;

    @Before
    public void setUp() throws Exception {
        roomController = new RoomController();
        mockMvc = MockMvcBuilders.standaloneSetup(
                roomController
        ).build();
    }

    @After
    public void tearDown() throws Exception {
    }

}