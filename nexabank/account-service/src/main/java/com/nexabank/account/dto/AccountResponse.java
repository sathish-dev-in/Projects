package com.nexabank.account.dto;

import com.nexabank.account.enums.AccountStatus;
import com.nexabank.account.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for account data.
 * Java 17 record for immutable data transfer.
 */
public record AccountResponse(
        Long id,
        String accountNumber,
        Long userId,
        BigDecimal balance,
        AccountType accountType,
        String accountTypeDescription,
        double interestRate,
        AccountStatus status,
        LocalDateTime createdAt
) {}
