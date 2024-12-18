package org.example.ayziwai.services.implementation;

import lombok.RequiredArgsConstructor;

import org.example.ayziwai.dto.UserDTO;
import org.example.ayziwai.dto.request.LoginRequest;
import org.example.ayziwai.dto.response.LoginResponse;
import org.example.ayziwai.entities.Role;
import org.example.ayziwai.entities.User;
import org.example.ayziwai.exceptions.AlreadyExistsException;
import org.example.ayziwai.repositories.UserRepository;
import org.example.ayziwai.services.interfaces.AuthService;
import org.example.ayziwai.utils.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByLogin(userDTO.getLogin())) {
            throw new AlreadyExistsException("User with login " + userDTO.getLogin() + " already exists");
        }

        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setActive(true);
        
        Set<Role> roles = userDTO.getRoles().stream()
            .map(roleName -> {
                Role role = new Role();
                role.setName("ROLE_" + roleName.toUpperCase());
                return role;
            })
            .collect(Collectors.toSet());
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        userDTO.setId(savedUser.getId());
        userDTO.setPassword(null);
        return userDTO;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
            .token(token)
            .type("Bearer")
            .login(user.getLogin())
            .roles(user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet()))
            .build();
    }
} 