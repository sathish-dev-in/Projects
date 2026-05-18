package com.nexabank.auth.config;

import com.nexabank.auth.entity.User;
import com.nexabank.auth.enums.UserRole;
import com.nexabank.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Seeds initial data on application startup.
 * Uses text block for log messages demonstrating Java 17 text blocks.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCustomer();
    }

    private void seedAdmin() {
        if (userRepository.findByEmail("admin@nexabank.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@nexabank.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("NexaBank Administrator");
            admin.setPhone("+1-800-NEXABANK");
            admin.setRole(UserRole.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);

            // Java 17 Text Block for multi-line log message
            String seedMessage = """
                    ╔══════════════════════════════════╗
                    ║   Admin user seeded successfully  ║
                    ║   Email: admin@nexabank.com       ║
                    ║   Password: admin123              ║
                    ╚══════════════════════════════════╝
                    """;
            log.info(seedMessage);
        }
    }

    private void seedCustomer() {
        if (userRepository.findByEmail("john.doe@nexabank.com").isEmpty()) {
            User customer = new User();
            customer.setEmail("john.doe@nexabank.com");
            customer.setPassword(passwordEncoder.encode("user123"));
            customer.setFullName("John Doe");
            customer.setPhone("+1-555-0100");
            customer.setRole(UserRole.CUSTOMER);
            customer.setActive(true);
            customer.setCreatedAt(LocalDateTime.now());
            userRepository.save(customer);
            log.info("Test customer seeded: john.doe@nexabank.com / user123");
        }
    }
}
