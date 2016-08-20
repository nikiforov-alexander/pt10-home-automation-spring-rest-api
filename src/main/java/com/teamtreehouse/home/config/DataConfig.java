package com.teamtreehouse.home.config;

import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataConfig implements ApplicationRunner {
    private final RoomDao roomDao;

    @Autowired
    public DataConfig(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Room room = new Room();
        room.setName("name");
        room.setSquareFootage(123);
        roomDao.save(room);
    }
}
