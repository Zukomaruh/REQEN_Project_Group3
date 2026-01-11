package org.example.managementClasses;

import org.example.Invoice;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Calendar;
import java.util.Date;

public class InvoiceManager {
    private static final InvoiceManager INSTANCE = new InvoiceManager();
    public List<Invoice> invoices = new ArrayList<>();
    private List<String> logs = new ArrayList<>();

    public static InvoiceManager getInstance() { return INSTANCE; }

    public InvoiceManager() { }

    // Neue Methode zum Hinzufügen (nutzt die Main und Tests)
    public void createInvoice(Invoice invoice) {
        invoices.add(invoice);
    }

    /**
     * US 5.1 & 5.2: Read & Sort Invoices
     * Liefert Rechnungen für einen Kunden, sortiert nach Datum (älteste zuerst).
     */
    public List<Invoice> getInvoicesForCustomer(Long customerId) {
        return invoices.stream()
                // FILTER (US 5.1)
                .filter(inv -> inv.getCustomerId() != null && inv.getCustomerId().equals(customerId))
                // SORT (US 5.2)
                .sorted(Comparator.comparing(Invoice::getStartTime))
                .collect(Collectors.toList());
    }

    public Invoice getInvoice(int index) {
        if (index < invoices.size()) return invoices.get(index);
        return null;
    }

    public String viewDetails(Invoice invoice) {
        return invoice.getDetails();
    }

    public String downloadPDF(Invoice invoice) {
        return invoice.generatePDF();
    }

    public String viewHistory() {
        StringBuilder history = new StringBuilder();
        for (Invoice inv : invoices) {
            history.append(inv.toString()).append("\n");
        }
        return history.toString();
    }

    /**
     * Deletes an invoice only if it is older than 7 years.
     * @return true if deleted, false otherwise (prints error).
     */
    public boolean deleteInvoice(Invoice invoice) {
        if (invoice == null || invoice.getStartTime() == null) {
            System.out.println("Error: Invoice or start time is null.");
            return false;
        }

        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.YEAR, -7); // now minus 7 years
        Date sevenYearsAgo = threshold.getTime();

        // "older than 7 years" => strictly before the threshold date
        if (invoice.getStartTime().before(sevenYearsAgo)) {
            return invoices.remove(invoice);
        } else {
            System.out.println("Error: Invoice cannot be deleted because it is not older than 7 years.");
            return false;
        }
    }

    // Hilfsmethode für Tests
    public void clear() {
        invoices.clear();
        logs.clear();
    }
}