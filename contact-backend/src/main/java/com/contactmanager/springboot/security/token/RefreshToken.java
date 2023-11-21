package com.contactmanager.springboot.security.token;
import com.contactmanager.springboot.security.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;


    @OneToOne
    private User user;
}
