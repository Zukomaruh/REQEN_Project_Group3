package org.example.enums;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    BANK_TRANSFER,
    APPLE_PAY,
    GOOGLE_PAY;

    public static PaymentMethod fromOption(int option) {
        return switch (option) {
            case 1 -> CREDIT_CARD;
            case 2 -> DEBIT_CARD;
            case 3 -> PAYPAL;
            case 4 -> BANK_TRANSFER;
            case 5 -> APPLE_PAY;
            default -> GOOGLE_PAY;
        };
    }
}
