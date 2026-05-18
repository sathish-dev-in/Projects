package com.nexabank.auth.dto;

/**
 * Authentication response DTO using Java 17 record.
 * Demonstrates records with static factory methods.
 */
public record AuthResponse(
        String token,
        Long userId,
        String email,
        String role,
        long expiresIn
) {
    /**
     * Factory method for creating a standard 24-hour auth response.
     */
    public static AuthResponse of(String token, Long userId, String email, String role) {
        return new AuthResponse(token, userId, email, role, 86400000L);
    }
}
