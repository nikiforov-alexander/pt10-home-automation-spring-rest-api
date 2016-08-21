package com.teamtreehouse.home.service;

import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    private RoomDao roomDao;

    @Override
    public Iterable<Room> findAll() {
        return roomDao.findAll();
    }
}
