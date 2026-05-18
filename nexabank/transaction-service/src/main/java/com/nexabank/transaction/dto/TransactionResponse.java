package com.nexabank.transaction.dto;

import com.nexabank.transaction.enums.TransactionStatus;
import com.nexabank.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction response DTO using Java 17 record.
 */
public record TransactionResponse(
        Long id,
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        TransactionType type,
        String typeDescription,
        TransactionStatus status,
        String description,
        LocalDateTime createdAt
) {}
