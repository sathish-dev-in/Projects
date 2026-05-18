package com.nexabank.transaction.dto;

/**
 * Generic API response wrapper for transaction-service.
 *
 * @param <T> the response data type
 */
public record ApiResponse<T>(boolean success, String message, T data) {

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
