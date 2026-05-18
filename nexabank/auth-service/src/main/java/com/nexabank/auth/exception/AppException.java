package com.nexabank.auth.exception;

/**
 * Sealed class hierarchy for exception handling.
 * Demonstrates Java 17 sealed classes with permits clause.
 * All subclasses must be final, non-sealed, or sealed themselves.
 */
public sealed class AppException extends RuntimeException
        permits UnauthorizedException, ResourceNotFoundException, ValidationException {

    private final int statusCode;

    protected AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
