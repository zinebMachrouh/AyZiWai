package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.dto.UserDTO;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO updateUserRoles(String id, Set<String> roles);
} 