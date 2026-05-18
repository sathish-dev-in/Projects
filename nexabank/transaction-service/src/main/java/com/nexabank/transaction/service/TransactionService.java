package com.nexabank.transaction.service;

import com.nexabank.transaction.dto.ApiResponse;
import com.nexabank.transaction.dto.TransactionResponse;
import com.nexabank.transaction.dto.TransferRequest;

import java.util.List;

/**
 * Transaction service interface.
 */
public interface TransactionService {

    TransactionResponse transfer(TransferRequest request, String userId);

    List<TransactionResponse> getTransactionsByAccountId(Long accountId);

    TransactionResponse getTransactionById(Long id);
}
