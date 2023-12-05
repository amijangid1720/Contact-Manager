package com.contactmanager.springboot.dto;

import com.contactmanager.springboot.entity.Contact;
import lombok.*;

import java.util.List;

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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class contactResponse {
        private List<Contact> contacts;
        private long totalContacts;
    }
}
