package org.example;

import java.util.Date;

public class Invoice {
    private static long idCounter = 1;
    private Long invoiceId;
    private Long customerId;      // Wichtig für US 5.1 (Filter nach Kunde)
    private String stationName;
    private String chargingMode;
    private double kWh;
    private int durationMinutes;
    private double pricePerKWh;
    private double totalCost;
    private Date startTime;       // Wichtig für US 5.2 (Sortierung)
    private String status;

    // Leerer Konstruktor für Flexibilität
    public Invoice() {
        this.invoiceId = idCounter++;
    }

    // Neuer Haupt-Konstruktor
    public Invoice(Long customerId, String stationName, String chargingMode, double kWh, int durationMinutes, double pricePerKWh, double totalCost, Date startTime, String status) {
        this.invoiceId = idCounter++;
        this.customerId = customerId;
        this.stationName = stationName;
        this.chargingMode = chargingMode;
        this.kWh = kWh;
        this.durationMinutes = durationMinutes;
        this.pricePerKWh = pricePerKWh;
        this.totalCost = totalCost;
        this.startTime = startTime;
        this.status = status;
    }

    // Getter & Setter (Notwendig für Manager & Tests)
    public Long getInvoiceId() { return invoiceId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getkWh() { return kWh; }
    public void setkWh(double kWh) { this.kWh = kWh; }

    public double getPricePerKWh() { return pricePerKWh; }
    public void setPricePerKWh(double pricePerKWh) { this.pricePerKWh = pricePerKWh; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getChargingMode() { return chargingMode; }
    public void setChargingMode(String mode) { this.chargingMode = mode; }

    // Methoden für PDF/Details (aus altem Code übernommen)
    public String generatePDF() {
        return "PDF generated for invoice #" + invoiceId + " with total: " + totalCost;
    }

    public String getDetails() {
        return "Invoice #" + invoiceId + "\n" +
                "Date: " + startTime + "\n" +
                "Station: " + stationName + "\n" +
                "Total: " + totalCost;
    }

    @Override
    public String toString() {
        return String.format("Invoice #%d | Cust: %d | %s | %.2f EUR | %s", invoiceId, customerId, startTime, totalCost, status);
    }
}