package com.nexabank.account.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for account-service.
 * Demonstrates Java 17 pattern matching with sealed class switch expression.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(AppException ex) {
        // Pattern matching with exhaustive switch — no default needed for sealed hierarchy
        String errorType = switch (ex) {
            case InsufficientFundsException e -> "INSUFFICIENT_FUNDS";
            case AccountNotFoundException e -> "ACCOUNT_NOT_FOUND";
            case ValidationException e -> "VALIDATION_ERROR";
        };

        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of(
                        "error", errorType,
                        "message", ex.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value"
                ));
        return ResponseEntity.badRequest()
                .body(Map.of("error", "VALIDATION_FAILED", "fields", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(Map.of(
                        "error", "INTERNAL_ERROR",
                        "message", "An unexpected error occurred",
                        "timestamp", LocalDateTime.now().toString()
                ));
    }
}
