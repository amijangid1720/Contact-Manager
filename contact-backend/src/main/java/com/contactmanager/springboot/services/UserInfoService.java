package com.contactmanager.springboot.services;


import com.contactmanager.springboot.Entity.UserInfo;
import com.contactmanager.springboot.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserInfoService {
    @Autowired
    UserInfoRepository userInfoRepository;
    public UserInfo getUserInfo(String username) {
        Optional<UserInfo> userInfo=userInfoRepository.findByEmail(username);
        if (userInfo.isPresent()) {
            return userInfo.get(); // Extract the UserInfo object
        } else {
            // Handle the case where UserInfo is not found (e.g., return null, throw an exception, etc.)
            return null; // You should consider better error handling here
        }
    }
}