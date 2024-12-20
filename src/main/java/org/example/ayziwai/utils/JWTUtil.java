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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTUtil {
    
    @Value("${jwt.access-expiration}")
    private long accessExpiration;  // Short-lived: 5 minutes
    
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration; // Long-lived: 24 hours
    
    private final Map<String, SecretKey> userSecretKeys = new ConcurrentHashMap<>();

    public String generateAccessToken(User user) {
        return generateToken(user, accessExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshExpiration);
    }

    private String generateToken(User user, long expTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expTime);

        // Get or create a secret key for this user
        SecretKey userKey = userSecretKeys.computeIfAbsent(user.getLogin(), 
            k -> Keys.hmacShaKeyFor(generateSecretKey().getBytes()));

        return Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("roles", user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList()))
                .signWith(userKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public Collection<? extends GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        try {
            String username = extractUsernameFromToken(token);
            SecretKey userKey = userSecretKeys.get(username);
            if (userKey == null) {
                return false;
            }

            Jwts.parserBuilder()
                .setSigningKey(userKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Token is expired but might be valid for refresh
            return false;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
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
            Claims claims = Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(parts[0] + "." + parts[1] + ".")
                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract username from token", e);
        }
    }

    private static String generateSecretKey() {
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public void invalidateUserTokens(String username) {
        userSecretKeys.remove(username);
    }
}
