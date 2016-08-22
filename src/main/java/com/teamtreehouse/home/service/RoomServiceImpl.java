package com.teamtreehouse.home.service;

import com.google.common.collect.Lists;
import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    private RoomDao roomDao;

    @Override
    public List<Room> findAll() {
        // converts Iterable from CRUD to List<Room> using Guava google
        return Lists.newArrayList(roomDao.findAll());
    }

    @Override
    public Room findOne(Long id) {
        return roomDao.findOne(id);
    }
}
