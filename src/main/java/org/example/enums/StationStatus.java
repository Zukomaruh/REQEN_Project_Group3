package org.example.enums;

public enum StationStatus {
    AVAILABLE, CHARGING, MAINTENANCE, OFFLINE;

    public enum TransactionStatus {
        PENDING, PAID, OVERDUE, CANCELLED
    }

    public enum ItemType {
        ENERGY, TIME, SERVICE_FEE
    }
}
