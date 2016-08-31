package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.Application;
import com.teamtreehouse.home.model.Room;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

// run integration test because that is the only way
// to test Spring Data Reps
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public class RoomDaoTest {
    // autowire roomDao
    @Autowired
    private RoomDao roomDao;

    // autowire userDetailsService to load user
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // setting port number to 8081, so that we
    // do not interfere with real bootRun
    static {
        System.setProperty("server.port", "8081");
    }

    // login user depending on username
    // can be "sa" with ROLE_USER, ROLE_ADMIN
    // or "jd" with ROLE_USER
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
    public void userWithAdminRoleCanCreateRoom() throws Exception {
        // Arrange:
        // login admin user
        setUpUserByUsername("sa");
        // create test room
        Room room = new Room("test room", 1);

        // Act and Assert:
        // When test room is saved with admin user
        Room savedRoom = roomDao.save(room);

        // Then some room can be found by id in
        // roomDao
        assertThat(
                roomDao.findOne(
                        savedRoom.getId()
                ),
                notNullValue(Room.class)
        );
    }

    @Test(expected = AccessDeniedException.class)
    public void userWithSimpleRoleCannotCreateRoom() throws Exception {
        // Arrange:
        // login "jd" user
        setUpUserByUsername("jd");
        // create test room
        Room room = new Room("test room", 1);

        // Act and Assert:
        // When test room is saved with user
        roomDao.save(room);

        // Then AccessDeniedException should be thrown
    }
}