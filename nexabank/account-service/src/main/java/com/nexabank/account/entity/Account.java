package com.nexabank.account.entity;

import com.nexabank.account.enums.AccountStatus;
import com.nexabank.account.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Account entity with auto-generated account number.
 */
@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Auto-generates account number before persisting.
     * Format: NB + 10 random digits.
     */
    @PrePersist
    public void generateAccountNumber() {
        if (this.accountNumber == null) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder("NB");
            for (int i = 0; i < 10; i++) {
                sb.append(random.nextInt(10));
            }
            this.accountNumber = sb.toString();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = AccountStatus.ACTIVE;
        }
    }
}
