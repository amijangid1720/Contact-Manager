package com.contactmanager.springboot.services;


import com.contactmanager.springboot.Entity.UserInfo;
import com.contactmanager.springboot.Repository.UserInfoRepository;
import com.contactmanager.springboot.security.Repository.UserRepository;
import com.contactmanager.springboot.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Optional;
@Service
public class UserInfoService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserRepository userRepository;
    public UserInfo getUserInfo(String username) {
        Optional<UserInfo> userInfo=userInfoRepository.findByEmail(username);
        if (userInfo.isPresent()) {
            return userInfo.get(); // Extract the UserInfo object
        } else {
            // Handle the case where UserInfo is not found (e.g., return null, throw an exception, etc.)
            return null; // You should consider better error handling here
        }
    }



    public void markDetailsAsFilled(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserInfo userInfo = user.getUserInfo();
            if (userInfo != null) {
                userInfo.setDetailsFilled(true);
                userInfoRepository.save(userInfo);
            }
        }

    }
    public boolean checkEmailExists(String email) {

        Optional<UserInfo> userInfo=userInfoRepository.findByEmail(email);
//        System.out.println(userInfo);
       if(userInfo.isEmpty()) return false;
        else return true;
    }

    public boolean checkPhoneExists(String phoneno) {
        Optional<UserInfo> userInfo=userInfoRepository.findByPhoneno(phoneno);
        System.out.println("------------------------------------------------------");
        System.out.println(userInfo);
        if(userInfo.isEmpty()) return false;
        else return true;
    }
    public void updateUserProfilePicture(Integer userid,String imageUrl)
    {
        Optional<UserInfo> optionalUser = userInfoRepository.findById(userid);

        if (optionalUser.isPresent()) {

            UserInfo user = optionalUser.get();
            user.setProfilePicture(imageUrl);
            userInfoRepository.save(user);
        } else {
            // Handle user not found, throw an exception, or perform other actions
            throw new RuntimeException("User not found with ID: " + userid);
        }
    }

}