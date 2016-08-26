package com.teamtreehouse.home.dao;

import com.teamtreehouse.home.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserDao extends PagingAndSortingRepository<User, Long>{
    User findByUsername(String username);
}
