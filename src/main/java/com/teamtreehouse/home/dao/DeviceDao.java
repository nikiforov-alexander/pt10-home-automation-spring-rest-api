package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Device;
import com.teamtreehouse.home.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDao
        extends PagingAndSortingRepository<Device, Long> {
    // makes this query possible:
    // BASE_URL/devices/search/contains-name?name=query
    // that will search for all devices containing "query" in name
    @RestResource(rel = "contains-name", path = "contains-name")
    Page<Device> findByNameContaining(
            @Param("name") String name,
            Pageable pageable);

    // here i ensure that only if user is in
    // room.administrators OR he is administrator
    // he can save/modify device
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "#device.room.hasAdministrator(authentication.principal)")
    <S extends Device> S save(@Param("device") S device);
}
