package com.nexabank.transaction.dto;

import java.math.BigDecimal;

/**
 * Credit request DTO for Feign client calls to account-service.
 */
public record CreditRequest(BigDecimal amount) {}
