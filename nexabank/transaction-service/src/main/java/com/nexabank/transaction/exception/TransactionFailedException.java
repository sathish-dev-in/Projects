package com.nexabank.transaction.exception;

/**
 * Exception for failed transaction processing.
 */
public final class TransactionFailedException extends AppException {

    public TransactionFailedException(String message) {
        super(message, 500);
    }
}
