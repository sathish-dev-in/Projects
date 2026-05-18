package com.nexabank.account.service.impl;

import com.nexabank.account.annotation.AuditLog;
import com.nexabank.account.dto.AccountResponse;
import com.nexabank.account.dto.CreateAccountRequest;
import com.nexabank.account.dto.DebitCreditRequest;
import com.nexabank.account.dto.DepositRequest;
import com.nexabank.account.entity.Account;
import com.nexabank.account.enums.AccountStatus;
import com.nexabank.account.exception.AccountNotFoundException;
import com.nexabank.account.exception.InsufficientFundsException;
import com.nexabank.account.exception.ValidationException;
import com.nexabank.account.repository.AccountRepository;
import com.nexabank.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Account service implementation demonstrating:
 * - Java 17 records (AccountResponse)
 * - Switch expressions (via AccountType enum)
 * - Optional for null-safe lookups
 * - @AuditLog custom annotation with AOP
 * - Sealed exception hierarchy
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @AuditLog(action = "CREATE_ACCOUNT", description = "Creating new bank account")
    @Transactional
    public AccountResponse createAccount(Long userId, CreateAccountRequest request) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountType(request.accountType());
        account.setBalance(request.initialDeposit() != null ? request.initialDeposit() : BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());

        Account saved = accountRepository.save(account);
        log.info("Account created: {} for userId: {}", saved.getAccountNumber(), userId);
        return mapToResponse(saved);
    }

    @Override
    public List<AccountResponse> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
    }

    @Override
    @AuditLog(action = "DEPOSIT", description = "Deposit funds to account")
    @Transactional
    public AccountResponse deposit(Long accountId, DepositRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new ValidationException("Account is not active");
        }

        account.setBalance(account.getBalance().add(request.amount()));
        Account updated = accountRepository.save(account);
        log.info("Deposit of {} to account {}", request.amount(), account.getAccountNumber());
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public AccountResponse debit(Long accountId, DebitCreditRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new ValidationException("Account is not active");
        }

        if (account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Available: " + account.getBalance() + ", Required: " + request.amount());
        }

        account.setBalance(account.getBalance().subtract(request.amount()));
        return mapToResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponse credit(Long accountId, DebitCreditRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new ValidationException("Account is not active");
        }

        account.setBalance(account.getBalance().add(request.amount()));
        return mapToResponse(accountRepository.save(account));
    }

    // BaseService generic methods
    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account save(Account entity) {
        return accountRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    private AccountResponse mapToResponse(Account account) {
        // Uses switch expression from AccountType enum
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getUserId(),
                account.getBalance(),
                account.getAccountType(),
                account.getAccountType().getDescription(),    // switch expression inside enum
                account.getAccountType().getInterestRate(),   // switch expression inside enum
                account.getStatus(),
                account.getCreatedAt()
        );
    }
}
