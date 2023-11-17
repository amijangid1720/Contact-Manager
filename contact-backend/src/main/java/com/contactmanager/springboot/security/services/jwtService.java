package com.contactmanager.springboot.security.services;
import com.contactmanager.springboot.security.Repository.UserRepository;
import com.contactmanager.springboot.security.user.User;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.function.Function;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Base64;

@Service
public class jwtService {
    @Autowired
    UserRepository userRepository;
    private static final String SECRET_KEY="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails)
    {
        return generateToken(new HashMap<>(),userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
//
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 120))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    //in summary, this code sets up a JWT parser,
    // configures it with a signing key, parses a JWT (checking its signature in the process),
    // and then retrieves the claims (user-related information) from the JWT's payload.
    // It's a common operation when working with JWTs in Java, especially in the context of authentication and authorization.
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser() //ets up a JWT parser, which is used to read and understand the content of a JWT.
                .setSigningKey(getSignInKey()) // In order to decode token we need signing key
                .parseClaimsJws(token) // parsing our token This part takes the JWT token and parses it. It checks the signature and verifies the contents.
                .getBody(); // After successfully parsing the JWT, this part retrieves the claims from the JWT's payload. Claims are the pieces of information contained within the token.
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
