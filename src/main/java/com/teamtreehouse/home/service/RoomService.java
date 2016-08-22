package com.teamtreehouse.home.service;

import com.teamtreehouse.home.model.Room;

import java.util.List;

public interface RoomService {
    List<Room> findAll();
    Room findOne(Long id);
}
