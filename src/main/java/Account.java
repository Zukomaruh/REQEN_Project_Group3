import java.time.LocalDateTime;

public class Account {
    private Long accountId;
    private Long userId;
    private Double balance;
    //private AccountType type; // PREPAID, POSTPAID
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
