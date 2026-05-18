package com.nexabank.account.enums;

/**
 * Account type enumeration with switch expressions.
 * Demonstrates Java 17 switch expressions returning values.
 */
public enum AccountType {
    SAVINGS,
    CURRENT,
    FIXED_DEPOSIT;

    /**
     * Get interest rate using Java 17 switch expression.
     * Exhaustive — no default needed because all cases are covered.
     */
    public double getInterestRate() {
        return switch (this) {
            case SAVINGS -> 3.5;
            case CURRENT -> 0.0;
            case FIXED_DEPOSIT -> 6.5;
        };
    }

    /**
     * Get human-readable description using switch expression.
     */
    public String getDescription() {
        return switch (this) {
            case SAVINGS -> "Regular savings account with 3.5% p.a. interest";
            case CURRENT -> "Current account for business transactions";
            case FIXED_DEPOSIT -> "Fixed deposit with 6.5% p.a. interest";
        };
    }
}
