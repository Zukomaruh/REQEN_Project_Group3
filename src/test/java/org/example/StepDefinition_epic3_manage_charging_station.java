package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.example.enums.AccountType;
import org.example.enums.StationStatus;
import org.example.enums.StationType;
import org.example.managementClasses.StationManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinition_epic3_manage_charging_station {
    ChargingLocation location;
    ChargingStation station;
    ChargingStation station02;
    String name;
    long locationID;
    StationType type;
    int capacity;
    float pricing;
    StationStatus status;
    String locationName;
    String locationAddress;
    List<ChargingStation> locationStations;
    String expectedOutput;

    @Given("a charging location exists with locationId {long}")
    public void aChargingLocationExistsWithLocationId(long arg0) {
        location  = new ChargingLocation("Meidling", "Bahnhofsplatz 9, 1120 Wien, Österreich");
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

    @Then("a new charging station with the name {string}, and without and locationID is created.")
    public void aNewChargingStationWithTheNameAndWithoutAndLocationIDIsCreated(String arg0) {
        station = new ChargingStation(name, type, capacity, pricing);
        assertEquals(arg0, station.getStationName());
        assertEquals(StationStatus.AVAILABLE, station.getStatus());
    }

    @And("the station is added to the List of the location with locationId {int}")
    public void theStationIsAddedToTheListOfTheLocationWithLocationId(int arg0) {
        assertTrue(location.getStations().contains(station));
    }

    @And("a station confirmation is printed that says {string}")
    public void aStationConfirmationIsPrintedThatSays(String arg0) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ChargingStation(locationID, name, type, capacity, pricing);
        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains(arg0));
    }

    @Given("the owner is on the system")
    public void theOwnerIsOnTheSystem() {
    }

    @Then("no charging station is created")
    public void noChargingStationIsCreated() {
        station02 = new ChargingStation(locationID, name, type, capacity, pricing);
        assertEquals(0, station02.getStationId());
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
    public void theseChargingLocationExist(DataTable datatable) {
        List<Map<String, String>> chargingLocations = datatable.asMaps(String.class, String.class);
        for (Map<String, String> locationMap : chargingLocations) {
            locationName = locationMap.get("name");
            locationAddress = locationMap.get("address");
            //String stations = locationMap.get("stations");
            location = new ChargingLocation(locationName, locationAddress);
        }

    }

    @And("these stations exist:")
    public void theseStationsExist(DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class); // <-- Ihre Änderung

        // Die Schleife muss nun über die Elemente der Liste iterieren,
        // nicht nur hartkodiert 2 Mal laufen.
        //AKTUELle Zeile abrufen
        for (Map<String, String> currentStationData : data) {
            // Alle 'data.get(...)' Aufrufe müssen auf 'currentStationData.get(...)' geändert werden.
            name = currentStationData.get("stationName");

            if (Objects.equals(currentStationData.get("type"), "AC")) {
                type = StationType.AC;
            } else {
                type = StationType.DC;
            }
            capacity = Integer.parseInt(currentStationData.get("capacity"));

            // ... (Rest der Logik mit currentStationData)
            pricing = Float.parseFloat(currentStationData.get("pricing"));
            // Erstellen der Stationen
            ChargingStation currentStation = new ChargingStation(
                    location.getLocationId(), name, type, capacity, pricing
            );
            if(!Objects.equals(currentStationData.get("status").toUpperCase(), "AVAILABLE")){
               currentStation.setStatus(StationStatus.CHARGING);
            }

            // Hinzufügen der Stationen
            location.addStation(currentStation);

            // Die ursprünglichen Variablen 'station' und 'station02' werden hier
            // ersetzt, da die Liste direkt iteriert wird.

        }
    }

    @When("the customer requests the charging stations information")
    public void theCustomerRequestsTheChargingStationsInformation() {
        locationStations = location.getStations();
        StringBuilder result = new StringBuilder();
        for (ChargingStation locationStation : locationStations) {
            result.append(locationStation.getInformation());
        }
        expectedOutput = result.toString();
    }

    @Then("the output looks like this:")
    public void theOutputLooksLikeThis(String arg0) {
        assertEquals(arg0, expectedOutput);
    }

    @When("the pricing of the charging station {string} is updated to {float}")
    public void thePricingOfTheChargingStationIsUpdatedTo(String arg0, float arg1) {
        for (int i = 0; i < location.getStations().size(); i++){
            if(Objects.equals(location.getStations().get(i).getStationName(), arg0)){
                location.getStations().get(i).setPricing(arg1);
            }
        }
    }

    @And("the information is requested")
    public void theInformationIsRequested() {
        locationStations = location.getStations();
        StringBuilder result = new StringBuilder();
        for (ChargingStation locationStation : locationStations) {
            result.append(locationStation.getInformation());
        }
        expectedOutput = result.toString();
    }

    @And("a station hint message is printed that says {string}")
    public void aStationHintMessageIsPrintedThatSays(String arg0) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new ChargingStation(name, type, capacity, pricing);
        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains(arg0));
    }

    @Given("an charging station exists with the id {long} and these values:")
    public void anChargingStationExistsWithTheIdAntTheseValues(long arg0, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);
        name = row.get("stationName");
        type = StationType.valueOf(row.get("type"));
        capacity = Integer.parseInt(row.get("capacity"));
        status = StationStatus.valueOf(row.get("status"));
        pricing = Float.parseFloat(row.get("pricing"));
        station = new ChargingStation(name, type, capacity, pricing);
        station.setStatus(status);
    }

    @When("the value {string} is updated to {string}")
    public void theValueIsUpdatedTo(String arg0, String arg1) {
        switch (arg0.trim().toLowerCase()){
            case "stationname":
                name = arg1;
                station.setStationName(name);
                break;
            case "type":
                type = StationType.valueOf(arg1);
                station.setType(type);
                break;
            case "capacity":
                capacity = Integer.parseInt(arg1);
                station.setCapacity(capacity);
                break;
            case "status":
                status = StationStatus.valueOf(arg1);
                station.setStatus(status);
                break;
            case "pricing":
                pricing = Float.parseFloat(arg1);
                station.setPricing(pricing);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @And("the updated station value is requested")
    public void theUpdatedStationValueIsRequested() {
        expectedOutput = station.getInformation();
    }

    @Then("the output contains the station value {string}")
    public void theOutputContainsTheStationValue(String arg0) {
        assertTrue(expectedOutput.contains(arg0));
    }
}
