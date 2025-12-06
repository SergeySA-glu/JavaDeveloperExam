package com.tasktracker.model.service;

import com.tasktracker.model.entity.User;
import com.tasktracker.security.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService{
    private final SecretKey signingKey;

    public JwtService(JwtConfig jwtConfig) {
        this.signingKey = Keys.hmacShaKeyFor(jwtConfig.getKey().getBytes());
    }

    public String generateToken(UserDetails userDetails, Date expirationDate) {
        Map<String, Object> additionalClaims = new HashMap<>();
        if (userDetails instanceof User user) {
            additionalClaims.put("id", user.getId());
            additionalClaims.put("email", user.getEmail());
            additionalClaims.put("role", user.getRole());
        }
        return generateToken(userDetails, expirationDate, additionalClaims);
    }

    private String generateToken(UserDetails userDetails, Date expirationDate, Map<String, Object> additionalClaims) {
        return Jwts.builder()
                .claims(additionalClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
