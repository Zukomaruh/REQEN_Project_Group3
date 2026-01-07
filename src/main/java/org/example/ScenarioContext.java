package org.example;

import org.example.enums.PrepaidAmount;

import java.math.BigDecimal;

public class ScenarioContext {
    static BigDecimal previousBalance;
    static BigDecimal lastDepositedAmount;
    static BigDecimal expectedBalance;

    public void savePreviousBalance(BigDecimal balance) {
        this.previousBalance = balance;
    }

    public void saveLastDepositedAmount(PrepaidAmount amount) {
        this.lastDepositedAmount = BigDecimal.valueOf(amount.getCode());
        this.expectedBalance = previousBalance.add(this.lastDepositedAmount);
    }

    public BigDecimal getExpectedBalance() {
        return expectedBalance;
    }

    public static BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public static BigDecimal getLastDepositedAmount() {
        return lastDepositedAmount;
    }
}