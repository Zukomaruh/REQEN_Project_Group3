package enums;

public enum SessionStatus {
    ACTIVE, COMPLETED, CANCELLED, INTERRUPTED;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, PAYMENT
    }

    public enum UserRole {
        OWNER,CUSTOMER
    }
}
