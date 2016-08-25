package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Control;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlDao
        extends PagingAndSortingRepository<Control, Long>{
}
