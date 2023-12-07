package com.contactmanager.springboot.entity;
// import com.contactmanager.springboot.Entity.User_Info;
import com.contactmanager.springboot.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties({"hibernateLazyInitializer"})
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

    @Column(name = "favorite")
    @ColumnDefault("false")
    private Boolean favorite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "friend")
    private Boolean isFriend;

    @Column(name = "family")
    private Boolean isFamily;

    @Column(name = "colleague")
    private Boolean isColleague;


}
