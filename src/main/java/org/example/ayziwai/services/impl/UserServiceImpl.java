package org.example.ayziwai.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ayziwai.dto.UserDTO;
import org.example.ayziwai.entities.Role;
import org.example.ayziwai.entities.User;
import org.example.ayziwai.exceptions.DoesNotExistsException;
import org.example.ayziwai.repositories.UserRepository;
import org.example.ayziwai.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUserRoles(String id, Set<String> roles) {
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
        return convertToDTO(userRepository.save(user));
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet()));
        return dto;
    }
} 