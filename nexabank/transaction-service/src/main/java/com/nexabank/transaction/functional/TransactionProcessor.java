package com.nexabank.transaction.functional;

import com.nexabank.transaction.dto.TransferRequest;
import com.nexabank.transaction.entity.Transaction;

/**
 * Functional interface for processing transactions.
 * Demonstrates Java 17 @FunctionalInterface usage.
 */
@FunctionalInterface
public interface TransactionProcessor {

    /**
     * Process a transfer request and return the resulting transaction.
     *
     * @param request the transfer request
     * @return the created Transaction entity
     */
    Transaction process(TransferRequest request);
}
