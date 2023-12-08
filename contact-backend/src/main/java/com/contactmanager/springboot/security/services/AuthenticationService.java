package com.contactmanager.springboot.security.services;

import com.contactmanager.springboot.entity.UserInfo;
import com.contactmanager.springboot.dao.UserInfoRepository;
import com.contactmanager.springboot.security.dao.RefreshTokenRepository;
import com.contactmanager.springboot.security.dao.TokenRepository;
import com.contactmanager.springboot.security.dao.UserRepository;
import com.contactmanager.springboot.security.dto.AuthenticationRequest;
import com.contactmanager.springboot.security.dto.AuthenticationResponse;
import com.contactmanager.springboot.security.dto.RegisterRequest;
import com.contactmanager.springboot.security.entity.Token;
import com.contactmanager.springboot.security.entity.TokenType;
import com.contactmanager.springboot.security.entity.User;
import com.contactmanager.springboot.security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository repository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private jwtService JwtService;

    @Autowired
    private final TokenRepository tokenRepository;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        //create a user object out of this register request
        System.out.println("password : " + request.getPassword());
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        System.out.println(user);
//        User user = new User(request.getFirstname(),request.getLastname(),userDetails.getUsername(),userDetails.getPassword() );
        var savedUser = repository.save(user);

        UserInfo userInfo = UserInfo.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail()) // This can be set if you want to store it in UserInfo
                .phoneno(request.getPhoneno())
                .gender(request.getGender())
                .address(request.getAddress())
                .user(savedUser) // Set the User entity for UserInfo
                .build();

        // Save the UserInfo entity to the user_info table
        userInfoRepository.save(userInfo);
        var jwtToken = JwtService.generateToken(user);
        var refreshToken=refreshTokenService.createRefreshToken(request.getEmail());
        revokeAllUserTokens(savedUser);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken.getToken()).userid(savedUser.getId()).build();

    }

    public void saveUserToken(User savedUser, String jwtToken) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        System.out.println(authenticate);
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken=JwtService.generateToken(user);
        var refreshToken=refreshTokenService.createRefreshToken(request.getEmail());

        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken.getToken()).userid(user.getId()).build();
    }



    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse authenticateViaGoogle(String googleEmail) throws Exception {
        try {
            // Check if the email exists in the database
            var user = repository.findByEmail(googleEmail).orElse(null);
            if (user != null) {
                // If the user with the Google email exists, generate a JWT token for them
                var jwtToken = JwtService.generateToken(user);
                var refreshToken=refreshTokenService.createRefreshToken(googleEmail);
                saveUserToken(user, jwtToken);
                return AuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken.getToken()).build();
            } else {
                // Handle the case when the email does not exist in the database
                // You can return an error response or perform other actions as needed
                // For example, you can throw an exception, log the event, or return a specific error response.
                throw new Exception("Email not found in the database");
            }
        } catch (Exception e) {
            System.out.print(e);
            throw e;
        }
    }

}