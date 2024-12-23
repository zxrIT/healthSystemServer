package com.ZengXiangRui.authentication.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {
    @Value("{token.securitySecretKey}")
    private String SecretKey;

    public String createToken(String id, String username) {
        long expirationTime = 1000 * 60 * 60 * 24;
        return Jwts.builder()
                .setSubject("health")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim("id", id)
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS256, SecretKey).compact();
    }
}
