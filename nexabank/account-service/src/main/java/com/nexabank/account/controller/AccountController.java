package com.nexabank.account.controller;

import com.nexabank.account.dto.AccountResponse;
import com.nexabank.account.dto.ApiResponse;
import com.nexabank.account.dto.CreateAccountRequest;
import com.nexabank.account.dto.DebitCreditRequest;
import com.nexabank.account.dto.DepositRequest;
import com.nexabank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for account management.
 * Reads user identity from X-User-Id header set by the API Gateway.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(Long.parseLong(userId), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Account created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getMyAccounts(
            @RequestHeader("X-User-Id") String userId) {
        List<AccountResponse> accounts = accountService.getAccountsByUserId(Long.parseLong(userId));
        return ResponseEntity.ok(ApiResponse.ok("Accounts retrieved successfully", accounts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.ok("Account retrieved", account));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<ApiResponse<AccountResponse>> deposit(
            @PathVariable Long id,
            @Valid @RequestBody DepositRequest request,
            @RequestHeader("X-User-Id") String userId) {
        AccountResponse response = accountService.deposit(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Deposit successful", response));
    }

    // Internal endpoint called by transaction-service via Feign
    @PostMapping("/{id}/debit")
    public ResponseEntity<ApiResponse<AccountResponse>> debit(
            @PathVariable Long id,
            @Valid @RequestBody DebitCreditRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        AccountResponse response = accountService.debit(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Debit successful", response));
    }

    // Internal endpoint called by transaction-service via Feign
    @PostMapping("/{id}/credit")
    public ResponseEntity<ApiResponse<AccountResponse>> credit(
            @PathVariable Long id,
            @Valid @RequestBody DebitCreditRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        AccountResponse response = accountService.credit(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Credit successful", response));
    }
}
