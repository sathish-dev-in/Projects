package com.nexabank.transaction.exception;

/**
 * Exception for account not found in transaction context.
 */
public final class AccountNotFoundException extends AppException {

    public AccountNotFoundException(String message) {
        super(message, 404);
    }
}
