package com.contactmanager.springboot.security.auth;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RefreshTokenRequest {
    private String refreshToken;
}
