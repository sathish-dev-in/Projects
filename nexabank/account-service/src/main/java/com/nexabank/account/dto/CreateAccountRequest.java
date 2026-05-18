package com.nexabank.account.dto;

import com.nexabank.account.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new bank account.
 * Java 17 record — immutable and concise.
 */
public record CreateAccountRequest(
        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Initial deposit amount is required")
        @DecimalMin(value = "0.00", message = "Initial deposit cannot be negative")
        BigDecimal initialDeposit
) {}
