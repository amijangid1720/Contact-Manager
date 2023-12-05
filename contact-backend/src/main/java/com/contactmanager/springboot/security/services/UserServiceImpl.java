package com.contactmanager.springboot.security.services;

import com.contactmanager.springboot.entity.UserInfo;
import com.contactmanager.springboot.dao.UserInfoRepository;
import com.contactmanager.springboot.security.dao.UserRepository;
import com.contactmanager.springboot.security.dao.RefreshTokenRepository;
import com.contactmanager.springboot.security.entity.Role;
import com.contactmanager.springboot.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public User loadUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null); // You can also throw an exception if you prefer
    }

    @Override
    public User loadUserByGoogleEmail(String email, String firstName, String lastName) {
        // Load or create a user based on the Google email
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.orElseGet(() -> createUserFromGoogle(email, firstName, lastName));
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

    private User createUserFromGoogle(String email, String firstName, String lastName) {
        // Create a new user using Google email and other relevant information
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setRole(Role.USER);

        // Set a default password if it's null
        if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
            newUser.setPassword(new BCryptPasswordEncoder().encode("defaultPassword"));
        }

        // Save the user to the repository
        userRepository.save(newUser);

        // Create UserInfo for the user with extracted first and last names
        UserInfo userInfo = UserInfo.builder()
                .firstName(firstName)  // Set the actual first name extracted from Google login response
                .lastName(lastName)    // Set the actual last name extracted from Google login response
                .email(email)
                .detailsFilled(false)
                // Set email from the Google login response
                .user(newUser)          // Set the User entity for UserInfo
                .build();

        // Save the UserInfo to the repository
        userInfoRepository.save(userInfo);

        // Return the created user
        return newUser;



    }
}