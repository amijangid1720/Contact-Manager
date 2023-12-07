package com.contactmanager.springboot.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateCheckRequest {

    String email;
    String phoneno;
}
