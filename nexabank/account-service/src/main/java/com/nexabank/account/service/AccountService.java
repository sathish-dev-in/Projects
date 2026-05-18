package com.nexabank.account.service;

import com.nexabank.account.dto.AccountResponse;
import com.nexabank.account.dto.CreateAccountRequest;
import com.nexabank.account.dto.DebitCreditRequest;
import com.nexabank.account.dto.DepositRequest;
import com.nexabank.account.entity.Account;

import java.util.List;

/**
 * Account service interface with generics.
 * Demonstrates generic service pattern: BaseService<T, ID>.
 */
public interface AccountService extends BaseService<Account, Long> {

    AccountResponse createAccount(Long userId, CreateAccountRequest request);

    List<AccountResponse> getAccountsByUserId(Long userId);

    AccountResponse getAccountById(Long accountId);

    AccountResponse deposit(Long accountId, DepositRequest request);

    AccountResponse debit(Long accountId, DebitCreditRequest request);

    AccountResponse credit(Long accountId, DebitCreditRequest request);
}
