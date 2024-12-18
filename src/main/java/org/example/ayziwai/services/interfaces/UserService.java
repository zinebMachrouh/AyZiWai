package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.dto.response.UserResponse;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse updateUserRoles(String id, Set<String> roles);
} 