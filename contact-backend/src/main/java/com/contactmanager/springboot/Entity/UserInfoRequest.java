package com.contactmanager.springboot.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneno;
    private String gender;
    private String address;
    private String profilePicture;

    @Override
    public String toString() {
        return "UserInfoRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
