package com.autonest.exception;

/**
 * Thrown when a business rule or domain constraint is violated.
 * Part of the sealed AppException hierarchy.
 */
public final class BusinessException extends AppException {

    public BusinessException(String message) {
        super(message, 400);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, 400, cause);
    }

    public static BusinessException invalidTransition(String from, String to) {
        return new BusinessException("Cannot transition service status from " + from + " to " + to);
    }

    public static BusinessException insufficientStock(String partName, int requested, int available) {
        return new BusinessException("Insufficient stock for part '" + partName + "': requested " + requested + ", available " + available);
    }
}
