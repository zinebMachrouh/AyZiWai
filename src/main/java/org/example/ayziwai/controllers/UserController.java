package org.example.ayziwai.controllers;

import java.util.List;
import java.util.Set;

import org.example.ayziwai.dto.response.UserResponse;
import org.example.ayziwai.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserResponse> updateUserRoles(
            @PathVariable String id,
            @RequestBody Set<String> roles) {
        return ResponseEntity.ok(userService.updateUserRoles(id, roles));
    }
} 