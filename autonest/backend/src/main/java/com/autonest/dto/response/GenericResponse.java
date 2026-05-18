package com.autonest.dto.response;

import java.time.LocalDateTime;

/**
 * Generic response wrapper record.
 * Demonstrates Java Generics with a Java Record.
 *
 * @param success   whether the operation was successful
 * @param message   human-readable message
 * @param data      the payload (generic type T)
 * @param timestamp when the response was generated
 */
public record GenericResponse<T>(
        boolean success,
        String message,
        T data,
        LocalDateTime timestamp
) {

    public static <T> GenericResponse<T> success(String message, T data) {
        return new GenericResponse<>(true, message, data, LocalDateTime.now());
    }

    public static <T> GenericResponse<T> success(T data) {
        return new GenericResponse<>(true, "Operation completed successfully", data, LocalDateTime.now());
    }

    public static <T> GenericResponse<T> of(boolean success, String message, T data) {
        return new GenericResponse<>(success, message, data, LocalDateTime.now());
    }

    public static GenericResponse<Void> error(String message) {
        return new GenericResponse<>(false, message, null, LocalDateTime.now());
    }

    public static GenericResponse<Void> successMessage(String message) {
        return new GenericResponse<>(true, message, null, LocalDateTime.now());
    }
}
