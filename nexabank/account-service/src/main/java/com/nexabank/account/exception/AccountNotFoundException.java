package com.nexabank.account.exception;

/**
 * Exception for account not found scenarios.
 * Final subclass of sealed AppException.
 */
public final class AccountNotFoundException extends AppException {

    public AccountNotFoundException(String message) {
        super(message, 404);
    }
}
