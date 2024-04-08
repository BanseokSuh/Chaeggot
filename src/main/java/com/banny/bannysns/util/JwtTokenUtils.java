package com.banny.bannysns.util;

import com.banny.bannysns.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {
    public static String generateToken(User user, String key, Long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userIdx", user.getId());
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUserName());
        claims.put("userRole", user.getUserRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}
