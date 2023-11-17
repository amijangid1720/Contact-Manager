package com.contactmanager.springboot.security.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneno;
    private String gender;
    private String address;
    private Boolean  detailsFilled;

}
