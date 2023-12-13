package com.contactmanager.springboot.services.userservice;

import com.contactmanager.springboot.entity.UserInfo;

import java.util.Map;

public interface UserInfoService {
    public UserInfo getUserInfo(String username);

    public UserInfo getUserInfoByEmail(String email);
    public void markDetailsAsFilled(Integer id);
    public boolean checkEmailExists(String email);
    public boolean checkPhoneExists(String phoneno);
    public void updateUserProfilePicture(Map data);
    void saveUserInfo(UserInfo userInfo);

}
