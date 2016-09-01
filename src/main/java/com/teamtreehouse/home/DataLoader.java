package com.teamtreehouse.home;

import com.teamtreehouse.home.dao.ControlDao;
import com.teamtreehouse.home.dao.DeviceDao;
import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.dao.UserDao;
import com.teamtreehouse.home.model.Control;
import com.teamtreehouse.home.model.Device;
import com.teamtreehouse.home.model.Room;
import com.teamtreehouse.home.model.User;
import com.teamtreehouse.home.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class DataLoader implements ApplicationRunner {
    private final RoomDao roomDao;
    private final DeviceDao deviceDao;
    private final ControlDao controlDao;
    private final UserDao userDao;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public DataLoader(RoomDao roomDao,
                      DeviceDao deviceDao,
                      ControlDao controlDao,
                      UserDao userDao,
                      CustomUserDetailsService customUserDetailsService
                      ) {
        this.roomDao = roomDao;
        this.deviceDao = deviceDao;
        this.controlDao = controlDao;
        this.userDao = userDao;
        this.customUserDetailsService = customUserDetailsService;
    }

    private void createCoupleOfTestRoomsWithDevicesAndControls(
            User user
    ) {
        // create n: room/device/controls
        for (int i = 1; i <= 2; i++) {
            // create new Control
            Control control = new Control("control " + i, i);
            // set admin as last modified user
            control.setLastModifiedBy(user);

            // create new Device
            Device device = new Device("device " + i);
            // add control to it
            device.addControl(control);

            // create new room
            Room room = new Room();
            room.setName("room " + i);
            room.setArea(i);
            // add user to room administrators
            room.addUserToRoomAdministrators(user);

            // add device to it
            room.addDevice(device);

            // save room
            roomDao.save(room);
        }
    }

    // load user by username as Admin, in order to successfully
    // create new room: see RoomDao.save @PreAuthorize
    // If we put here non-admin user we won't be able to
    // create Room because Access will be denied, because room
    // that is not created can't have `administrators` before:
    private void authenticatedUserWithUserName(String username) {
        // get "admin" UserDetails object:
        // casted from "our" com.teamtreehouse...User
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        "sa"
                );
        // create new authentication token: our authentication object
        // that will be null however if we want to use it using
        // @PreAuthorize in RoomDao
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
        // set authentication object
        SecurityContextHolder.getContext().setAuthentication(
                authenticationToken
        );
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
        User otherAdmin = new User(
                "Other Administrator",
                "oa",
                "oa",
                new String[]{"ROLE_USER", "ROLE_ADMIN"});
        // and save them
        userDao.save(admin);
        userDao.save(johnDoe);
        userDao.save(otherAdmin);

        authenticatedUserWithUserName("sa");

        createCoupleOfTestRoomsWithDevicesAndControls(admin);

    }
}
