package org.example.enums;

public enum SessionStatus {
    ACTIVE, COMPLETED, CANCELLED, INTERRUPTED;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, PAYMENT
    }
}
