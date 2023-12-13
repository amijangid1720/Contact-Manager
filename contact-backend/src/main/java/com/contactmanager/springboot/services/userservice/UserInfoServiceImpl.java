package com.contactmanager.springboot.services.userservice;


import com.contactmanager.springboot.entity.UserInfo;
import com.contactmanager.springboot.dao.UserInfoRepository;
import com.contactmanager.springboot.security.dao.UserRepository;
import com.contactmanager.springboot.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
@Service
public class UserInfoServiceImpl implements UserInfoService {
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

public UserInfo getUserInfoByEmail(String email){
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(email);
        if(userInfo.isPresent()){
            return userInfo.get();
        }
        else {
            return null;
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

    public void updateUserProfilePicture(Map data) {
        Object userIdObject = data.get("userid");

        if (userIdObject != null) {
            try {
                int userId = Integer.parseInt(userIdObject.toString());
                UserInfo user = userInfoRepository.findByUserId(userId);

                if (user != null) {
                    user.setProfilePicture(data.get("url").toString());
                    userInfoRepository.save(user);
                } else {
                    // Handle the case where the user is not found
                    System.out.println("User not found with ID: " + userId);
                }
            } catch (NumberFormatException e) {
                // Handle the case where the user ID is not a valid integer
                System.out.println("Invalid user ID format: " + userIdObject);
            }
        } else {
            // Handle the case where the user ID is not present in the data map
            System.out.println("User ID is not present in the data map");
        }
    }

    @Override
    public void saveUserInfo(UserInfo userInfo) {
          userInfoRepository.save(userInfo);
    }
}