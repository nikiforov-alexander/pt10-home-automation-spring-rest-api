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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    <S extends Room> S save(@Param("room") S room);

    // makes this GET request possible:
    // BASE_URL/rooms/search/has-area-less-than/?area=1
    // rel changes link name in HATEOAS _links.. I don't really know
    // what it should be here, so we'll just duplicate
    @RestResource(
            rel = "has-area-less-than",
            path = "has-area-less-than")
    Page<Room> findByAreaLessThan(
            @Param("area") Integer area,
            Pageable pageable);

    // makes this GET request possible:
    // BASE_URL/rooms/search/has-name/?name=name
    // rel changes link name in HATEOAS _links.. I don't really know
    // what it should be here, so we'll just duplicate
    @RestResource(
            rel = "has-name",
            path = "has-name")
    Page<Room> findByName(
            @Param("name") String name,
            Pageable pageable);
}
