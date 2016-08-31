package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao
        extends PagingAndSortingRepository<Room, Long>{

    // We allow only admins can create Room
    @Override
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Room save(@Param("room") Room room);

    // makes this GET request possible:
    // BASE_URL/rooms/search/has-name-and-area-less-than/?name=name&area=1
    // rel changes link name in HATEOAS _links.. I don't really know
    // what it should be here, so we'll just duplicate
    @RestResource(
            rel = "has-name-and-area-less-than",
            path = "has-name-and-area-less-than")
    Page<Room> findByNameAndAreaLessThan(
            @Param("name") String name,
            @Param("area") Integer area,
            Pageable pageable);
}
