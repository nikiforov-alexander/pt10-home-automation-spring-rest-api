package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// make sure that this directory is not exposed to REST API
@RepositoryRestResource(exported = false)
public interface UserDao extends CrudRepository<User, Long> {
    // "query" method of SpringData: finds user by username
    User findByUsername(String username);
}
