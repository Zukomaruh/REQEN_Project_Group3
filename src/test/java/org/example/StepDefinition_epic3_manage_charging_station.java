package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinition_epic3_manage_charging_station {
    @Given("a charging location exists with locationId {int}")
    public void aChargingLocationExistsWithLocationId(int arg0) {
    }

    @When("the owner creates a charging station with the name {string}")
    public void theOwnerCreatesAChargingStationWithTheName(String arg0) {
    }

    @And("the locationID {int}")
    public void theLocationID(int arg0) {
    }

    @And("the type AC")
    public void theTypeAC() {
    }

    @And("the capacity {int}")
    public void theCapacity(int arg0) {
    }

    @And("the pricing of {double}")
    public void thePricingOf(int arg0, int arg1) {
    }

    @Then("a new charging station with the name {string}, an unique stationID and the status AVAILABLE is created.")
    public void aNewChargingStationWithTheNameAnUniqueStationIDAndTheStatusAVAILABLEIsCreated(String arg0) {
    }

    @And("the station is added to the List of the location with locationId {int}")
    public void theStationIsAddedToTheListOfTheLocationWithLocationId(int arg0) {
    }

    @Given("the owner is on the system")
    public void theOwnerIsOnTheSystem() {
    }

    @Then("no charging station is created")
    public void noChargingStationIsCreated() {
    }

    @And("an error massage is printed that says {string}")
    public void anErrorMassageIsPrintedThatSays(String arg0) {
    }

    @Given("these charging location exist:")
    public void theseChargingLocationExist() {
    }

    @And("these stations exist:")
    public void theseStationsExist() {
    }

    @When("the customer requests the charging stations information")
    public void theCustomerRequestsTheChargingStationsInformation() {
    }

    @Then("the output looks like this:")
    public void theOutputLooksLikeThis() {
    }

    @When("the pricing of the charging station {string} is updated to {double}")
    public void thePricingOfTheChargingStationIsUpdatedTo(String arg0, int arg1, int arg2) {
    }

    @And("the information is requested")
    public void theInformationIsRequested() {
    }
}
