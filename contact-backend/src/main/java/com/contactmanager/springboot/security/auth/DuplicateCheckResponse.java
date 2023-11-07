package com.contactmanager.springboot.security.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateCheckResponse {
    Boolean emailExists;
    Boolean phoneExists;
}
