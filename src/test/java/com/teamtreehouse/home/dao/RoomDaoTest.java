package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.Application;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

// run integration test because that is the only way
// to test Spring Data Reps
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
// user test.properties with in memory testing db
@TestPropertySource("classpath:/test-RoomDaoTest.properties")
public class RoomDaoTest {
    // autowire roomDao
    @Autowired
    private RoomDao roomDao;

    // autowire userDetailsService to load user
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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

    @Test
    public void roomsCanBeSearchedByName() throws Exception {
        // Arrange:
        // login admin user
        setUpUserByUsername("sa");
        // add test room to roomDao with name "test"
        roomDao.save(new Room("test", 1));

        // Act and Assert:
        // When we try to search by name "test"
        Page<Room> page = roomDao.findByName("test", new PageRequest(1, 20));

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
    public void roomsCanBeSearchedByAreaLessThan() throws Exception {
        // Arrange:
        // login admin user
        setUpUserByUsername("sa");
        // add test room to roomDao with area 1
        roomDao.save(new Room("room with area 0", 0));

        // Act and Assert:
        // When we try to search by area less than 1
        Page<Room> page = roomDao.findByAreaLessThan(1 , new PageRequest(1, 20));

        // Then we should get page with 1L total elements
        // and 1 page
        assertThat(
                page.getTotalElements(), is(1L)
        );
        assertThat(
                page.getTotalPages(), is(1)
        );
    }
}