package com.contactmanager.springboot.security.services;

import com.contactmanager.springboot.security.dao.UserRepository;
import com.contactmanager.springboot.security.entity.RefreshToken;
import com.contactmanager.springboot.security.dao.RefreshTokenRepository;
import com.contactmanager.springboot.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;
    @Value("${contact-manager.app.refreshTokenValidity}")
    public long refreshTokenValidity;

    public RefreshToken createRefreshToken(String userName) {
        User currentUser = userRepository.findByEmail(userName).get();
        RefreshToken refreshToken = currentUser.getRefreshToken();
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(refreshTokenValidity))
                    .user(currentUser)
                    .build();

        } else {
            //update expiry of refresh token
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenValidity));
        }
        currentUser.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        //RefreshToken refreshTokenOb = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new RuntimeException("Refresh Token Does not Exist"));
        try {
            RefreshToken refreshTokenOb = refreshTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Refresh Token Does not Exist"));

            if (refreshTokenOb.getExpiryDate().compareTo(Instant.now()) < 0) {
                refreshTokenRepository.delete(refreshTokenOb);
                throw new RuntimeException("Refresh Token Expired");
            }

            return refreshTokenOb;
        } catch (RuntimeException e) {
            // Log the exception or handle it appropriately
            throw new RuntimeException("Error verifying refresh token", e);
        }
    }
}
