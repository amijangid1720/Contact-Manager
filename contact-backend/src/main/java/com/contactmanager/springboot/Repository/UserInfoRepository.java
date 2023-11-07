package com.contactmanager.springboot.Repository;

import com.contactmanager.springboot.Entity.UserInfo;
import com.contactmanager.springboot.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByPhoneno(String phoneno);
}
