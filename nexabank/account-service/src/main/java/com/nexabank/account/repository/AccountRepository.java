package com.nexabank.account.repository;

import com.nexabank.account.entity.Account;
import com.nexabank.account.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Account entity.
 * Demonstrates text blocks in JPQL queries (Java 17).
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

    List<Account> findByUserIdAndStatus(Long userId, AccountStatus status);

    Optional<Account> findByAccountNumber(String accountNumber);

    // Java 17 text block used for complex query
    @Query("""
            SELECT a FROM Account a
            WHERE a.userId = :userId
            AND a.status = 'ACTIVE'
            ORDER BY a.createdAt DESC
            """)
    List<Account> findActiveAccountsByUserId(Long userId);
}
