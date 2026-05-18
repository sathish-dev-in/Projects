package com.nexabank.auth.service;

import com.nexabank.auth.dto.AuthResponse;
import com.nexabank.auth.dto.LoginRequest;
import com.nexabank.auth.dto.RegisterRequest;

/**
 * Auth service interface.
 * Demonstrates abstraction through interface definition.
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    boolean validateToken(String token);
}
