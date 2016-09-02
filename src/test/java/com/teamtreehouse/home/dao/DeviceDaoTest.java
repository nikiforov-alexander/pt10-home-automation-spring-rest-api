package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.Application;
import com.teamtreehouse.home.model.Device;
import com.teamtreehouse.home.model.Room;
import com.teamtreehouse.home.service.CustomUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

// run integration test because that is the only way
// to test Spring Data Reps
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
// user test.properties with in memory testing db
@TestPropertySource("classpath:/test.properties")
public class DeviceDaoTest {
    // autowire user Dao
    @Autowired
    private UserDao userDao;

    // autowire room Dao
    @Autowired
    private RoomDao roomDao;

    // autowire device Dao
    @Autowired
    private DeviceDao deviceDao;

    // autowire user details service to load users
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // login user depending on username
    // can be "sa" with ROLE_USER, ROLE_ADMIN
    // or "jd" with ROLE_USER
    // or "oa" with ROLE_USER, ROLE_ADMIN but not room admin
    private void setUpUserByUsername(String username) {
        UserDetails user =
                customUserDetailsService.loadUserByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user , null, user.getAuthorities()
                )
        );
    }

    @Test
    public void devicesCanBeSearchedByNameContaining() throws Exception {
        // Arrange:
        setUpUserByUsername("sa");

        // take first room from roomDao
        Room room = roomDao.findOne(1L);
        // create new device
        Device newDevice = new Device("test");
        // add device to room
        newDevice.setRoom(room);
        // add test device to deviceDao with name "test"
        deviceDao.save(newDevice);

        // Act and Assert:
        // When we try to search by name "test"
        Page<Device> page = deviceDao.findByNameContaining(
                "test", new PageRequest(1, 20));

        // Then we should get page with 1L total elements
        // and 1 page
        assertThat(
                page.getTotalElements(), is(1L)
        );
        assertThat(
                page.getTotalPages(), is(1)
        );
    }

    @Test
    public void devicesCanBeSavedByAdminUsers()
            throws Exception {
        // Arrange:
        // login user "sa" with "ROLE_ADMIN"
        setUpUserByUsername("sa");

        // take first room from roomDao
        Room room = roomDao.findOne(1L);
        // create new device
        Device newDevice = new Device("device-to-be-saved-by-admin");
        // add device to room
        newDevice.setRoom(room);

        // Act, and Assert:
        // When we try to save new Device with room
        Device savedDevice = deviceDao.save(newDevice);

        // Then savedDevice should not be null
        assertThat(
                savedDevice,
                notNullValue(Device.class)
        );
    }

    @Test(expected = AccessDeniedException.class)
    public void devicesCannotBeSavedByNonAdminNonRoomAdminUsers()
            throws Exception {
        // Arrange:
        // login user "jd" non-admin non-room-administrator
        setUpUserByUsername("jd");

        // take first room from roomDao
        Room room = roomDao.findOne(1L);
        // create new device
        Device newDevice = new Device("test");
        // add device to room
        newDevice.setRoom(room);

        // Act, and Assert:
        // When we try to save new Device with room
        deviceDao.save(newDevice);
        // Then AccessDeniedException should be thrown
    }
}