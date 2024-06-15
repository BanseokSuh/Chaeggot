package com.banny.chaeggot.util;

import com.banny.chaeggot.entity.User;
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
     * @param loginId
     * @param key
     * @return Boolean
     */
    public static Boolean isTokenValid(String token, String loginId, String key) {
        String userIdByToken = getLoginId(token, key);
        return userIdByToken.equals(loginId) && !isTokenExpired(token, key);
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
     * Get loginId from token
     * - parseClaimsJws() method is used to parse the token.
     *
     * @param token
     * @param key
     * @return loginId
     */
    public static String getLoginId(String token, String key) {
        return extractAllClaims(token, key).get("loginId", String.class);
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
        claims.put("userId", user.getId());
        claims.put("loginId", user.getLoginId());
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
