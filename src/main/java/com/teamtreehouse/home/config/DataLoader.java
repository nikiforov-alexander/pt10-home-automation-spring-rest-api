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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
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
        // create n: room/device/controls
        for (int i = 1; i <= 2; i++) {
            // create new Control
            Control control = new Control("control " + i, i);

            // create new Device
            Device device = new Device("device " + i);
            // add control to it
            device.addControl(control);

            // create new room
            Room room = new Room();
            room.setName("room " + 1);
            room.setArea(i);

            // add device to it
            room.addDevice(device);

            // save room
            roomDao.save(room);
        }
    }
}
