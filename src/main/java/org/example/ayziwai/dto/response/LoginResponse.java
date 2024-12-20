package org.example.ayziwai.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String type;
    private String login;
    private Collection<String> roles;
} 