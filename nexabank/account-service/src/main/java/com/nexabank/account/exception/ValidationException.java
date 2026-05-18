package com.nexabank.account.exception;

/**
 * Exception for validation failures in account-service.
 * Final subclass of sealed AppException.
 */
public final class ValidationException extends AppException {

    public ValidationException(String message) {
        super(message, 400);
    }
}
