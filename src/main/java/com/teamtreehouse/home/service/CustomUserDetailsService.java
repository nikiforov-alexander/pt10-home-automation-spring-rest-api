package com.teamtreehouse.home.service;

import com.teamtreehouse.home.dao.UserDao;
import com.teamtreehouse.home.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " was not found");
        }
        // instead of returning org.springframework.security.core.userdetails
        // I will return my User implementing UserDetails, so that
        // when I check whether Room.hasAdministrator(Principal principal)
        // using SpEl in RoomDao.save method:
        // ...#room.hasAdministrator(authentication.principal)
        // The authentication.principal will be our com.teamtreehouse.User
        // and not the org.springframework.security.core.userdetails.User
        // see below: @craigsdennis variant
        return user;
//        return new org.springframework.security.core.userdetails
//                .User(
//                    user.getUsername(),
//                    user.getPassword(),
//                    AuthorityUtils.createAuthorityList(user.getRoles()
//                )
//        );
    }
}
