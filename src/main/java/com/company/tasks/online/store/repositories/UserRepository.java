package com.company.tasks.online.store.repositories;

import com.company.tasks.online.store.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select distinct u from User u " +
            "join fetch u.roles " +
            "where u.name=:userName and u.enabled=1")
    User getUserByUserName(String userName);


    User findUserByName(String userName);
}
