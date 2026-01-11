package org.example.enums;

public enum SessionStatus {
    ACTIVE, COMPLETED, CANCELLED, INTERRUPTED, TERMINATED;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, PAYMENT
    }
}
