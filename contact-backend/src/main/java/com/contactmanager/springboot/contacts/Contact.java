package com.contactmanager.springboot.contacts;
import com.contactmanager.springboot.Entity.User_Info;
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
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
