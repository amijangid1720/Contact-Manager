package com.contactmanager.springboot.security.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RefreshTokenRequest {
    private String refreshToken;

}
