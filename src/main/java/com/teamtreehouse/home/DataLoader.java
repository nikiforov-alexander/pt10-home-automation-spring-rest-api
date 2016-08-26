package com.teamtreehouse.home;

import com.teamtreehouse.home.dao.ControlDao;
import com.teamtreehouse.home.dao.DeviceDao;
import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.dao.UserDao;
import com.teamtreehouse.home.model.Control;
import com.teamtreehouse.home.model.Device;
import com.teamtreehouse.home.model.Room;
import com.teamtreehouse.home.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ComponentScan
public class DataLoader implements ApplicationRunner {
    private final RoomDao roomDao;
    private final DeviceDao deviceDao;
    private final ControlDao controlDao;
    private final UserDao userDao;

    @Autowired
    public DataLoader(RoomDao roomDao,
                      DeviceDao deviceDao,
                      ControlDao controlDao,
                      UserDao userDao) {
        this.roomDao = roomDao;
        this.deviceDao = deviceDao;
        this.controlDao = controlDao;
        this.userDao = userDao;
    }

    /**
     * Generates list of users with administration
     * roles, simply by looping through all users
     * returned from userDao.findAll() method
     * @return List<User> with administrators roles:
     * "ROLE_ADMIN"
     */
    List<User> generateListOfAdministrators() {
        List<User> administrators = new ArrayList<>();
        userDao.findAll().forEach(
            user -> {
                for (String role: user.getRoles()) {
                    if (role.equals("ROLE_ADMIN")) {
                        administrators.add(user);
                    }
                }
            }
        );
        return administrators;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // create two users: admin and johnDoe
        User johnDoe = new User(
                "John Doe",
                "jd",
                "123",
                new String[]{"ROLE_USER"});
        User admin = new User(
                "System Administrator",
                "sa",
                "sa",
                new String[]{"ROLE_USER", "ROLE_ADMIN"});
        // and save them
        userDao.save(admin);
        userDao.save(johnDoe);

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
            room.setName("room " + i);
            room.setArea(i);
            // set administrators and add them to room
            room.setAdministrators(
                    generateListOfAdministrators()
            );

            // add device to it
            room.addDevice(device);

            // save room
            roomDao.save(room);
        }

    }
}
