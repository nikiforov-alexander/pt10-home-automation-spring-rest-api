package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao
        extends CrudRepository<Room, Long> {
}
