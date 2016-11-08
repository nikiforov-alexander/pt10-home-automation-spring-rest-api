package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.Control;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlDao
        extends PagingAndSortingRepository<Control, Long>{

    // here i ensure that only if user is in
    // room.administrators OR he is administrator
    // he can save/modify control
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
        "#control.device.room.hasAdministrator(authentication.principal)")
    @Override
    <S extends Control> S save(@Param("control") S control);
}
