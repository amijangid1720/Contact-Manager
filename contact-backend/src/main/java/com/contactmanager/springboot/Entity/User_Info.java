package com.contactmanager.springboot.Entity;

import com.contactmanager.springboot.contacts.Contact;
import com.contactmanager.springboot.security.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_info")
public class User_Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserInfo_id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email",unique = true) //@Column(name = "email", unique = true)
    private String email;

    @Column(name = "phoneno")
    private String phoneno;

    @Column(name="gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id") // Define the foreign key relationship
    private User user;





}
