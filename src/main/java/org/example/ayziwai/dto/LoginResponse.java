package org.example.ayziwai.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String type;
    private String login;
    private Collection<String> roles;
} 