package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.dto.UserDTO;
import org.example.ayziwai.dto.request.LoginRequest;
import org.example.ayziwai.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    UserDTO register(UserDTO userDTO);
} 