package org.example.ayziwai.services.implementation;

import java.util.Set;
import java.util.stream.Collectors;

import org.example.ayziwai.dto.request.LoginRequest;
import org.example.ayziwai.dto.request.UserRequest;
import org.example.ayziwai.dto.response.LoginResponse;
import org.example.ayziwai.dto.response.UserResponse;
import org.example.ayziwai.entities.Role;
import org.example.ayziwai.entities.User;
import org.example.ayziwai.exceptions.AlreadyExistsException;
import org.example.ayziwai.repositories.UserRepository;
import org.example.ayziwai.services.TokenBlacklistService;
import org.example.ayziwai.services.interfaces.AuthService;
import org.example.ayziwai.utils.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.existsByLogin(userRequest.getLogin())) {
            throw new AlreadyExistsException("User with login " + userRequest.getLogin() + " already exists");
        }

        User user = new User();
        user.setLogin(userRequest.getLogin());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setActive(true);
        
        Set<Role> roles = userRequest.getRoles().stream()
            .map(roleName -> {
                Role role = new Role();
                role.setName("ROLE_" + roleName.toUpperCase());
                return role;
            })
            .collect(Collectors.toSet());
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );

        User user = userRepository.findByLogin(loginRequest.getLogin())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .type("Bearer")
            .login(user.getLogin())
            .roles(user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet()))
            .build();
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.blacklistToken(token);
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .active(user.isActive())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .collect(Collectors.toSet()))
                .build();
    }
} 