package com.nexabank.auth.exception;

/**
 * Exception for unauthorized access attempts.
 * Final subclass of sealed AppException.
 */
public final class UnauthorizedException extends AppException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }
}
