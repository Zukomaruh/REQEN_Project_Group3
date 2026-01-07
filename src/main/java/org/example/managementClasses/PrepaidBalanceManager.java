package org.example.managementClasses;

import org.example.Account;
import org.example.PrepaidBalance;
import org.example.enums.PaymentMethod;
import org.example.enums.PrepaidAmount;

import java.math.BigDecimal;

public class PrepaidBalanceManager {
    private static final PrepaidBalanceManager INSTANCE = new PrepaidBalanceManager();

    public static PrepaidBalanceManager getInstance(){
        return INSTANCE;
    }

    public Object addPrepaidBalance(Account account, String paymentInput, String prepaidInput){
        PrepaidAmount prepaidAmount = null;
        PaymentMethod paymentMethod = null;

        //Validierung PaymentMethod
        try {
            int paymentOption = Integer.parseInt(paymentInput.trim());
            paymentMethod = PaymentMethod.fromOption(paymentOption);
        }
        catch (NumberFormatException invalid){
            return null;
        }

        //Validierung PrepaidAmount
        try {
            int prepaidOption = Integer.parseInt(prepaidInput.trim());
            prepaidAmount = PrepaidAmount.fromOption(prepaidOption);
        }
        catch (NumberFormatException invalid){
            return null;
        }

        PrepaidBalance prepaidBalance = account.getPrepaidBalance();
        assert prepaidAmount != null;

        //Erste Aufladung?
        if (prepaidBalance == null) {
            prepaidBalance = new PrepaidBalance(paymentMethod, prepaidAmount);
        } else {
            BigDecimal newAmount = prepaidBalance.addPrepaidAmount(prepaidAmount);
            prepaidBalance.setPrepaidAmount(newAmount);
            prepaidBalance.setPaymentMethod(paymentMethod);
        }
        updatePrepaidBalance(account, prepaidBalance);

        System.out.println("payment successful!");
        return true;
    }

    private void updatePrepaidBalance(Account account, PrepaidBalance prepaidBalance) {
        account.setPrepaidBalance(prepaidBalance);
        System.out.printf("updated balance: %.2f", prepaidBalance);
    }
}
/*public boolean addIndividualPrepaidAmount(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be a positive number");
            return false;}
        if (amount.compareTo(new BigDecimal("20")) < 0) {
            System.out.println("Amount must be at least 20");
            return false;}
        if (amount.compareTo(new BigDecimal("500")) > 0) {
            System.out.println("Amount must not exceed 500");
            return false;}

        PrepaidBalance balance = account.getPrepaidBalance();
        if (balance == null) {
            balance = new PrepaidBalance(PaymentMethod.CREDIT_CARD, PrepaidAmount.TWENTY); // Dummy-Initialisierung
            balance.setPrepaidAmount(BigDecimal.ZERO);
            account.setPrepaidBalance(balance);
        }
        BigDecimal newTotal = balance.getPrepaidAmount().add(amount);
        balance.setPrepaidAmount(newTotal);

        // Balance aktualisieren (mit deiner bestehenden Ausgabe)
        updatePrepaidBalance(account, balance);

        System.out.println("payment successful!");
        return true;
    }*/

/*package org.example.managementClasses;

import org.example.Account;
import org.example.PrepaidBalance;
import org.example.enums.PaymentMethod;
import org.example.enums.PrepaidAmount;

public class PrepaidBalanceManager {
    Account account;
    PrepaidBalance prepaidBalance;
    private static final PrepaidBalanceManager INSTANCE = new PrepaidBalanceManager();

    public static PrepaidBalanceManager getInstance(){
        return INSTANCE;
    }

    public boolean addPrepaidBalance(Account account, PrepaidBalance prepaidBalance){
        account.setPrepaidBalance(prepaidBalance);
        System.out.println("payment successful!");
        return true;
    }
}
*/
