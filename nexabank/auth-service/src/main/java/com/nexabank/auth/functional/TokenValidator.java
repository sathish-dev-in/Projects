package com.nexabank.auth.functional;

/**
 * Functional interface for token validation.
 * Demonstrates Java 17 functional interface with custom @FunctionalInterface annotation.
 */
@FunctionalInterface
public interface TokenValidator {

    /**
     * Validates a JWT token.
     *
     * @param token the JWT token string to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validate(String token);

    /**
     * Default method for creating a combined validator using short-circuit AND.
     * Demonstrates functional composition with default methods.
     */
    default TokenValidator and(TokenValidator other) {
        return token -> this.validate(token) && other.validate(token);
    }
}
