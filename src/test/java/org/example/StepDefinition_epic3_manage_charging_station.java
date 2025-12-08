package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.AccountType;
import org.example.enums.StationStatus;
import org.example.enums.StationType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinition_epic3_manage_charging_station {
    ChargingLocation location = new ChargingLocation("Meidling", "Bahnhofsplatz 9, 1120 Wien, Österreich");
    ChargingStation station;
    String name;
    long locationID;
    StationType type;
    int capacity;
    float pricing;
    StationStatus status;
    String locationName;
    String locationAddress;
    List<ChargingStation> locationStations;

    @Given("a charging location exists with locationId {long}")
    public void aChargingLocationExistsWithLocationId(long arg0) {
        location.setLocationId(arg0);
    }

    @When("the owner creates a charging station with the name {string}")
    public void theOwnerCreatesAChargingStationWithTheName(String arg0) {
        name = arg0;
    }

    @And("the locationID {long}")
    public void theLocationID(long arg0) {
        locationID = arg0;
    }

    @And("the type {string}")
    public void theType(String arg0) {
        if(Objects.equals(arg0, "AC")){
            type = StationType.AC;
        }else {
            type = StationType.DC;
        }
    }

    @And("the capacity {int}")
    public void theCapacity(int arg0) {
        capacity = arg0;
    }

    @And("the pricing of {float}")
    public void thePricingOf(float arg0) {
        pricing = arg0;
    }

    @Then("a new charging station with the name {string}, an unique stationID and the status AVAILABLE is created.")
    public void aNewChargingStationWithTheNameAnUniqueStationIDAndTheStatusAVAILABLEIsCreated(String arg0) {
        station = new ChargingStation(locationID, name, type, capacity, pricing);
        assertEquals(arg0, station.getStationName());
        assertEquals(StationStatus.AVAILABLE, station.getStatus());
    }

    @And("the station is added to the List of the location with locationId {int}")
    public void theStationIsAddedToTheListOfTheLocationWithLocationId(int arg0) {
        assertTrue(location.getStations().contains(station));
    }

    @Given("the owner is on the system")
    public void theOwnerIsOnTheSystem() {
    }

    @Then("no charging station is created")
    public void noChargingStationIsCreated() {
        station = new ChargingStation(locationID, name, type, capacity, pricing);
        assertEquals(0, station.getStationId());
    }

    @And("an error massage is printed that says {string}")
    public void anErrorMassageIsPrintedThatSays(String arg0) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ChargingStation(locationID, name, type, capacity, pricing);
        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains(arg0));
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
