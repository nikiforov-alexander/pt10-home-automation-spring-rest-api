package com.teamtreehouse.home;

import com.teamtreehouse.home.dao.ControlDao;
import com.teamtreehouse.home.dao.DeviceDao;
import com.teamtreehouse.home.dao.RoomDao;
import com.teamtreehouse.home.dao.UserDao;
import com.teamtreehouse.home.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataLoaderTest {
    @Mock
    private RoomDao roomDao;
    @Mock
    private ControlDao controlDao;
    @Mock
    private DeviceDao deviceDao;
    @Mock
    private UserDao userDao;

    private DataLoader dataLoader;

    @Before
    public void setUp() throws Exception {
        dataLoader = new DataLoader(
                roomDao,
                deviceDao,
                controlDao,
                userDao
        );
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void generateAdministratorsListShouldGenerateCorrectListOfAdmins()
            throws Exception {
        // Arrange:
        // create testAllUserList with all users
        // that will be returned when userDao will be called
        List<User> testAdminList = new ArrayList<>();
        // create testAdminList that has only users with admin roles
        // and is considered to be correct list
        List<User> testAllUsersList = new ArrayList<>();
        // Arrange:
        // create two users: normal user and admin user with two roles
        User normalUser = new User(
                "Normal User",
                "username",
                "password",
                new String[]{"ROLE_USER"}
        );
        User adminUser = new User(
                "System Administrator",
                "sa",
                "password",
                new String[]{"ROLE_USER", "ROLE_ADMIN"}
        );
        // add both users to testAllUsersList
        testAllUsersList.add(normalUser);
        testAllUsersList.add(adminUser);
        // add adminUser to testAdminList
        testAdminList.add(adminUser);
        // Arrange that userDao.findAll returns list with
        // all users
        when(userDao.findAll()).thenReturn(testAllUsersList);

        // Act and Assert:
        // When generateListOfAdministrators is called
        List<User> adminListReturnedByTestedMethod =
                dataLoader.generateListOfAdministrators();
        // Then list with one admin should be returned
        // and equal to arranged testAdminList
        assertEquals(
                testAdminList,
                adminListReturnedByTestedMethod
        );
    }
}