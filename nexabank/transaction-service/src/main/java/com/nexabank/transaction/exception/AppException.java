package com.nexabank.transaction.exception;

/**
 * Sealed exception hierarchy for transaction-service.
 * Demonstrates Java 17 sealed classes.
 */
public sealed class AppException extends RuntimeException
        permits InsufficientFundsException, AccountNotFoundException, TransactionFailedException {

    private final int statusCode;

    protected AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
