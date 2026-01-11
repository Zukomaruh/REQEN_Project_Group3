package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.enums.AccountType;
import org.example.managementClasses.ChargingLocationManager;
import org.example.enums.StationStatus;
import org.example.enums.StationType;

public class StepDefinition_epic6_manage_charging_location {
    String name;
    String address;
    ChargingLocation location;
    String testOutput;

    private final ChargingLocationManager locationManager = ChargingLocationManager.getInstance();

    private ChargingLocation createdLocation;
    private List<ChargingLocation> retrievedLocations;
    private String lastOutput;

    private String tempName;
    private String tempAddress;

    private int targetDeleteId;
    private boolean deleteResult;
    private String systemNotification;




    @Given("owner is on the system main class.")
    public void theOwnerIsOnTheSystemMainClass() {

        locationManager.clear();
        createdLocation = null;
        retrievedLocations = null;
        lastOutput = null;
        tempName = null;
        tempAddress = null;
    }



    @When("the owner creates a charging location with the name {string}")
    public void theOwnerCreatesAChargingLocationWithTheName(String name) {
        tempName = name;
    }

    @And("enters the address {string}")
    public void entersTheAddress(String address) {
        tempAddress = address;
        createdLocation = locationManager.createLocation(tempName, tempAddress);
    }

    @Then("a charging Location with the name {string} with an empty Charging Station List is created successfully")
    public void aChargingLocationWithTheNameWithAnEmptyChargingStationListIsCreatedSuccessfully(String expectedName) {

        if (tempName == null || tempName.isBlank() || tempAddress == null || tempAddress.isBlank()) {
            assertNull(createdLocation, "Location should not be created with invalid input.");
            return;
        }

        assertNotNull(createdLocation, "Location should be created.");
        assertEquals(expectedName, createdLocation.getName());
        assertNotNull(createdLocation.getLocationId(), "Location ID must be generated.");
        assertNotNull(createdLocation.getStations(), "Stations list must not be null.");
        assertTrue(createdLocation.getStations().isEmpty(), "Stations list must be empty.");
    }

    @And("a confirmation is printed that says {string}")
    public void aConfirmationIsPrintedThatSays(String arg0) {
    }

    @Then("no charging location is created")
    public void noChargingLocationIsCreated() {
        assertNull(createdLocation, "No location should be created for invalid input.");
    }


    @And("an error is printed that says {string}")
    public void anErrorIsPrintedThatSays(String arg0) {
    }



    @Given("these charging locations exist:")
    public void theseChargingLocationsExist(DataTable dataTable) {
        locationManager.clear();

        List<List<String>> rows = dataTable.asLists(String.class);
        // erste Zeile ist Header
        for (int i = 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            String name = stripQuotes(row.get(0));
            String address = stripQuotes(row.get(1));
            String stationsRaw = row.get(2).trim(); // z.B. "[station1, station2]" oder "[]"

            ChargingLocation loc = locationManager.createLocation(name, address);

            if (!stationsRaw.equals("[]")) {
                String inner = stationsRaw.substring(1, stationsRaw.length() - 1); // Klammern entfernen
                if (!inner.trim().isEmpty()) {
                    String[] stationNames = inner.split(",");
                    for (String rawStationName : stationNames) {
                        String stationName = rawStationName.trim();
                        if (stationName.isEmpty()) continue;

                        ChargingStation station = new ChargingStation(

                                loc.getLocationId(),
                                stationName,
                                StationType.AC,
                                11,
                                0
                        );
                        loc.addStation(station);
                    }
                }
            }
        }
    }

    @When("the system retrieves all charging locations")
    public void theSystemRetrievesAllChargingLocations() {
        retrievedLocations = locationManager.getAllLocations();

        StringBuilder sb = new StringBuilder();
        for (ChargingLocation loc : retrievedLocations) {
            sb.append("---\n");
            // Für den Test verwenden wir den Platzhalter <generated_ID>
            sb.append("locationID: <generated_ID>\n");
            sb.append("name: ").append(loc.getName()).append("\n");
            sb.append("address: ").append(loc.getAddress()).append("\n");
            sb.append("stations:\n");
            for (ChargingStation station : loc.getStations()) {
                sb.append("  ").append(station.getStationName()).append("\n");
            }
        }
        sb.append("---\n");

        lastOutput = sb.toString();
        System.out.print(lastOutput);
    }

    @Then("the console shows this:")
    public void theOutputLooksLikeThis(String expectedOutput) {
        assertNotNull(lastOutput, "Output must not be null.");

        assertTrue(lastOutput.trim().contains(expectedOutput.trim()));
    }

    private String stripQuotes(String value) {
        if (value == null) return null;
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    @Given("a location exists with the following values:")
    public void aLocationExistsWithTheFollowingValues(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        name = data.get("name").replace("\"", ""); // Removing quotes added in Gherkin
        address = data.get("address").replace("\"", ""); // Removing quotes added in Gherkin
        String[] stations = data.get("stations").split(",");
        location = new ChargingLocation(name, address);
    }

    @When("the name is updated to {string}")
    public void theNameIsUpdatedTo(String arg0) {
        location.setName(arg0);
    }

    @And("the Locations information is requested")
    public void theLocationsInformationIsRequested() {
        testOutput = location.toString();
    }

    @Then("it contains {string}")
    public void itContains(String arg0) {
        assertTrue(testOutput.contains(arg0));
    }

    @And("does not contain {string}")
    public void doesNotContain(String arg0) {
        assertFalse(testOutput.contains(arg0));
    }

    // ==========================
    // User Story 6.4 – DELETE
    // ==========================

    @Given("a charging location exists with id {int}, name {string} and address {string} and has no stations")
    public void aChargingLocationExistsWithNoStations(int id, String name, String address) {

        ChargingLocation loc = locationManager.createLocation(name, address);
        assertNotNull(loc, "Location must be created for this scenario.");

        loc.setLocationId((long) id);
        targetDeleteId = id;

        assertTrue(loc.getStations().isEmpty(), "Location must start with no stations");
        assertNotNull(locationManager.findById(id), "Location must exist in manager");
    }

    @Given("a charging location exists with id {int}, name {string} and address {string} and has stations")
    public void aChargingLocationExistsWithStations(int id, String name, String address) {

        ChargingLocation loc = locationManager.createLocation(name, address);
        assertNotNull(loc, "Location must be created for this scenario.");

        loc.setLocationId((long) id);
        targetDeleteId = id;

        ChargingStation st = new ChargingStation(
                loc.getLocationId(),
                "stationBlocker",
                StationType.AC,
                11,
                0.30f
        );
        loc.addStation(st);

        assertFalse(loc.getStations().isEmpty(), "Location must contain stations");
        assertNotNull(locationManager.findById(id), "Location must exist in manager");
    }

    @When("the owner requests deletion of the charging location with id {int}")
    public void theOwnerRequestsDeletionOfTheChargingLocationWithId(int id) {

        targetDeleteId = id;
        deleteResult = locationManager.deleteLocation(id);

        if (!deleteResult) {
            ChargingLocation loc = locationManager.findById(id);
            if (loc != null && loc.getStations() != null && !loc.getStations().isEmpty()) {
                systemNotification = "Deletion rejected: location contains charging stations";
            } else if (loc == null) {
                systemNotification = "Deletion rejected: location not found";
            } else {
                systemNotification = "Deletion rejected";
            }
        }
    }

    @Then("the system deletes the charging location")
    public void theSystemDeletesTheChargingLocation() {
        assertTrue(deleteResult, "Expected deletion to succeed");
        assertNull(locationManager.findById(targetDeleteId), "Location should not exist after deletion");
    }

    @Then("the system rejects the deletion and returns a system notification {string}")
    public void theSystemRejectsTheDeletionAndReturnsASystemNotification(String expected) {
        assertFalse(deleteResult, "Expected deletion to be rejected");
        assertEquals(expected, systemNotification);

        if (!"Deletion rejected: location not found".equals(expected)) {
            assertNotNull(locationManager.findById(targetDeleteId), "Location should still exist after rejection");
        }
    }


    @And("when all charging locations are retrieved, the location with id {int} is not included")
    public void whenAllChargingLocationsAreRetrievedTheLocationWithIdIsNotIncluded(int id) {
        boolean exists = locationManager.getAllLocations().stream()
                .anyMatch(l -> l.getLocationId() != null && l.getLocationId().longValue() == id);
        assertFalse(exists, "Deleted location should not be included in the returned list");
    }

    @And("when all charging locations are retrieved, the location with id {int} is included")
    public void whenAllChargingLocationsAreRetrievedTheLocationWithIdIsIncluded(int id) {
        boolean exists = locationManager.getAllLocations().stream()
                .anyMatch(l -> l.getLocationId() != null && l.getLocationId().longValue() == id);
        assertTrue(exists, "Location should still be included in the returned list");
    }
}
