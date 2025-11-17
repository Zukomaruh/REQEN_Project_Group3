import enums.InvoiceStatus;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private Long invoiceId;
    private Long userId;
    private Long chargingSessionId;
    private String invoiceNumber;
    private Double totalAmount;
    private InvoiceStatus status; // PENDING, PAID, OVERDUE
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    private List<InvoiceItem> items;

    public void addItem(InvoiceItem item) {

    }

    public void removeItem(Long itemId) {

    }

    public Double calculateTotal() {
        return null;
    }

    public void markAsPaid() {

    }

    public boolean isOverdue() {
        return false;
    }

    public List<InvoiceItem> sortBy(String fieldName, boolean ascending) {
        return null;
    }
}