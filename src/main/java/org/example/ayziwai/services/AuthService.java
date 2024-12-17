package org.example.ayziwai.services;

import org.example.ayziwai.dto.LoginRequest;
import org.example.ayziwai.dto.LoginResponse;
import org.example.ayziwai.dto.UserDTO;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    UserDTO register(UserDTO userDTO);
} 