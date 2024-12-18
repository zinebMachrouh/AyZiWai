package org.example.ayziwai.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ayziwai.dto.response.UserResponse;
import org.example.ayziwai.entities.Role;
import org.example.ayziwai.entities.User;
import org.example.ayziwai.exceptions.DoesNotExistsException;
import org.example.ayziwai.repositories.UserRepository;
import org.example.ayziwai.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUserRoles(String id, Set<String> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistsException("User not found"));

        Set<Role> newRoles = roles.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName("ROLE_" + roleName.toUpperCase());
                    return role;
                })
                .collect(Collectors.toSet());

        user.setRoles(newRoles);
        return convertToResponse(userRepository.save(user));
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .active(true)
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .collect(Collectors.toSet()))
                .build();
    }
} 