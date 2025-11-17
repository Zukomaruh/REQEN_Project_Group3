import java.time.LocalDateTime;

public class Transaction {
    private Long transactionId;
    private Long accountId;
    private TransactionType type; // DEPOSIT, WITHDRAWAL, PAYMENT
    private Double amount;
    private String description;
    private LocalDateTime transactionDate;
    private TransactionStatus status;

    public boolean isValid() {
        return false;
    }

    public void process() {

    }

    public void rollback() {

    }
}
