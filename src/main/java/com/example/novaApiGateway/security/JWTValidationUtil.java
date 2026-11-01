package com.example.novaApiGateway.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class JWTValidationUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;


    public void validateToken(final String token) {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
    @PostConstruct
    public void debugSecret() {
        System.out.println("🔍 DEBUG JWT SECRET: " + SECRET_KEY);

        // Check if it's literally reading the placeholder
        if (SECRET_KEY == null || SECRET_KEY.contains("${")) {
            System.err.println("❌ ERROR: .env file is NOT being read! jwtSecret is: " + SECRET_KEY);
        }
    }

}
