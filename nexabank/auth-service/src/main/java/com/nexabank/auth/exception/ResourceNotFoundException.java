package com.nexabank.auth.exception;

/**
 * Exception for resource not found scenarios.
 * Final subclass of sealed AppException.
 */
public final class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
