package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDao
        extends PagingAndSortingRepository<Device, Long> {
    // makes this query possible:
    // BASE_URL/devices/search/contains-name?name=query
    // that will search for all devices containing "query" in name
    @RestResource(rel = "name", path = "contains-name")
    Page<Device> findByNameContaining(
            @Param("name") String name,
            Pageable pageable);
}
