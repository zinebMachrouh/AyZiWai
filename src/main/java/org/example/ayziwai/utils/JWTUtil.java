package org.example.ayziwai.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.example.ayziwai.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTUtil {
    
    private static final long expiration = 1000 * 60 * 5;
    private final Map<String, SecretKey> userSecretKeys = new ConcurrentHashMap<>();

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        try {
            // Get or create a secret key for this user
            SecretKey userKey = userSecretKeys.computeIfAbsent(user.getLogin(), 
                k -> Keys.hmacShaKeyFor(generateSecretKey().getBytes()));

            return Jwts.builder()
                    .setSubject(user.getLogin())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .setIssuer("AyZiWai")
                    .claim("roles", user.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toList()))
                    .signWith(userKey)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating JWT token", e);
            throw new RuntimeException("Could not generate token", e);
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }



    public Collection<? extends GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Claims getClaimsFromToken(String token) {
        String username = extractUsernameFromToken(token);
        SecretKey userKey = userSecretKeys.get(username);
        if (userKey == null) {
            throw new IllegalArgumentException("No secret key found for user");
        }

        return Jwts.parserBuilder()
                .setSigningKey(userKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractUsernameFromToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }
        
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(payload);
            return jsonNode.get("sub").asText();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract username from token", e);
        }
    }

   private static String generateSecretKey() {
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean isTokenExpiringSoon(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            // Check if token will expire in next 30 seconds
            return expiration.getTime() - System.currentTimeMillis() <= 30000;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public User getUserFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        User user = new User();
        user.setLogin(claims.getSubject());
        // Set other user properties as needed
        return user;
    }
}
