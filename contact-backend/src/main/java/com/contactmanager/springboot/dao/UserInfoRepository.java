package com.contactmanager.springboot.dao;

import com.contactmanager.springboot.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {
    Optional<UserInfo> findByEmail(String email);
    UserInfo findByUserId(Integer id);
    Optional<UserInfo> findByPhoneno(String phoneno);
//    List<UserInfo> findByUserId(Integer id);
}
