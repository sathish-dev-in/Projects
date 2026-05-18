package com.nexabank.transaction.controller;

import com.nexabank.transaction.dto.ApiResponse;
import com.nexabank.transaction.dto.TransactionResponse;
import com.nexabank.transaction.dto.TransferRequest;
import com.nexabank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for transaction operations.
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader("X-User-Id") String userId) {
        TransactionResponse response = transactionService.transfer(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Transfer completed successfully", response));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByAccount(
            @PathVariable Long accountId,
            @RequestHeader("X-User-Id") String userId) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(ApiResponse.ok("Transactions retrieved", transactions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        TransactionResponse transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.ok("Transaction retrieved", transaction));
    }
}
