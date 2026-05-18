package com.nexabank.auth.service.impl;

import com.nexabank.auth.annotation.AuditLog;
import com.nexabank.auth.dto.AuthResponse;
import com.nexabank.auth.dto.LoginRequest;
import com.nexabank.auth.dto.RegisterRequest;
import com.nexabank.auth.entity.User;
import com.nexabank.auth.enums.UserRole;
import com.nexabank.auth.exception.UnauthorizedException;
import com.nexabank.auth.exception.ValidationException;
import com.nexabank.auth.functional.TokenValidator;
import com.nexabank.auth.repository.UserRepository;
import com.nexabank.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Auth service implementation demonstrating:
 * - Java 17 records (DTOs)
 * - Optional for null-safe lookups
 * - Custom @AuditLog annotation with AOP
 * - Sealed class exception hierarchy
 * - Functional interface usage
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    // Functional interface lambda — demonstrates @FunctionalInterface usage
    private TokenValidator notExpiredValidator = token -> {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).getPayload();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    };

    @Override
    @AuditLog(action = "USER_REGISTER", description = "New user registration")
    public AuthResponse register(RegisterRequest request) {
        // Optional usage for null-safe email check
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ValidationException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setRole(UserRole.CUSTOMER);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        log.info("New user registered: {} with role {}", saved.getEmail(), saved.getRole());

        return AuthResponse.of(generateToken(saved), saved.getId(), saved.getEmail(), saved.getRole().name());
    }

    @Override
    @AuditLog(action = "USER_LOGIN", description = "User login attempt")
    public AuthResponse login(LoginRequest request) {
        // Optional.orElseThrow for null-safe lookup
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.isActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        log.info("User logged in: {}", user.getEmail());
        return AuthResponse.of(generateToken(user), user.getId(), user.getEmail(), user.getRole().name());
    }

    @Override
    public boolean validateToken(String token) {
        // Using composed functional interface validator
        TokenValidator composedValidator = notExpiredValidator;
        return composedValidator.validate(token);
    }

    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }
}
