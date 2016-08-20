package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Room;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoomDao
        extends PagingAndSortingRepository<Room, Long>{
}
