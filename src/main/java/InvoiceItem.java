import enums.StationStatus;

public class InvoiceItem {
    private Long itemId;
    private Long invoiceId;
    private String description;
    private Integer quantity;
    private Double totalPrice;
    private StationStatus.ItemType itemType; // ENERGY, TIME, SERVICE_FEE

    public void calculateTotalPrice() {

    }

    public boolean matchesSearchCriteria(String field, String value) {
        return false;
    }
}
