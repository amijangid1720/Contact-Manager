package com.contactmanager.springboot.entity;

import com.contactmanager.springboot.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserInfo_id;

    @Column(name = "firstname",nullable = false)
    private String firstName;

    @Column(name = "lastname",nullable = false)
    private String lastName;

    @Column(name = "email",unique = true,nullable = false) //@Column(name = "email", unique = true)
    private String email;

    @Column(name = "phoneno",nullable = true)
    private String phoneno;


    @Column(name="gender")
    private String gender;

    @Column(name = "address")
    private String address;


    @Getter
    @Column(name = "detailsFilled")
    private Boolean detailsFilled;

    @Column(name = "profilePicture")
    private String profilePicture;

    @Column(name = "folderid")
    private String folderId;

    @Column(name = "fileid")
    private String fileId;

    @OneToOne
    @JoinColumn(name = "userid") // This column references the "id" column in the "user_login" table
    @JsonIgnore
    private User user;


    @Override
    public String toString() {
        return "UserInfo{" +
                "UserInfo_id=" + UserInfo_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", detailsFilled=" + detailsFilled +
                ", profilePicture='" + profilePicture + '\'' +
                ", folderId='" + folderId + '\'' +
                ", fileId='" + fileId + '\'' +
                ", user=" + user +
                '}';
    }

    public boolean isDetailsFilled() {
        return detailsFilled != null && detailsFilled;
    }
}
