package com.teamtreehouse.home.config;

import com.teamtreehouse.home.dao.ControlDao;
import com.teamtreehouse.home.dao.DeviceDao;
import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.model.Control;
import com.teamtreehouse.home.model.Device;
import com.teamtreehouse.home.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final RoomDao roomDao;
    private final DeviceDao deviceDao;
    private final ControlDao controlDao;

    @Autowired
    public DataLoader(RoomDao roomDao,
                      DeviceDao deviceDao,
                      ControlDao controlDao) {
        this.roomDao = roomDao;
        this.deviceDao = deviceDao;
        this.controlDao = controlDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // create new Control
        Control control = new Control("control 1", 1);
        // and save it to set Id
        controlDao.save(control);

        // create new Device
        Device device = new Device("device 1");
        // add 1-st Control from db
        device.addControl(controlDao.findOne(1L));
        // save device to db
        deviceDao.save(device);

        // create new room
        Room room = new Room();
        room.setName("room 1");
        room.setArea(1);
        // add device to it: 1-st one from db
        room.addDevice(deviceDao.findOne(1L));
        // save room
        roomDao.save(room);
    }
}
