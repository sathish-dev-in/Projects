package com.nexabank.auth.repository;

import com.nexabank.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity with Optional-based lookups.
 * Demonstrates Java 17 Optional usage for null-safe data access.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email — returns Optional for null-safe lookups.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by email.
     */
    boolean existsByEmail(String email);
}
