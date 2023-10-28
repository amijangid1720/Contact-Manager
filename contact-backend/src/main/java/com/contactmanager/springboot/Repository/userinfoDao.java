package com.contactmanager.springboot.Repository;

import com.contactmanager.springboot.Entity.User_Info;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userinfoDao extends JpaRepository<User_Info,Integer> {
}
