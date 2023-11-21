package com.contactmanager.springboot.security.services;

import com.contactmanager.springboot.security.Repository.UserRepository;
import com.contactmanager.springboot.security.token.RefreshToken;
import com.contactmanager.springboot.security.token.RefreshTokenRepository;
import com.contactmanager.springboot.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Override
    public User loadUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null); // You can also throw an exception if you prefer
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = loadUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return user;
    }


}
