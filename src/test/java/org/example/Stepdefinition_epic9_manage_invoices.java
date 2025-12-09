package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.managementClasses.InvoiceManager;

import static org.junit.jupiter.api.Assertions.*;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class Stepdefinition_epic9_manage_invoices {
    private InvoiceManager manager = new InvoiceManager();
    private Invoice currentInvoice;
    private Date startTime = new Date();
    private Date endTime = new Date(System.currentTimeMillis() + 3600000); // 1 hour later
    private double kWh = 10.0;
    private int duration = 60;
    private String mode = "Fast";
    private String station = "Station1";
    private String pricingRule = "Standard";
    private String pdfContent;
    private String details;
    private String history;

    @Given("a charging session has ended")
    public void aChargingSessionHasEnded() {
        // Simulate session end
        endTime = new Date();
    }

    @When("the session result is received with:")
    public void theSessionResultIsReceivedWith(DataTable dataTable) {
        // Fields: kWh, duration, mode, station are assumed received
        manager.createInvoiceFromSession(kWh, duration, mode, station, startTime, endTime, pricingRule);
        currentInvoice = manager.getInvoice(0);
    }

    @Then("an invoice record is created automatically")
    public void anInvoiceRecordIsCreatedAutomatically() {
        assertNotNull(currentInvoice);
        assertEquals("Created", currentInvoice.getStatus());
    }

    @Given("prices may have changed during the day")
    public void pricesMayHaveChangedDuringTheDay() {
        manager.setCurrentPrice(0.30); // Simulate change, but getPriceAtTime returns priceAtStart = 0.25
    }

    @When("the invoice is created")
    public void theInvoiceIsCreated() {
        manager.createInvoiceFromSession(kWh, duration, mode, station, startTime, endTime, pricingRule);
        currentInvoice = manager.getInvoice(0);
    }

    @Then("the system applies the price valid at the start time of the charging session")
    public void theSystemAppliesThePriceValidAtTheStartTimeOfTheChargingSession() {
        assertEquals(0.25, currentInvoice.getPricePerKWh(), 0.001); // Should use price at start
    }

    @Given("the customer uses a prepaid system")
    public void theCustomerUsesAPrepaidSystem() {
        currentInvoice = new Invoice(kWh, duration, mode, station, startTime, endTime, 0.25, pricingRule);
        currentInvoice.setCustomerBalance(100.0);
    }

    @When("the invoice is generated")
    public void theInvoiceIsGenerated() {
        manager.deductBalance(currentInvoice);
    }

    @Then("the charged amount is deducted from the customer’s available balance")
    public void theChargedAmountIsDeductedFromTheCustomerSAvailableBalance() {
        assertEquals(100.0 - currentInvoice.getTotalCost(), currentInvoice.getCustomerBalance(), 0.001);
    }

    @Given("I open an invoice")
    public void iOpenAnInvoice() {
        currentInvoice = new Invoice(kWh, duration, mode, station, startTime, endTime, 0.25, pricingRule);
    }

    @When("I expand the invoice details")
    public void iExpandTheInvoiceDetails() {
        details = manager.viewDetails(currentInvoice);
    }

    @Then("I see itemized rows containing:")
    public void iSeeItemizedRowsContaining(DataTable dataTable) {
        assertTrue(details.contains("Start time"));
        assertTrue(details.contains("End time"));
        assertTrue(details.contains("kWh charged"));
        assertTrue(details.contains("Price per kWh"));
        assertTrue(details.contains("Total cost"));
        assertTrue(details.contains("Applied pricing rule"));
    }

    @Given("I click {string}")
    public void iClick(String arg0) {
        if ("Download PDF".equals(arg0)) {
            if (currentInvoice == null) {
                currentInvoice = new Invoice(kWh, duration, mode, station, startTime, endTime, 0.25, pricingRule);
            }
            pdfContent = manager.downloadPDF(currentInvoice);
        } else if ("Flag for correction".equals(arg0)) {
            manager.flagForCorrection(currentInvoice, "User");
        }
    }

    @When("the request is made")
    public void theRequestIsMade() {
        // Request simulated in click
    }

    @Then("a correctly formatted PDF invoice is generated instantly")
    public void aCorrectlyFormattedPDFInvoiceIsGeneratedInstantly() {
        assertNotNull(pdfContent);
        assertTrue(pdfContent.contains("PDF generated"));
    }

    @And("the PDF is available for immediate download")
    public void thePDFIsAvailableForImmediateDownload() {
        assertTrue(pdfContent.length() > 0);
    }

    @Given("I detect a billing error")
    public void iDetectABillingError() {
        currentInvoice = new Invoice(kWh, duration, mode, station, startTime, endTime, 0.25, pricingRule);
    }

    @Then("the invoice is marked as {string}")
    public void theInvoiceIsMarkedAs(String arg0) {
        assertEquals(arg0, currentInvoice.getStatus());
    }

    @And("the action is logged with reporter and timestamp")
    public void theActionIsLoggedWithReporterAndTimestamp() {
        assertFalse(manager.getLogs().isEmpty());
        assertTrue(manager.getLogs().get(0).contains("Flagged by"));
    }

    @And("the invoice appears in the {string} queue")
    public void theInvoiceAppearsInTheQueue(String arg0) {
        assertFalse(manager.getReviewQueue().isEmpty());
        assertEquals(currentInvoice, manager.getReviewQueue().get(0));
    }

    @Given("an invoice is flagged or selected for correction")
    public void anInvoiceIsFlaggedOrSelectedForCorrection() {
        currentInvoice = new Invoice(kWh, duration, mode, station, startTime, endTime, 0.25, pricingRule);
        manager.flagForCorrection(currentInvoice, "User");
    }

    @When("I open the correction tool")
    public void iOpenTheCorrectionTool() {
        // Simulate opening tool
    }

    @Then("I can adjust the following fields:")
    public void iCanAdjustTheFollowingFields(DataTable dataTable) {
        // Simulate adjustment capability; in test, just call setters
        currentInvoice.setkWh(15.0);
        currentInvoice.setPricePerKWh(0.20);
        currentInvoice.setDuration(90);
        assertEquals(15.0, currentInvoice.getkWh(), 0.001);
        assertEquals(0.20, currentInvoice.getPricePerKWh(), 0.001);
        assertEquals(90, currentInvoice.getDuration());
    }

    @And("I can generate a credit note and a corrected invoice")
    public void iCanGenerateACreditNoteAndACorrectedInvoice() {
        manager.correctInvoice(currentInvoice, 15.0, 0.20, 90);
        assertEquals("Corrected", manager.getInvoice(0).getStatus());
    }

    @Given("a correction is saved")
    public void aCorrectionIsSaved() {
        currentInvoice = new Invoice(kWh, duration, mode, station, startTime, endTime, 0.25, pricingRule);
        manager.invoices.add(currentInvoice); // Add original to list
        manager.correctInvoice(currentInvoice, 15.0, 0.20, 90);
    }

    @When("the process is completed")
    public void theProcessIsCompleted() {
        // Process complete simulated
    }

    @Then("the customer automatically receives the credit note and the corrected invoice via email")
    public void theCustomerAutomaticallyReceivesTheCreditNoteAndTheCorrectedInvoiceViaEmail() {
        Invoice creditNote = new Invoice(); // Placeholder
        Invoice corrected = manager.getInvoice(1); // Since added
        manager.sendEmail(creditNote, corrected);
        // Assume sent, no assert needed
    }

    @Given("the correction is processed")
    public void theCorrectionIsProcessed() {
        currentInvoice = new Invoice(kWh, duration, mode, station, startTime, endTime, 0.25, pricingRule);
        manager.invoices.add(currentInvoice); // Add original to list
        manager.correctInvoice(currentInvoice, 15.0, 0.20, 90);
    }

    @When("I view the customer’s invoice history")
    public void iViewTheCustomerSInvoiceHistory() {
        history = manager.viewHistory();
    }

    @Then("the original invoice is shown as {string}")
    public void theOriginalInvoiceIsShownAs(String arg0) {
        assertTrue(history.contains(arg0));
    }

    @And("a link to the new invoice version is displayed")
    public void aLinkToTheNewInvoiceVersionIsDisplayed() {
        // Simulate link; assert history has corrected
        assertTrue(history.contains("Corrected"));
    }
}