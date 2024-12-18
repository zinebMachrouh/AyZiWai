package org.example.ayziwai.controllers;

import lombok.RequiredArgsConstructor;

import org.example.ayziwai.dto.UserDTO;
import org.example.ayziwai.dto.request.LoginRequest;
import org.example.ayziwai.dto.response.LoginResponse;
import org.example.ayziwai.services.interfaces.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.register(userDTO));
    }
}
