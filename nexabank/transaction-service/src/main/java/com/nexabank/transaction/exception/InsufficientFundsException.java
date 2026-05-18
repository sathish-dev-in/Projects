package com.nexabank.transaction.exception;

/**
 * Exception for insufficient funds during transfer.
 */
public final class InsufficientFundsException extends AppException {

    public InsufficientFundsException(String message) {
        super(message, 400);
    }
}
