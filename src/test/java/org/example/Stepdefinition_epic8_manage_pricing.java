package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Stepdefinition_epic8_manage_pricing {
    @Given("a charging station exists with the stationId {int}")
    public void aChargingStationExistsWithTheStationId(int arg0) {
    }

    @When("I create a pricing rule with the pricingId {int}")
    public void iCreateAPricingRuleWithThePricingId(int arg0) {
    }

    @And("the locationId {int}")
    public void theLocationId(int arg0) {
    }

    @And("the validFrom {string}")
    public void theValidFrom(String arg0) {
    }

    @And("the validTo {string}")
    public void theValidTo(String arg0) {
    }

    @And("the priceComponents")
    public void thePriceComponents() {
    }

    @Then("a new pricing rule is created")
    public void aNewPricingRuleIsCreated() {
    }

    @Given("a pricing rule is created with the pricingId {int}")
    public void aPricingRuleIsCreatedWithThePricingId(int arg0) {
    }

    @When("the input data is valid")
    public void theInputDataIsValid() {
    }

    @Then("the pricing rule is added to the List of the pricing for the station")
    public void thePricingRuleIsAddedToTheListOfThePricingForTheStation() {
    }

    @And("isActive is true")
    public void isactiveIsTrue() {
    }

    @When("the input data is invalid")
    public void theInputDataIsInvalid() {
    }

    @And("the invalid input is deleted")
    public void theInvalidInputIsDeleted() {
    }

    @When("the pricingId {int} being the current pricing is true")
    public void thePricingIdBeingTheCurrentPricingIsTrue(int arg0) {
    }

    @Then("its current price {double} is displayed")
    public void itsCurrentPriceIsDisplayed(int arg0, int arg1) {
    }

    @Given("a pricing exists with the pricingId {int}")
    public void aPricingExistsWithThePricingId(int arg0) {
    }

    @When("I update the pricing {double}")
    public void iUpdateThePricing(int arg0, int arg1) {
    }

    @Then("the new pricing {double} is displayed")
    public void theNewPricingIsDisplayed(int arg0, int arg1) {
    }

    @Given("delete the pricing with the pricingId {int}")
    public void deleteThePricingWithThePricingId(int arg0) {
    }

    @When("the the new pricingId is null")
    public void theTheNewPricingIdIsNull() {
    }

    @Then("an error message is displayed that say {string}")
    public void anErrorMessageIsDisplayedThatSay(String arg0) {
    }

    @Given("the pricing rule with the pricingId {int} is in use by active sessions")
    public void thePricingRuleWithThePricingIdIsInUseByActiveSessions(int arg0) {
    }

    @When("I update the pricingId {int} to {double}")
    public void iUpdateThePricingIdTo(int arg0, int arg1, int arg2) {
    }

    @Then("ongoing sessions continue with {double}")
    public void ongoingSessionsContinueWith(int arg0, int arg1) {
    }

    @And("new sessions use {double}")
    public void newSessionsUse(int arg0, int arg1) {
    }

    @Given("chargingPoints is {int}")
    public void chargingpointsIs(int arg0) {
    }

    @When("I update the pricing to {double}")
    public void iUpdateThePricingTo(int arg0, int arg1) {
    }

    @Then("all chargingPoints should be updated to {double}")
    public void allChargingPointsShouldBeUpdatedTo(int arg0, int arg1) {
    }

    @Given("I have updated the pricing to {double}")
    public void iHaveUpdatedThePricingTo(int arg0, int arg1) {
    }

    @When("I want to update it again to {double}")
    public void iWantToUpdateItAgainTo(int arg0, int arg1) {
    }
}
