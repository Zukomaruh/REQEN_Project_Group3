package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitons_ManageChargingProcess {
    @Given("the battery is less than {int}%")
    public void theBatteryIsLessThan(int arg0) {
    }

    @When("the car is connected")
    public void theCarIsConnected() {
    }

    @Then("it charges to {int}% or target percentage")
    public void itChargesToOrTargetPercentage(int arg0) {
    }

    @Given("the battery is greater or equal to {int}%")
    public void theBatteryIsGreaterOrEqualTo(int arg0) {
    }

    @When("charging begins")
    public void chargingBegins() {
    }

    @Then("the car starts charging immediately")
    public void theCarStartsChargingImmediately() {
    }

    @Given("the car has finished charging")
    public void theCarHasFinishedCharging() {
    }

    @When("the target percentage is charged")
    public void theTargetPercentageIsCharged() {
    }

    @Then("the driven distance matches expected range within {int}% tolerance")
    public void theDrivenDistanceMatchesExpectedRangeWithinTolerance(int arg0) {
    }

    @Given("the display is on")
    public void theDisplayIsOn() {
    }

    @When("charging details are shown \\(percentage, kW, time to full)")
    public void chargingDetailsAreShownPercentageKWTimeToFull() {
    }

    @Then("the information is legible")
    public void theInformationIsLegible() {
    }

    @Given("the car is charging")
    public void theCarIsCharging() {
    }

    @When("the charging percentage updates")
    public void theChargingPercentageUpdates() {
    }

    @Then("it only increases")
    public void itOnlyIncreases() {
    }

    @When("it reaches {int}%")
    public void itReaches(int arg0) {
    }

    @Then("a completion message is displayed")
    public void aCompletionMessageIsDisplayed() {
    }

    @And("the charging stops")
    public void theChargingStops() {
    }
}
