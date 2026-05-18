package com.nexabank.transaction.enums;

/**
 * Transaction type enumeration with switch expressions.
 * Demonstrates Java 17 switch expressions returning values.
 */
public enum TransactionType {
    TRANSFER,
    DEPOSIT,
    WITHDRAWAL;

    /**
     * Get human-readable description using Java 17 switch expression.
     */
    public String getDescription() {
        return switch (this) {
            case TRANSFER -> "Inter-account fund transfer";
            case DEPOSIT -> "Account deposit";
            case WITHDRAWAL -> "Account withdrawal";
        };
    }

    /**
     * Determine if the transaction affects two accounts using switch expression.
     */
    public boolean isBilateral() {
        return switch (this) {
            case TRANSFER -> true;
            case DEPOSIT, WITHDRAWAL -> false;
        };
    }
}
