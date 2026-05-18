package com.autonest.exception;

/**
 * Sealed exception hierarchy root.
 * Only ResourceNotFoundException and BusinessException are permitted subclasses.
 */
public sealed class AppException extends RuntimeException
        permits ResourceNotFoundException, BusinessException {

    private final int statusCode;

    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AppException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
