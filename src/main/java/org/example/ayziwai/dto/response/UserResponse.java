package org.example.ayziwai.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private String id;
    private String login;
    private boolean active;
    private Set<String> roles;
} 