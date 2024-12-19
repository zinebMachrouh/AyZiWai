package org.example.ayziwai.services;

import org.example.ayziwai.entities.User;
import org.example.ayziwai.utils.JWTUtil;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JWTUtil jwtUtil;
    
    public String refreshToken(String oldToken) {
        if (!jwtUtil.isTokenExpiringSoon(oldToken)) {
            return oldToken;
        }
        
        String username = jwtUtil.getUsernameFromToken(oldToken);
        User user = jwtUtil.getUserFromToken(oldToken);
        return jwtUtil.generateToken(user);
    }
} 