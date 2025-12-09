package org.example.managementClasses;

import org.example.Invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceManager {
    public List<Invoice> invoices = new ArrayList<>();
    private List<Invoice> reviewQueue = new ArrayList<>();
    private double currentPricePerKWh = 0.25; // Default price
    private double priceAtStart = 0.25; // To simulate price at start
    private List<String> logs = new ArrayList<>();

    public void createInvoiceFromSession(double kWh, int duration, String mode, String station, Date startTime, Date endTime, String pricingRule) {
        double priceAtStart = getPriceAtTime(startTime);
        Invoice invoice = new Invoice(kWh, duration, mode, station, startTime, endTime, priceAtStart, pricingRule);
        invoices.add(invoice);
    }

    private double getPriceAtTime(Date time) {
        return priceAtStart; // Use the simulated start price
    }

    public void setCurrentPrice(double price) {
        this.currentPricePerKWh = price;
    }

    public Invoice getInvoice(int index) {
        if (index < invoices.size()) {
            return invoices.get(index);
        }
        return null;
    }

    public void deductBalance(Invoice invoice) {
        invoice.deductFromBalance();
    }

    public String viewDetails(Invoice invoice) {
        return invoice.getDetails();
    }

    public String downloadPDF(Invoice invoice) {
        return invoice.generatePDF();
    }

    public void flagForCorrection(Invoice invoice, String reporter) {
        invoice.setStatus("Needs Review");
        logs.add("Flagged by " + reporter + " at " + new Date());
        reviewQueue.add(invoice);
    }

    public void correctInvoice(Invoice invoice, double newKWh, double newPrice, int newDuration) {
        invoice.setkWh(newKWh);
        invoice.setPricePerKWh(newPrice);
        invoice.setDuration(newDuration);
        // Simulate credit note and new invoice
        Invoice creditNote = new Invoice(); // Placeholder
        Invoice corrected = new Invoice(invoice.getkWh(), invoice.getDuration(), invoice.getMode(), invoice.getStation(), invoice.getStartTime(), invoice.getEndTime(), invoice.getPricePerKWh(), invoice.getAppliedPricingRule());
        corrected.setStatus("Corrected");
        invoices.add(corrected);
        invoice.setStatus("Cancelled/Corrected");
    }

    public void sendEmail(Invoice creditNote, Invoice corrected) {
        // Simulate email send
        System.out.println("Email sent with credit note and corrected invoice");
    }

    public String viewHistory() {
        StringBuilder history = new StringBuilder();
        for (Invoice inv : invoices) {
            history.append("Invoice status: ").append(inv.getStatus()).append("\n");
        }
        return history.toString();
    }

    public List<Invoice> getReviewQueue() {
        return reviewQueue;
    }

    public List<String> getLogs() {
        return logs;
    }
}