package com.teamtreehouse.home.config;

import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements ApplicationRunner {
    private final RoomDao roomDao;

    @Autowired
    public DataLoader(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // add two rooms when app starts, database for now is drop create
        Room room1 = new Room();
        room1.setName("room 1");
        room1.setSquareFootage(1);
        roomDao.save(room1);

        Room room2 = new Room();
        room2.setName("room 2");
        room2.setSquareFootage(2);
        roomDao.save(room2);
    }
}
