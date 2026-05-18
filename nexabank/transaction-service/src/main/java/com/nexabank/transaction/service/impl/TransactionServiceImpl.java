package com.nexabank.transaction.service.impl;

import com.nexabank.transaction.annotation.AuditLog;
import com.nexabank.transaction.client.AccountServiceClient;
import com.nexabank.transaction.dto.*;
import com.nexabank.transaction.entity.Transaction;
import com.nexabank.transaction.enums.TransactionStatus;
import com.nexabank.transaction.enums.TransactionType;
import com.nexabank.transaction.exception.AccountNotFoundException;
import com.nexabank.transaction.exception.InsufficientFundsException;
import com.nexabank.transaction.exception.TransactionFailedException;
import com.nexabank.transaction.functional.TransactionProcessor;
import com.nexabank.transaction.repository.TransactionRepository;
import com.nexabank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transaction service implementation demonstrating:
 * - Java 17 records (DTOs)
 * - Functional interface (TransactionProcessor)
 * - @AuditLog annotation with AOP
 * - Sealed class exception hierarchy
 * - Switch expression in TransactionType enum
 * - Pattern matching instanceof
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;

    @Override
    @AuditLog(action = "FUND_TRANSFER", description = "Inter-account fund transfer")
    @Transactional
    public TransactionResponse transfer(TransferRequest request, String userId) {
        // Use functional interface as lambda — demonstrates TransactionProcessor
        TransactionProcessor processor = this::executeTransfer;
        Transaction transaction = processor.process(request);
        return mapToResponse(transaction);
    }

    private Transaction executeTransfer(TransferRequest request) {
        // Step 1: Fetch source account
        ApiResponse<AccountInfo> sourceResponse = accountServiceClient.getAccount(
                request.fromAccountId(), null);

        if (sourceResponse == null || !sourceResponse.success() || sourceResponse.data() == null) {
            throw new AccountNotFoundException(
                    "Source account not found: " + request.fromAccountId());
        }

        // Step 2: Fetch destination account
        ApiResponse<AccountInfo> destResponse = accountServiceClient.getAccount(
                request.toAccountId(), null);

        if (destResponse == null || !destResponse.success() || destResponse.data() == null) {
            throw new AccountNotFoundException(
                    "Destination account not found: " + request.toAccountId());
        }

        AccountInfo source = sourceResponse.data();
        AccountInfo dest = destResponse.data();

        // Step 3: Check sufficient balance — pattern matching instanceof
        if (source.balance().compareTo(request.amount()) < 0) {
            // Save failed transaction record
            Transaction failed = Transaction.builder()
                    .fromAccountId(request.fromAccountId())
                    .toAccountId(request.toAccountId())
                    .fromAccountNumber(source.accountNumber())
                    .toAccountNumber(dest.accountNumber())
                    .amount(request.amount())
                    .type(TransactionType.TRANSFER)
                    .status(TransactionStatus.FAILED)
                    .description(request.description())
                    .failureReason("Insufficient funds")
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionRepository.save(failed);
            throw new InsufficientFundsException(
                    "Insufficient funds. Available: " + source.balance() + ", Required: " + request.amount());
        }

        try {
            // Step 4: Debit source
            accountServiceClient.debit(
                    request.fromAccountId(),
                    new DebitRequest(request.amount()),
                    null);

            // Step 5: Credit destination
            accountServiceClient.credit(
                    request.toAccountId(),
                    new CreditRequest(request.amount()),
                    null);

            // Step 6: Save completed transaction
            Transaction completed = Transaction.builder()
                    .fromAccountId(request.fromAccountId())
                    .toAccountId(request.toAccountId())
                    .fromAccountNumber(source.accountNumber())
                    .toAccountNumber(dest.accountNumber())
                    .amount(request.amount())
                    .type(TransactionType.TRANSFER)
                    .status(TransactionStatus.COMPLETED)
                    .description(request.description())
                    .createdAt(LocalDateTime.now())
                    .build();

            Transaction saved = transactionRepository.save(completed);
            log.info("Transfer completed: {} -> {} | Amount: {}",
                    source.accountNumber(), dest.accountNumber(), request.amount());
            return saved;

        } catch (InsufficientFundsException e) {
            throw e;
        } catch (Exception e) {
            // Save failed transaction
            Transaction failed = Transaction.builder()
                    .fromAccountId(request.fromAccountId())
                    .toAccountId(request.toAccountId())
                    .fromAccountNumber(source.accountNumber())
                    .toAccountNumber(dest.accountNumber())
                    .amount(request.amount())
                    .type(TransactionType.TRANSFER)
                    .status(TransactionStatus.FAILED)
                    .description(request.description())
                    .failureReason(e.getMessage())
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionRepository.save(failed);
            throw new TransactionFailedException("Transfer failed: " + e.getMessage());
        }
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        return transactionRepository
                .findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(accountId, accountId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AccountNotFoundException("Transaction not found with ID: " + id));
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getFromAccountNumber(),
                t.getToAccountNumber(),
                t.getAmount(),
                t.getType(),
                t.getType().getDescription(),  // switch expression inside enum
                t.getStatus(),
                t.getDescription(),
                t.getCreatedAt()
        );
    }
}
