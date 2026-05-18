package com.nexabank.transaction.repository;

import com.nexabank.transaction.entity.Transaction;
import com.nexabank.transaction.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Transaction entity with text block queries.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(
            Long fromAccountId, Long toAccountId);

    // Java 17 text block for multi-line JPQL query
    @Query("""
            SELECT t FROM Transaction t
            WHERE (t.fromAccountId = :accountId OR t.toAccountId = :accountId)
            AND t.status = :status
            ORDER BY t.createdAt DESC
            """)
    List<Transaction> findByAccountIdAndStatus(Long accountId, TransactionStatus status);
}
