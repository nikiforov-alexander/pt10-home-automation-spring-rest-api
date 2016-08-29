package com.teamtreehouse.home.handler;

import com.teamtreehouse.home.dao.UserDao;
import com.teamtreehouse.home.model.Control;
import com.teamtreehouse.home.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Control.class)
public class ControlEventHandler {
    private final UserDao userDao;

    @Autowired
    public ControlEventHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    // Before creation and save of Control currently logged on User
    // will be set to Control.lastModifiedBy
    @HandleBeforeCreate
    @HandleBeforeSave
    public void addUserBasedOnLoggedInUser(Control control) {
        // get username from security context
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        // find user ny username
        User user = userDao.findByUsername(username);
        System.out.println("HERE");
        // set last modified ny to this control
        control.setLastModifiedBy(user);
    }

}
