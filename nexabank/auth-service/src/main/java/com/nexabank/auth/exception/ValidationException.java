package com.nexabank.auth.exception;

/**
 * Exception for validation failures.
 * Final subclass of sealed AppException.
 */
public final class ValidationException extends AppException {

    public ValidationException(String message) {
        super(message, 400);
    }
}
