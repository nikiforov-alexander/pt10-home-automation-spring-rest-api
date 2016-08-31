package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.Application;
import com.teamtreehouse.home.model.Device;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

// run integration test because that is the only way
// to test Spring Data Reps
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public class DeviceDaoTest {
    // autowire device Dao
    @Autowired
    private DeviceDao deviceDao;

    // setting port number to 8081, so that we
    // do not interfere with real bootRun
    static {
        System.setProperty("server.port", "8081");
    }

    @Test
    public void devicesCanBeSearchedByNameContaining() throws Exception {
        // Arrange:
        // add test device to deviceDao with name "test"
        deviceDao.save(new Device("test device"));

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
}