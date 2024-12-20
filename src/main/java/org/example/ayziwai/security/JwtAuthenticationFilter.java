package org.example.ayziwai.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.example.ayziwai.services.TokenBlacklistService;
import org.example.ayziwai.utils.JWTUtil;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            log.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());
            
            extractAndValidateToken(request)
                    .ifPresent(token -> processToken(token, request, response));
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                log.debug("Current Authentication: Principal={}, Authorities={}", 
                    auth.getPrincipal(), 
                    auth.getAuthorities());
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private void processToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (tokenBlacklistService.isBlacklisted(token)) {
            log.debug("Token is blacklisted");
            return;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = jwtUtil.getRolesFromToken(token);
            
            log.debug("Token processing - Username: {}, Authorities: {}", username, authorities);
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
            );
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication set in SecurityContext - User: {}, Authorities: {}", 
                username, authorities);
        }
    }

    private Optional<String> extractAndValidateToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .filter(jwtUtil::validateToken);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/login") || 
               path.startsWith("/api/auth/register");
    }
} 