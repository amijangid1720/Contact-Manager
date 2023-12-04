package com.contactmanager.springboot.contacts;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContactRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String work;
    private String phoneno;
    private String gender;
    private String description;
    private String favourite;
    private Boolean isFriend;
    private Boolean isFamily;
    private Boolean isColleague;

}
