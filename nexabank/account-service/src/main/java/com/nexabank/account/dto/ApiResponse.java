package com.nexabank.account.dto;

/**
 * Generic API response wrapper using Java 17 record with generics.
 * Demonstrates Generics + Records combination.
 *
 * @param <T> the type of data payload
 */
public record ApiResponse<T>(boolean success, String message, T data) {

    /**
     * Creates a successful response with data.
     */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Creates an error response without data.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    /**
     * Creates a successful response without data payload.
     */
    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(true, message, null);
    }
}
