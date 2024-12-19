package org.example.ayziwai.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    private final ConcurrentMap<String, Boolean> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklistToken(String token) {
        blacklistedTokens.put(token, true);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.getOrDefault(token, false);
    }
} 