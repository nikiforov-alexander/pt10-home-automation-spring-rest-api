package com.teamtreehouse.home.controller;

import com.teamtreehouse.home.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;
}
