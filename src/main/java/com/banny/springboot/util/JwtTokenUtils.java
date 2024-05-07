package com.banny.springboot.util;

import com.banny.springboot.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    /**
     * Check if token is valid
     *
     * @param token
     * @param userId
     * @param key
     * @return Boolean
     */
    public static Boolean isTokenValid(String token, String userId, String key) {
        String userIdByToken = getUserId(token, key);
        return userIdByToken.equals(userId) && !isTokenExpired(token, key);
    }

    /**
     * Check if token is expired
     *
     * @param token
     * @param key
     * @return Boolean
     */
    public static Boolean isTokenExpired(String token, String key) {
        Date expiration = extractAllClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Get userId from token
     * - parseClaimsJws() method is used to parse the token.
     *
     * @param token
     * @param key
     * @return userId
     */
    public static String getUserId(String token, String key) {
        return extractAllClaims(token, key).get("userId", String.class);
    }

    /**
     * Get all claims from token
     *
     * @param token
     * @param key
     * @return Claims
     */
    public static Claims extractAllClaims(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

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
        claims.put("userIdx", user.getUserIdx());
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUsername());
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
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
