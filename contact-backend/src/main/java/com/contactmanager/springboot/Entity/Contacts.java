package com.contactmanager.springboot.Entity;
import java.util.*;
import com.contactmanager.springboot.security.user.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "work")
    private String work;

    @Column(name = "phoneno",unique = true)
    private String phoneno;

    @Column(name = "gender")
    private String gender;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "user_contacts",
            joinColumns = @JoinColumn(name = "contact_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();


}
