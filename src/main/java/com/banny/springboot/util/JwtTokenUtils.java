package com.banny.springboot.util;

import com.banny.springboot.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    /**
     * Generate token
     *
     * @param user
     * @param key
     * @param expiredTimeMs
     * @return Token
     */
    public static String generateToken(User user, String key, Long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUserName());
        claims.put("userRole", user.getUserRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSigningKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get signing key
     *
     * @param key
     * @return Key
     */
    private static Key getSigningKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}
