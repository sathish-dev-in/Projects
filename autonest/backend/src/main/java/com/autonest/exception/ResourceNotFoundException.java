package com.autonest.exception;

/**
 * Thrown when a requested resource does not exist in the system.
 * Part of the sealed AppException hierarchy.
 */
public final class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found with id: " + id, 404);
    }

    public ResourceNotFoundException(String resourceName, String field, Object value) {
        super(resourceName + " not found with " + field + ": " + value, 404);
    }

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
