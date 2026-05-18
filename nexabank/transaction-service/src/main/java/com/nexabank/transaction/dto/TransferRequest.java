package com.nexabank.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Transfer request DTO using Java 17 record.
 * Immutable, concise, with built-in equals/hashCode/toString.
 */
public record TransferRequest(
        @NotNull(message = "Source account ID is required")
        Long fromAccountId,

        @NotNull(message = "Destination account ID is required")
        Long toAccountId,

        @NotNull(message = "Transfer amount is required")
        @DecimalMin(value = "0.01", message = "Transfer amount must be greater than 0")
        BigDecimal amount,

        String description
) {}
