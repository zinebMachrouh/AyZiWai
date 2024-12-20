package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.dto.request.LoginRequest;
import org.example.ayziwai.dto.request.UserRequest;
import org.example.ayziwai.dto.response.LoginResponse;
import org.example.ayziwai.dto.response.UserResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    UserResponse register(UserRequest userRequest);
    void logout(String token);
} 