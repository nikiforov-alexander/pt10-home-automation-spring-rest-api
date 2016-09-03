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

    /**
     * method user in `run` method below:
     * create three users and saves them in userDao:
     * 1. usual user "jd" with "ROLE_USER", rooms created later
     *   will NOT have it in room.administrators.
     * 2. room administrator "ra" with "ROLE_USER",
     *   rooms created later will have it in room.administrators
     * 3. System administrator "sa" that can do everything
     */
    private void createThreeUsersAndSaveInUsersDao() {
        // create "jd" user :
        // will be non-admin and non-room-admin
        // room.administrators will NOT contain "jd" user
        User johnDoe = new User(
                "John Doe",
                "jd",
                "jd",
                new String[]{"ROLE_USER"});
        // "ra" user will be non-admin but room-admin:
        // for both test rooms
        // room.administrators will contain "ra" user
        User roomAdminWithRoleUser = new User(
                "Room Admin",
                "ra",
                "ra",
                new String[]{"ROLE_USER"});
        // create admin user: can do everything
        User admin = new User(
                "System Administrator",
                "sa",
                "sa",
                new String[]{"ROLE_USER", "ROLE_ADMIN"});
        // and save them
        userDao.save(admin);
        userDao.save(johnDoe);
        userDao.save(roomAdminWithRoleUser);
    }

    /**
     * this method is used in `run` method below.
     * It creates 2 rooms, with 1 device in each room,
     * and 1 control in each device.
     * user "ra" from userDao will be added to
     * each room.administrators, because this is
     * the only way to add user with "ROLE_USER"
     * to room.administrators
     */
    private void createCoupleOfTestRoomsWithDevicesAndControls() {
        // get room admin user
        User roomAdminWithRoleUser = userDao.findByUsername("ra");

        // create 2: room/device/controls
        for (int i = 1; i <= 2; i++) {
            // create new Control
            Control control = new Control("control " + i, i);
            // set roomAdminUser as last modified user for control
            control.setLastModifiedBy(roomAdminWithRoleUser);

            // create new Device
            Device device = new Device("device " + i);

            // add control to it
            device.addControl(control);

            // set control.device to this new device
            control.setDevice(device);

            // create new room
            Room room = new Room("room " + i, i);

            // add "ra" user to room administrators
            room.addUserToRoomAdministrators(roomAdminWithRoleUser);

            // add device to it
            room.addDevice(device);

            // set device.room to new room
            device.setRoom(room);

            // save room
            roomDao.save(room);
        }
    }

    /**
     * This method is used in `run` method below.
     * It loads user by username. In current implementation we
     * load "sa" user in order to successfully
     * create new room: see RoomDao.save @PreAuthorize
     * If we put here non-admin user we won't be able to
     * create Room because Access will be denied, because room
     * that is not created can't have `administrators` before:
    */
    private void authenticatedUserWithUserName(String username) {
        // get UserDetails object:
        // casted from "our" com.teamtreehouse...User
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        username
                );
        // create new authentication token: our authentication object
        // that will be null however if we want to use it using
        // @PreAuthorize in RoomDao
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
        // set authentication object to security context
        SecurityContextHolder.getContext().setAuthentication(
                authenticationToken
        );
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // create three users and save them in usersDao
        createThreeUsersAndSaveInUsersDao();

        // authenticate admin user to successfully create rooms
        authenticatedUserWithUserName("sa");

        // create 2 rooms, each room having one device,
        // each device having one control
        // "ra" user will be added to roomAdministrators
        createCoupleOfTestRoomsWithDevicesAndControls();
    }
}
