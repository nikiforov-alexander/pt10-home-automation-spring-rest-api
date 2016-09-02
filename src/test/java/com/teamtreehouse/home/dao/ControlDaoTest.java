package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.Application;
import com.teamtreehouse.home.model.Control;
import com.teamtreehouse.home.model.Device;
import com.teamtreehouse.home.service.CustomUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

// run integration test because that is the only way
// to test Spring Data Reps
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
// user test.properties with in memory testing db
@TestPropertySource("classpath:/test-ControlDaoTest.properties")
public class ControlDaoTest {

    // autowire user Dao
    @Autowired
    private UserDao userDao;

    // autowire control Dao
    @Autowired
    private ControlDao controlDao;

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

    @Test(expected = AccessDeniedException.class)
    public void controlsCannotBeSavedByNonAdminsNonRoomAdmins()
            throws Exception {
        // Arrange:
        // login user "jd" : non-admin, non-room-admin
        setUpUserByUsername("jd");

        // take first room from roomDao
        Device device = deviceDao.findOne(1L);
        // create new control
        Control newControl = new Control("control", 1);
        // set control.device to first device
        newControl.setDevice(device);

        // Act and Assert:
        // When we try to save new Control with Device
        // Then AccessDeniedException should be thrown
        controlDao.save(newControl);
    }

    @Test
    public void controlsCanBeSavedByAdmin()
            throws Exception {
        // Arrange:
        // login user "sa" : user with "ROLE_ADMIN"
        setUpUserByUsername("sa");

        // take first room from roomDao
        Device device = deviceDao.findOne(1L);
        // create new control
        Control newControl = new Control("control", 1);
        // set control.device to first device
        newControl.setDevice(device);

        // Act and Assert:
        // When we try to save new Control with Device
        Control savedControl = controlDao.save(newControl);

        // Then savedControl returned by dao should not be null
        assertThat(
                savedControl,
                notNullValue(Control.class)
        );
    }
}