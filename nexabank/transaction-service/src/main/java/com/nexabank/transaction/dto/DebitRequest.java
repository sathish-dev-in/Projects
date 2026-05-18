package com.nexabank.transaction.dto;

import java.math.BigDecimal;

/**
 * Debit request DTO for Feign client calls to account-service.
 */
public record DebitRequest(BigDecimal amount) {}
