import org.example.enums.DepositStatus;
import org.example.enums.PaymentMethod;

import java.time.LocalDateTime;

public class Deposit {
    private Long depositId;
    private Long userId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime depositDate;
    private DepositStatus status;

    public void processPayment() {

    }

    public boolean isSuccessful() {
        return false;
    }

    public void confirmDeposit() {

    }
}
