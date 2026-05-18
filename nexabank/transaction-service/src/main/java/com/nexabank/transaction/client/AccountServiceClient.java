package com.nexabank.transaction.client;

import com.nexabank.transaction.dto.AccountInfo;
import com.nexabank.transaction.dto.ApiResponse;
import com.nexabank.transaction.dto.CreditRequest;
import com.nexabank.transaction.dto.DebitRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign client for inter-service communication with account-service.
 * Demonstrates Spring Cloud OpenFeign for declarative REST clients.
 */
@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @GetMapping("/api/accounts/{id}")
    ApiResponse<AccountInfo> getAccount(
            @PathVariable("id") Long id,
            @RequestHeader(value = "X-User-Id", required = false) String userId);

    @PostMapping("/api/accounts/{id}/debit")
    ApiResponse<AccountInfo> debit(
            @PathVariable("id") Long id,
            @RequestBody DebitRequest req,
            @RequestHeader(value = "X-User-Id", required = false) String userId);

    @PostMapping("/api/accounts/{id}/credit")
    ApiResponse<AccountInfo> credit(
            @PathVariable("id") Long id,
            @RequestBody CreditRequest req,
            @RequestHeader(value = "X-User-Id", required = false) String userId);
}
