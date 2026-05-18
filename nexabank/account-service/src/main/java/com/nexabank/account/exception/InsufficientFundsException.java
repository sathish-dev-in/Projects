package com.nexabank.account.exception;

/**
 * Exception for insufficient balance in transfer operations.
 * Final subclass of sealed AppException.
 */
public final class InsufficientFundsException extends AppException {

    public InsufficientFundsException(String message) {
        super(message, 400);
    }
}
