package com.contactmanager.springboot.security.dao;
import com.contactmanager.springboot.security.entity.RefreshToken;
import com.contactmanager.springboot.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
