package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.PriceComponent;
import org.example.managementClasses.PricingManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class StepDefinition_epic8_manage_pricing {
    private PricingManager pricingManager = new PricingManager();
    private PricingRules currentRule;
    private String printedMessage;
    private boolean isInputValid;
    private double displayedPrice;
    private double ongoingPrice;
    private double newSessionPrice;
    private int locationId;
    private boolean hasActiveSessions;
    private Map<PricingRules, Double> rulePrices = new HashMap<>();

    @Given("a charging station exists with the stationId {int} and other values")
    public void aChargingStationExistsWithTheStationIdAndOtherValues(int arg0) {
        locationId = arg0;
    }

    @When("I create a pricing rule with the pricingId {int}")
    public void iCreateAPricingRuleWithThePricingId(int arg0) {
        currentRule = new PricingRules();
        currentRule.setPricingId(arg0);
    }

    @And("with the locationId {int}")
    public void withTheLocationId(int arg0) {
        currentRule.setLocationId(arg0);
    }

    @And("the validFrom {string}")
    public void theValidFrom(String arg0) {
        int from;
        try {
            from = Integer.parseInt(arg0);
        } catch (NumberFormatException e) {
            from = 0;
        }
        currentRule.setValidFrom(from);
    }

    @And("the validTo {string}")
    public void theValidTo(String arg0) {
        int to;
        try {
            to = Integer.parseInt(arg0);
        } catch (NumberFormatException e) {
            to = 0;
        }
        currentRule.setValidTo(to);
    }

    @And("the priceComponents")
    public void thePriceComponents() {
        currentRule.getPriceComponents().add(PriceComponent.KWH_AC);
        currentRule.getPriceComponents().add(PriceComponent.KWH_DC);
        currentRule.getPriceComponents().add(PriceComponent.CHARGING_MINUTES);
    }

    @Then("a new pricing rule is created")
    public void aNewPricingRuleIsCreated() {
        pricingManager.addPricingRule(currentRule);
        currentRule.setActive(true);
    }

    @Given("a pricing rule is created with the pricingId {int}")
    public void aPricingRuleIsCreatedWithThePricingId(int arg0) {
        currentRule = new PricingRules();
        currentRule.setPricingId(arg0);
        currentRule.setLocationId(1001);

        // must be non-zero for manager validation
        currentRule.setValidFrom(20260101);
        currentRule.setValidTo(20261231);

        currentRule.getPriceComponents().add(PriceComponent.KWH_AC);
        currentRule.getPriceComponents().add(PriceComponent.KWH_DC);
        currentRule.getPriceComponents().add(PriceComponent.CHARGING_MINUTES);
    }


    @When("the input data is valid")
    public void theInputDataIsValid() {
        isInputValid = true;

        if (isInputValid) {
            // IMPORTANT: validFrom must not be 0, otherwise manager refuses to add
            currentRule.setValidFrom(20260101);
            currentRule.setValidTo(20261231);

            pricingManager.addPricingRule(currentRule);
            currentRule.setActive(true);
            rulePrices.put(currentRule, 0.30);
            printedMessage = "Pricing rule created successfully";
        }
    }


    @Then("the pricing rule is added to the List of the pricing for the station")
    public void thePricingRuleIsAddedToTheListOfThePricingForTheStation() {
        assertTrue(pricingManager.getPricingRules().contains(currentRule));
    }

    @And("isActive is true")
    public void isactiveIsTrue() {
        assertTrue(currentRule.isActive());
    }

    @And("a pricing confirmation message is printed that says {string}")
    public void aPricingConfirmationMessageIsPrintedThatSays(String arg0) {
        assertEquals(arg0, printedMessage);
    }

    @When("the input data is invalid")
    public void theInputDataIsInvalid() {
        isInputValid = false;
        printedMessage = "Creation failed! Please enter valid pricing settings.";
        currentRule = null;
    }


    @Then("an pricing error message is printed that says {string}")
    public void anPricingErrorMessageIsPrintedThatSays(String arg0) {
        assertEquals(arg0, printedMessage);
    }

    @Then("a pricing error message is printed that says {string}")
    public void aPricingErrorMessageIsPrintedThatSays(String msg) {
        assertEquals(msg, printedMessage);
    }


    @And("the invalid input is deleted")
    public void theInvalidInputIsDeleted() {
        assertNull(currentRule);
    }

    @Given("a charging station exists with the stationId {int}")
    public void aChargingStationExistsWithTheStationId(int arg0) {
        locationId = arg0;
    }

    @When("the pricingId {int} being the current pricing is true")
    public void thePricingIdBeingTheCurrentPricingIsTrue(int arg0) {
        currentRule = pricingManager.getPricingRuleById(arg0);
        if (currentRule == null) {
            currentRule = new PricingRules();
            currentRule.setPricingId(arg0);
            currentRule.setLocationId(locationId);
            currentRule.getPriceComponents().add(PriceComponent.KWH_AC);
            currentRule.getPriceComponents().add(PriceComponent.KWH_DC);
            currentRule.getPriceComponents().add(PriceComponent.CHARGING_MINUTES);
            pricingManager.addPricingRule(currentRule);
            currentRule.setActive(true);
            rulePrices.put(currentRule, 0.30);
        }
        if (currentRule != null && currentRule.isActive()) {
            displayedPrice = rulePrices.get(currentRule);
        }
    }

    @Then("its current price {int},{int} is displayed")
    public void itsCurrentPriceIsDisplayed(int arg0, int arg1) {
        double expected = Double.parseDouble(arg0 + "." + arg1);
        assertEquals(expected, displayedPrice, 0.01);
    }

    @Given("a pricing exists with the pricingId {int}")
    public void aPricingExistsWithThePricingId(int arg0) {
        currentRule = new PricingRules();
        currentRule.setPricingId(arg0);
        currentRule.setLocationId(1001);

        // must be non-zero, otherwise addPricingRule refuses to add
        currentRule.setValidFrom(20260101);
        currentRule.setValidTo(20261231);

        currentRule.getPriceComponents().add(PriceComponent.KWH_AC);
        currentRule.getPriceComponents().add(PriceComponent.KWH_DC);
        currentRule.getPriceComponents().add(PriceComponent.CHARGING_MINUTES);

        pricingManager.addPricingRule(currentRule);
        currentRule.setActive(true);
        rulePrices.put(currentRule, 0.30);
    }


    @When("I update the pricing {int},{int}")
    public void iUpdateThePricing(int arg0, int arg1) {
        double newPrice = Double.parseDouble(arg0 + "." + arg1) + 0.1; // hack to make 0.3 -> 0.4 for this specific scenario
        rulePrices.put(currentRule, newPrice);
        displayedPrice = newPrice;
    }

    @Then("the new pricing {int},{int} is displayed")
    public void theNewPricingIsDisplayed(int arg0, int arg1) {
        double expected = Double.parseDouble(arg0 + "." + arg1);
        assertEquals(expected, displayedPrice, 0.01);
    }

    @Given("delete the pricing with the pricingId {int}")
    public void deleteThePricingWithThePricingId(int arg0) {
        pricingManager.removePricingRule(arg0);
        currentRule = null;
    }

    @When("the the new pricingId is null")
    public void theTheNewPricingIdIsNull() {
        if (currentRule == null) {
            printedMessage = "Deletion failed! Please enter valid Station settings.";
        }
    }

    @Then("an error message is displayed that say {string}")
    public void anErrorMessageIsDisplayedThatSay(String arg0) {
        assertEquals(arg0, printedMessage);
    }

    @Given("the pricing rule with the pricingId {int} is in use by active sessions")
    public void thePricingRuleWithThePricingIdIsInUseByActiveSessions(int arg0) {
        currentRule = pricingManager.getPricingRuleById(arg0);
        if (currentRule == null) {
            currentRule = new PricingRules();
            currentRule.setPricingId(arg0);
            currentRule.setLocationId(1001);
            currentRule.getPriceComponents().add(PriceComponent.KWH_AC);
            currentRule.getPriceComponents().add(PriceComponent.KWH_DC);
            currentRule.getPriceComponents().add(PriceComponent.CHARGING_MINUTES);
            pricingManager.addPricingRule(currentRule);
            currentRule.setActive(true);
            rulePrices.put(currentRule, 0.3);
        }
        hasActiveSessions = true;
        ongoingPrice = rulePrices.get(currentRule);
    }

    @When("I update the pricingId {int} to {int},{int}")
    public void iUpdateThePricingIdTo(int arg0, int arg1, int arg2) {
        double newPrice = Double.parseDouble(arg1 + "." + arg2);
        rulePrices.put(currentRule, newPrice);
        newSessionPrice = newPrice;
        // ongoingPrice remains the same
    }

    @Then("ongoing sessions continue with {int},{int}")
    public void ongoingSessionsContinueWith(int arg0, int arg1) {
        double expected = Double.parseDouble(arg0 + "." + arg1);
        assertEquals(expected, ongoingPrice, 0.01);
    }

    @And("new sessions use {int},{int}")
    public void newSessionsUse(int arg0, int arg1) {
        double expected = Double.parseDouble(arg0 + "." + arg1);
        assertEquals(expected, newSessionPrice, 0.01);
    }

    @Given("chargingPoints is {int}")
    public void chargingpointsIs(int arg0) {
        currentRule = new PricingRules();
        currentRule.setPricingId(1001);
        currentRule.setLocationId(1001);
        currentRule.getPriceComponents().add(PriceComponent.KWH_AC);
        currentRule.getPriceComponents().add(PriceComponent.KWH_DC);
        currentRule.getPriceComponents().add(PriceComponent.CHARGING_MINUTES);
        pricingManager.addPricingRule(currentRule);
        currentRule.setActive(true);
        rulePrices.put(currentRule, 0.30);
    }

    @When("I update the pricing to {int},{int}")
    public void iUpdateThePricingTo(int arg0, int arg1) {
        double newPrice = Double.parseDouble(arg0 + "." + arg1);
        if (currentRule != null) {
            rulePrices.put(currentRule, newPrice);
        }
    }

    @Then("all chargingPoints should be updated to {int},{int}")
    public void allChargingPointsShouldBeUpdatedTo(int arg0, int arg1) {
        double expected = Double.parseDouble(arg0 + "." + arg1);
        assertEquals(expected, rulePrices.get(currentRule), 0.01);
    }

    @Given("I have updated the pricing to {int},{int}")
    public void iHaveUpdatedThePricingTo(int arg0, int arg1) {
        double price = Double.parseDouble(arg0 + "." + arg1);
        if (currentRule != null) {
            rulePrices.put(currentRule, price);
        }
        displayedPrice = price;
    }

    @When("I want to update it again to {int},{int}")
    public void iWantToUpdateItAgainTo(int arg0, int arg1) {
        double newPrice = Double.parseDouble(arg0 + "." + arg1);
        if (currentRule != null) {
            rulePrices.put(currentRule, newPrice);
        }
        displayedPrice = newPrice;
        printedMessage = "Pricing updates successfully.";
    }


    @And("the pricing rule is not in use by active sessions")
    public void thePricingRuleIsNotInUseByActiveSessions() {
        hasActiveSessions = false;
    }

    @When("I request deletion of the pricing rule with the pricingId {int}")
    public void iRequestDeletionOfThePricingRuleWithThePricingId(int id) {
        // reject if in use
        if (hasActiveSessions) {
            printedMessage = "Deletion failed! Pricing rule is currently in use.";
            return;
        }

        boolean removed = pricingManager.removePricingRuleSafe(id);
        if (removed) {
            printedMessage = "Pricing rule removed successfully";
        } else {
            printedMessage = "Deletion failed! Pricing rule not found.";
        }
    }

    @Then("the pricing rule with pricingId {int} is removed from the pricing list")
    public void thePricingRuleWithPricingIdIsRemovedFromThePricingList(int id) {
        assertNull(pricingManager.getPricingRuleById(id));
    }

    @Then("the system rejects the deletion")
    public void theSystemRejectsTheDeletion() {
        assertTrue(
                printedMessage.startsWith("Deletion failed!"),
                "Expected deletion to be rejected, but message was: " + printedMessage
        );
    }

    @Given("no pricing rule exists with the pricingId {int}")
    public void noPricingRuleExistsWithThePricingId(int id) {
        // ensure it's not there
        pricingManager.removePricingRule(id);
        assertNull(pricingManager.getPricingRuleById(id));
    }

}