package com.contactmanager.springboot.security.auth;

import com.contactmanager.springboot.security.services.RefreshTokenService;
import com.contactmanager.springboot.security.services.jwtService;
import com.contactmanager.springboot.security.token.RefreshToken;
import com.contactmanager.springboot.security.user.User;
import com.contactmanager.springboot.services.UserInfoService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    private final com.contactmanager.springboot.security.auth.AuthenticationService service;
    @Autowired
    private final jwtService jwtservice;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @PostMapping("/register")
    public ResponseEntity<com.contactmanager.springboot.security.auth.AuthenticationResponse> register(
            @RequestBody com.contactmanager.springboot.security.auth.RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<com.contactmanager.springboot.security.auth.AuthenticationResponse> authenticate(
            @RequestBody com.contactmanager.springboot.security.auth.AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    //    @PostMapping("/google")
//    public ResponseEntity<String> authenticateGoogle(@RequestBody String idToken) {
//        // Your logic to handle the received ID token
//        // Validate the token, extract user information, and perform other actions
//        System.out.println("Received ID token from frontend: " + idToken);
//
//        // You can return a success message or any relevant response
//        return ResponseEntity.ok().body("{\"message\": \"ID token received successfully\"}");
//    }
    @PostMapping("/google")
    public ResponseEntity<String> authenticateGoogle(@RequestBody String idToken) throws  Exception{
        try {
            // Initialize the Google API client
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();


            // Build the Google ID token verifier
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList("360599613542-j8il63optd440q20iknopkmttkev7lj2.apps.googleusercontent.com")) // Replace with your actual client ID
                    .build();


            // Verify the ID token
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                // Extract user information from the payload
                GoogleIdToken.Payload payload = googleIdToken.getPayload();
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);
                System.out.println(payload.getEmail());

                // Add your logic to process the verified ID token
                // ...
                com.contactmanager.springboot.security.auth.AuthenticationResponse response = service.authenticateViaGoogle(payload.getEmail());

                System.out.println("response.toString()");
                System.out.println(response.toString());
                return ResponseEntity.ok().body("{\"token\": \""+response.getToken()+"\"}");
            } else {
                System.out.println("Invalid ID token.");
                return ResponseEntity.badRequest().body("{\"error\": \"Invalid ID token\"}");
            }
        } catch (GeneralSecurityException | IOException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\": \"Internal Server Error\"}");
        }


    }
    @PostMapping("/check-duplicate")
    public DuplicateCheckResponse checkDuplicate(@RequestBody DuplicateCheckRequest request) {
        boolean emailExists = userInfoService.checkEmailExists(request.getEmail());
        boolean phoneExists = userInfoService.checkPhoneExists(request.getPhoneno());

        return new DuplicateCheckResponse(emailExists, phoneExists);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshJwtToken(@RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        RefreshToken refreshToken=refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());

        User user=refreshToken.getUser();
        String token =this.jwtservice.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .build();

    }

}
