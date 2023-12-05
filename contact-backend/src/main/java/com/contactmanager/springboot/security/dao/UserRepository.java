package com.contactmanager.springboot.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.contactmanager.springboot.security.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
