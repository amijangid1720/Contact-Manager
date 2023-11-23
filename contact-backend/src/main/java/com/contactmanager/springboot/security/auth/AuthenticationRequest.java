package com.contactmanager.springboot.security.auth;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    String email;
    String password;
}
