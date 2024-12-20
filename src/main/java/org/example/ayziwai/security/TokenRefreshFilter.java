package org.example.ayziwai.security;

import java.io.IOException;

import org.example.ayziwai.entities.User;
import org.example.ayziwai.repositories.UserRepository;
import org.example.ayziwai.utils.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenRefreshFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                String username = jwtUtil.getUsernameFromToken(token);
                
                // If token is expired, try to refresh it
                if (!jwtUtil.validateToken(token)) {
                    User user = userRepository.findByLogin(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    
                    String newToken = jwtUtil.generateAccessToken(user);
                    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
                }
            } catch (Exception e) {
                log.error("Token refresh failed: {}", e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
} 