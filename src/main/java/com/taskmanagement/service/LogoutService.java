package com.taskmanagement.service;

import com.taskmanagement.entity.RevokedToken;
import com.taskmanagement.repository.RevokedTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final RevokedTokenRepository revokedTokenRepository;

    public void logout(String token) {

        if (token == null || token.isBlank()) {
            throw new RuntimeException(
                    "JWT token is required"
            );
        }

        if (!revokedTokenRepository
                .existsByToken(token)) {

            RevokedToken revokedToken =
                    new RevokedToken(token);

            revokedTokenRepository.save(
                    revokedToken
            );
        }
    }

    public boolean isTokenRevoked(String token) {

        return revokedTokenRepository
                .existsByToken(token);
    }
}