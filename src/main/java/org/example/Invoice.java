package org.example;

public class Invoice {
    private double kWh;
    private int duration;
    private String mode;
    private String station;
    private java.util.Date startTime;
    private java.util.Date endTime;
    private double pricePerKWh;
    private double totalCost;
    private String appliedPricingRule;
    private String status;
    private double customerBalance; // For prepaid simulation

    public Invoice() {
    }

    public Invoice(double kWh, int duration, String mode, String station, java.util.Date startTime, java.util.Date endTime, double pricePerKWh, String appliedPricingRule) {
        this.kWh = kWh;
        this.duration = duration;
        this.mode = mode;
        this.station = station;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pricePerKWh = pricePerKWh;
        this.appliedPricingRule = appliedPricingRule;
        this.totalCost = kWh * pricePerKWh;
        this.status = "Created";
        this.customerBalance = 0; // Default
    }

    // Getters and Setters
    public double getkWh() {
        return kWh;
    }

    public void setkWh(double kWh) {
        this.kWh = kWh;
        this.totalCost = this.kWh * this.pricePerKWh;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public java.util.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    public java.util.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public double getPricePerKWh() {
        return pricePerKWh;
    }

    public void setPricePerKWh(double pricePerKWh) {
        this.pricePerKWh = pricePerKWh;
        this.totalCost = this.kWh * this.pricePerKWh;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getAppliedPricingRule() {
        return appliedPricingRule;
    }

    public void setAppliedPricingRule(String appliedPricingRule) {
        this.appliedPricingRule = appliedPricingRule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCustomerBalance() {
        return customerBalance;
    }

    public void setCustomerBalance(double customerBalance) {
        this.customerBalance = customerBalance;
    }

    public void deductFromBalance() {
        if (this.customerBalance >= this.totalCost) {
            this.customerBalance -= this.totalCost;
        } else {
            // Simulate insufficient balance, but for test, just set to negative or handle
            this.customerBalance -= this.totalCost;
        }
    }

    public String generatePDF() {
        // Simulate PDF generation
        return "PDF generated for invoice with total: " + totalCost;
    }

    public String getDetails() {
        return "Start time: " + startTime + "\nEnd time: " + endTime + "\nkWh charged: " + kWh + "\nPrice per kWh: " + pricePerKWh + "\nTotal cost: " + totalCost + "\nApplied pricing rule: " + appliedPricingRule;
    }
}