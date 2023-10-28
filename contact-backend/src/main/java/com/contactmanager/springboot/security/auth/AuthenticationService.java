package com.contactmanager.springboot.security.auth;

import com.contactmanager.springboot.security.Repository.UserRepository;
import com.contactmanager.springboot.security.services.jwtService;
import com.contactmanager.springboot.security.token.Token;
import com.contactmanager.springboot.security.token.TokenRepository;
import com.contactmanager.springboot.security.token.TokenType;
import com.contactmanager.springboot.security.user.User;
import com.contactmanager.springboot.security.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository repository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private jwtService JwtService;

    @Autowired
    private final TokenRepository tokenRepository;
    public AuthenticationResponse register(com.contactmanager.springboot.security.auth.RegisterRequest request) {
      //create a user object out of this register request
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
//        User user = new User(request.getFirstname(),request.getLastname(),userDetails.getUsername(),userDetails.getPassword() );
        var savedUser=repository.save(user);
        var jwtToken=JwtService.generateToken(user);
        revokeAllUserTokens(savedUser);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    private void saveUserToken(User savedUser, String jwtToken) {
        var token= Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )

        );
        var user=repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken=JwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public AuthenticationResponse authenticateViaGoogle(String googleEmail) throws  Exception {
        try {
            // Check if the email exists in the database
            var user = repository.findByEmail(googleEmail).orElse(null);
            if (user != null) {
                // If the user with the Google email exists, generate a JWT token for them
                var jwtToken = JwtService.generateToken(user);

                saveUserToken(user, jwtToken);
                return AuthenticationResponse.builder().token(jwtToken).build();
            } else {
                // Handle the case when the email does not exist in the database
                // You can return an error response or perform other actions as needed
                // For example, you can throw an exception, log the event, or return a specific error response.
                throw new Exception("Email not found in the database");
            }
        } catch (Exception e) {
            System.out.print(e);
            throw  e;
        }
    }

}
