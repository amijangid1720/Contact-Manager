package com.contactmanager.springboot.contacts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String work;
    private String phoneno;
    private String gender;
    private String description;

    // Constructors, getters, and setters

    @Override
    public String toString() {
        return "ContactRequest{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", work='" + work + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", gender='" + gender + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
