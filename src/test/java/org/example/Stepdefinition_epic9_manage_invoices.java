package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.managementClasses.InvoiceManager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Stepdefinition_epic9_manage_invoices {

    private InvoiceManager manager = InvoiceManager.getInstance();
    private Long activeCustomerId;
    private List<Invoice> retrievedInvoiceList;

    // For delete feature
    private Invoice invoiceToDelete;
    private boolean deleteResult;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @Given("a {string} with ID {long} exists")
    public void aCustomerWithIDExists(String entity, long id) {
        this.activeCustomerId = id;
        manager.clear();

        // reset delete-state too
        invoiceToDelete = null;
        deleteResult = false;
        outContent = null;
        originalOut = null;
    }

    @Given("the customer has the following {string}:")
    public void theCustomerHasInvoices(String entity, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String station = row.get("stationName");
            String mode = row.get("chargingMode");
            double kWh = Double.parseDouble(row.get("kWh"));
            int duration = Integer.parseInt(row.get("duration"));
            double price = Double.parseDouble(row.get("pricePerKWh"));
            double total = Double.parseDouble(row.get("totalCost"));
            String status = row.get("status");
            LocalDateTime localDateTime = LocalDateTime.parse(row.get("startTime"));
            Date start = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            Invoice inv = new Invoice(activeCustomerId, station, mode, kWh, duration, price, total, start, status);
            manager.createInvoice(inv);
        }

        // IMPORTANT: pick the invoice from the created ones (used by delete scenarios)
        List<Invoice> createdForCustomer = manager.getInvoicesForCustomer(activeCustomerId);
        if (!createdForCustomer.isEmpty()) {
            invoiceToDelete = createdForCustomer.get(0);
        }
    }

    @Given("the customer has no invoices")
    public void theCustomerHasNoInvoices() {
        manager.clear();
    }

    @When("the customer requests the invoice list")
    public void theCustomerRequestsTheInvoiceList() {
        retrievedInvoiceList = manager.getInvoicesForCustomer(activeCustomerId);
    }

    @Then("the list contains {int} invoices")
    public void theListContainsInvoices(int count) {
        assertEquals(count, retrievedInvoiceList.size());
    }

    @Then("the invoices are sorted by {string} \\(oldest first)")
    public void theInvoicesAreSorted(String criteria) {
        for (int i = 0; i < retrievedInvoiceList.size() - 1; i++) {
            Date d1 = retrievedInvoiceList.get(i).getStartTime();
            Date d2 = retrievedInvoiceList.get(i + 1).getStartTime();
            assertTrue(d1.before(d2) || d1.equals(d2), "Sortierung falsch: " + d1 + " ist nach " + d2);
        }
    }

    @Then("the first invoice is from {string} with total cost {double}")
    public void checkFirstInvoiceContent(String station, double cost) {
        Invoice first = retrievedInvoiceList.get(0);
        assertEquals(station, first.getStationName());
        assertEquals(cost, first.getTotalCost(), 0.01);
    }

    @Then("the first invoice is from {string} starting at {string}")
    public void checkFirstInvoiceDate(String station, String timeString) {
        Invoice first = retrievedInvoiceList.get(0);
        assertEquals(station, first.getStationName());
        LocalDateTime expected = LocalDateTime.parse(timeString);
        Date expectedDate = Date.from(expected.atZone(ZoneId.systemDefault()).toInstant());
        assertEquals(expectedDate.getTime(), first.getStartTime().getTime());
    }

    @Then("the system returns an empty list")
    public void theSystemReturnsAnEmptyList() {
        assertTrue(retrievedInvoiceList.isEmpty());
    }

    @Then("a message {string} is displayed")
    public void aMessageIsDisplayed(String msg) {
        if (retrievedInvoiceList.isEmpty()) {
            assertEquals("No invoices found", msg);
        }
    }

    // =========================
    // Delete old invoices steps
    // =========================

    private void startCapturingStdOut() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    private void stopCapturingStdOut() {
        if (originalOut != null) {
            System.setOut(originalOut);
        }
    }

    @When("the owner deletes the invoice")
    public void theOwnerDeletesTheInvoice() {
        assertNotNull(invoiceToDelete, "No invoice available to delete (check your Given table setup).");

        startCapturingStdOut();
        deleteResult = manager.deleteInvoice(invoiceToDelete);
        stopCapturingStdOut();
    }

    @Then("It gets deleted")
    public void itGetsDeleted() {
        assertTrue(deleteResult);
        assertFalse(manager.invoices.contains(invoiceToDelete));
    }

    @When("the owner deletes it.")
    public void theOwnerDeletesIt() {
        assertNotNull(invoiceToDelete, "No invoice available to delete (check your Given table setup).");

        startCapturingStdOut();
        deleteResult = manager.deleteInvoice(invoiceToDelete);
        stopCapturingStdOut();
    }

    @Then("the Invoice does not get deleted and an error is printed.")
    public void theInvoiceDoesNotGetDeletedAndAnErrorIsPrinted() {
        assertFalse(deleteResult);
        assertNotNull(invoiceToDelete);
        assertTrue(manager.invoices.contains(invoiceToDelete));

        String printed = (outContent != null) ? outContent.toString() : "";
        assertTrue(printed.contains("Error"), "Expected an error message to be printed.");
    }
}
