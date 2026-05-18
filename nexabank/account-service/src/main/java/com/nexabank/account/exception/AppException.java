package com.nexabank.account.exception;

/**
 * Sealed class hierarchy for exception handling in account-service.
 */
public sealed class AppException extends RuntimeException
        permits InsufficientFundsException, AccountNotFoundException, ValidationException {

    private final int statusCode;

    protected AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
