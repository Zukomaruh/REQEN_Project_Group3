
package org.example;

import org.example.enums.PaymentMethod;
import org.example.enums.PrepaidAmount;

import java.math.BigDecimal;

public class PrepaidBalance {
    private PaymentMethod paymentMethod;
    private BigDecimal currentAmount = BigDecimal.ZERO; // Kumulativer Amount statt Enum

    // Validierung entfernen, da sie fehlerhaft und unnötig ist (Enums sind typed)
    public PrepaidBalance(PaymentMethod paymentMethod, PrepaidAmount prepaidAmount) {
        setPaymentMethod(paymentMethod);
        setPrepaidAmount(BigDecimal.valueOf(prepaidAmount.getCode()));
    }


    // Neue Methode: Addiert den neuen PrepaidAmount zum aktuellen und gibt den neuen Wert zurück
    public BigDecimal addPrepaidAmount(PrepaidAmount prepaidAmount) {
        this.currentAmount = this.currentAmount.add(BigDecimal.valueOf(prepaidAmount.getCode()));
        return this.currentAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPrepaidAmount() {
        return currentAmount;
    }

    public void setPrepaidAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.currentAmount = amount;
    }
}

/*package org.example;

import org.example.enums.PaymentMethod;
import org.example.enums.PrepaidAmount;

public class PrepaidBalance{
    PaymentMethod paymentMethod;
    PrepaidAmount prepaidAmount;



    public PrepaidBalance (PaymentMethod paymentMethod, PrepaidAmount prepaidAmount) {
        if ( isInputValid(paymentMethod, prepaidAmount) ){
            setPrepaidAmount(prepaidAmount);
            setPaymentMethod(paymentMethod);
        }
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PrepaidAmount getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(PrepaidAmount prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }
}*/
