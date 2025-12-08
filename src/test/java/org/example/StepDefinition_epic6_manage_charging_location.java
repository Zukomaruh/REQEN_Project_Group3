package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.example.managementClasses.ChargingLocationManager;
import org.example.enums.StationStatus;
import org.example.enums.StationType;

public class StepDefinition_epic6_manage_charging_location {

    private final ChargingLocationManager locationManager = ChargingLocationManager.getInstance();

    private ChargingLocation createdLocation;
    private List<ChargingLocation> retrievedLocations;
    private String lastOutput;

    private String tempName;
    private String tempAddress;

    // ============================
    // BACKGROUND
    // ============================

    @Given("the owner is on the system main class.")
    public void theOwnerIsOnTheSystemMainClass() {
        // Test-Reset vor jedem Scenario
        locationManager.clear();
        createdLocation = null;
        retrievedLocations = null;
        lastOutput = null;
        tempName = null;
        tempAddress = null;
    }

    // ============================
    // US 6.1 – create charging location
    // ============================

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
        // Wenn die Eingabe ungültig ist, darf kein Objekt existieren
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

    @Then("no charging location is created")
    public void noChargingLocationIsCreated() {
        assertNull(createdLocation, "No location should be created for invalid input.");
    }

    // ============================
    // US 6.2 – read charging locations
    // ============================

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
                                null,                        // stationId – für Test nicht relevant
                                loc.getLocationId(),
                                stationName,
                                StationType.AC,              // Dummy-Werte für den Test
                                11,
                                StationStatus.AVAILABLE,
                                null                         // Pricing – für diesen Test egal
                        );
                        loc.getStations().add(station);
                    }
                }
            }
        }
    }

    @When("the system retrieves all charging locations")
    public void theSystemRetrievesAllChargingLocations() {
        retrievedLocations = locationManager.readAllLocations();

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

    @Then("the output looks like this:")
    public void theOutputLooksLikeThis(String expectedOutput) {
        assertNotNull(lastOutput, "Output must not be null.");
        assertEquals(expectedOutput.trim(), lastOutput.trim());
    }

    // ============================
    // Hilfsmethode
    // ============================

    private String stripQuotes(String value) {
        if (value == null) return null;
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
