package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Device;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDao
        extends PagingAndSortingRepository<Device, Long> {
}
