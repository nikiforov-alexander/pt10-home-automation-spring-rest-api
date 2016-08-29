package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao
        extends PagingAndSortingRepository<Room, Long>{

    // We allow only admins and users that are defined in
    // room's administrators to save Room
    @Override
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "#room.hasAdministrator(authentication.principal)")
    Room save(@Param("room") Room room);
}
