package com.nexabank.transaction.dto;

import java.math.BigDecimal;

/**
 * Minimal account info used by Feign client response parsing.
 */
public record AccountInfo(
        Long id,
        String accountNumber,
        Long userId,
        BigDecimal balance,
        String accountType,
        String status
) {}
