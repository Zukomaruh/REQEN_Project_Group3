package org.example;

import java.time.LocalDateTime;

public class PrepaidAccount {
    private Long accountId;
    private Long userId;
    private Double balance;

    public PrepaidAccount(Long accountId) {
        this.accountId = accountId;
    }

    private LocalDateTime lastDepositDate;

    public void deposit(Double amount) {

    }

    public void withdraw(Double amount) {

    }

    public boolean hasSufficientBalance(Double amount) {
        return false;
    }

    public void applyTransaction(Transaction transaction) {

    }

    public Double getAvailableBalance() {
        return null;
    }


}
