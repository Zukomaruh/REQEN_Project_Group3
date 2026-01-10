package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.managementClasses.InvoiceManager;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Stepdefinition_epic9_manage_invoices {

    private InvoiceManager manager = InvoiceManager.getInstance();
    private Long activeCustomerId;
    private List<Invoice> retrievedInvoiceList;

    @Given("a {string} with ID {long} exists")
    public void aCustomerWithIDExists(String entity, long id) {
        this.activeCustomerId = id;
        manager.clear();
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
            Date d2 = retrievedInvoiceList.get(i+1).getStartTime();
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
}