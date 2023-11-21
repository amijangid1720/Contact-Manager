package com.contactmanager.springboot.security.services;

import com.contactmanager.springboot.security.token.RefreshToken;
import com.contactmanager.springboot.security.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface    UserService extends UserDetailsService {
    User loadUserByEmail(String email);

    User saveUser(User user);





}
