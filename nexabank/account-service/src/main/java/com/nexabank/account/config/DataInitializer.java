package com.nexabank.account.config;

import com.nexabank.account.entity.Account;
import com.nexabank.account.enums.AccountStatus;
import com.nexabank.account.enums.AccountType;
import com.nexabank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Seeds initial account data for demo/testing.
 * Uses text blocks for log messages (Java 17).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        if (accountRepository.count() == 0) {
            // Seed accounts for the test customer (userId=2 from auth-service seed)
            Account savings = new Account();
            savings.setUserId(2L);
            savings.setAccountType(AccountType.SAVINGS);
            savings.setBalance(new BigDecimal("10000.00"));
            savings.setStatus(AccountStatus.ACTIVE);
            savings.setCreatedAt(LocalDateTime.now());
            accountRepository.save(savings);

            Account current = new Account();
            current.setUserId(2L);
            current.setAccountType(AccountType.CURRENT);
            current.setBalance(new BigDecimal("5000.00"));
            current.setStatus(AccountStatus.ACTIVE);
            current.setCreatedAt(LocalDateTime.now());
            accountRepository.save(current);

            String seedMessage = """
                    ╔══════════════════════════════════╗
                    ║  Demo accounts seeded             ║
                    ║  User #2: Savings + Current       ║
                    ╚══════════════════════════════════╝
                    """;
            log.info(seedMessage);
        }
    }
}
