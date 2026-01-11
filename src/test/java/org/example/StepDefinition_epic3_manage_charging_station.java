package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.ChargingMode;
import org.example.enums.SessionStatus;
import org.example.enums.StationStatus;
import org.example.enums.StationType;
import org.example.managementClasses.ChargingProcessManager;
import org.example.managementClasses.ChargingLocationManager;
import org.example.managementClasses.StationManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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

    // --- Managers for deletion (and stable station listing) ---
    private final StationManager stationManager = StationManager.getInstance();
    private final ChargingProcessManager processManager = ChargingProcessManager.getInstance();

    // --- Delete scenario state ---
    private long expectedIdFromFeature;          // the id written in the feature (e.g. 2001)
    private long actualStationIdToDelete;        // the real stationId from the created ChargingStation model
    private String stationNameToDelete;          // name for list assertions
    private boolean deletionResult;
    private String deletionConsoleOutput;

    // =========================
    // Create Charging Station
    // =========================

    @Given("a charging location exists with locationId {long}")
    public void aChargingLocationExistsWithLocationId(long arg0) {
        location = new ChargingLocation("Meidling", "Bahnhofsplatz 9, 1120 Wien, Österreich");
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
        if (Objects.equals(arg0, "AC")) {
            type = StationType.AC;
        } else {
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
        // no-op
    }

    @Then("no charging station is created")
    public void noChargingStationIsCreated() {
        station02 = new ChargingStation(locationID, name, type, capacity, pricing);
        assertEquals(0, station02.getStationId());
    }

    // NOTE: your feature says "an error is printed that says ..." in another class,
    // but this step exists here with the typo "massage". Keep it for compatibility.
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

    // This is the step your run says is missing:
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

    // =========================
    // Read Charging Station
    // =========================

    @Given("these charging location exist:")
    public void theseChargingLocationExist(DataTable datatable) {
        List<Map<String, String>> chargingLocations = datatable.asMaps(String.class, String.class);
        for (Map<String, String> locationMap : chargingLocations) {
            locationName = locationMap.get("name");
            locationAddress = locationMap.get("address");

            location = new ChargingLocation(locationName, locationAddress);
            // NOTE: the table contains "stations" column, but your existing code ignores it.
            // That is okay because stations are created in the next step.
        }
    }

    @And("these stations exist:")
    public void theseStationsExist(DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);

        for (Map<String, String> currentStationData : data) {
            name = currentStationData.get("stationName");

            if (Objects.equals(currentStationData.get("type"), "AC")) {
                type = StationType.AC;
            } else {
                type = StationType.DC;
            }

            capacity = Integer.parseInt(currentStationData.get("capacity"));
            pricing = Float.parseFloat(currentStationData.get("pricing"));

            ChargingStation currentStation = new ChargingStation(
                    location.getLocationId(), name, type, capacity, pricing
            );

            if (!Objects.equals(currentStationData.get("status").toUpperCase(), "AVAILABLE")) {
                currentStation.setStatus(StationStatus.CHARGING);
            }

            location.addStation(currentStation);
        }
        ChargingLocationManager.getInstance().getLocation(location.getLocationId()).setPricing(pricing);
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

    // =========================
    // Update Charging Station
    // =========================

    @When("the pricing of the charging station {string} is updated to {float}")
    public void thePricingOfTheChargingStationIsUpdatedTo(String arg0, float arg1) {
        for (int i = 0; i < location.getStations().size(); i++) {
            if (Objects.equals(location.getStations().get(i).getStationName(), arg0)) {
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

        // arg0 is not used by your model directly, but we keep the parameter for matching the step.
    }

    @When("the value {string} is updated to {string}")
    public void theValueIsUpdatedTo(String arg0, String arg1) {
        switch (arg0.trim().toLowerCase()) {
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

    // =========================
    // Delete Charging Station (matches your current feature text)
    // =========================

    @Given("a charging station exists with the id {int}")
    public void aChargingStationExistsWithTheId(int idFromFeature) {
        stationManager.clear();
        processManager.clear();

        expectedIdFromFeature = idFromFeature;

        ChargingLocation loc = new ChargingLocation("TestLocation", "Test Address");
        loc.setLocationId(1001L);
        stationManager.addLocation(loc);

        ChargingStation s = stationManager.createStation(
                1001L,
                "stationDelete_" + idFromFeature,
                StationType.AC,
                145,
                0.30
        );

        // IMPORTANT: StationManager.deleteStationByStationId checks ChargingStation.getStationId(),
        // so we must delete using the REAL stationId from the model, not the "2001" from the feature.
        actualStationIdToDelete = s.getStationId();
        stationNameToDelete = s.getStationName();
    }

    @Given("the charging station has no active charging process")
    public void theChargingStationHasNoActiveChargingProcess() {
        // nothing to do because we cleared processManager and didn't start a process
        // (explicit step kept for feature readability)
    }

    @Given("the charging station is associated with an active charging process")
    public void theChargingStationIsAssociatedWithAnActiveChargingProcess() {
        // create ACTIVE process for the station we created in the previous step
        processManager.startProcess(
                1L,
                actualStationIdToDelete,
                stationNameToDelete,
                ChargingMode.STANDARD,
                10,
                80,
                11,
                60
        );
        // status is ACTIVE by default in your manager
        assertTrue(processManager.hasActiveProcessForStation(actualStationIdToDelete));
    }

    @When("the owner requests deletion of the charging station")
    public void theOwnerRequestsDeletionOfTheChargingStation() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        deletionResult = stationManager.deleteStationByStationId(actualStationIdToDelete, processManager);

        System.setOut(originalOut);
        deletionConsoleOutput = outContent.toString();
    }

    @Then("the charging station with the id {int} is deleted")
    public void theChargingStationWithTheIdIsDeleted(int idFromFeature) {
        // idFromFeature is the textual ID in the feature (e.g. 2001).
        // We assert deletion based on actual deletion behavior.
        assertTrue(deletionResult);

        boolean stillExists = stationManager.getAllStations().stream()
                .anyMatch(s -> s.getStationId() != null && s.getStationId() == actualStationIdToDelete);

        assertFalse(stillExists);
    }

    @Then("the station is removed from the station network")
    public void theStationIsRemovedFromTheStationNetwork() {
        // same semantic as above, but kept as separate step because feature says so
        boolean stillExists = stationManager.getAllStations().stream()
                .anyMatch(s -> s.getStationId() != null && s.getStationId() == actualStationIdToDelete);

        assertFalse(stillExists);
    }

    @Then("no charging station is deleted")
    public void noChargingStationIsDeleted() {
        assertFalse(deletionResult);

        boolean stillExists = stationManager.getAllStations().stream()
                .anyMatch(s -> s.getStationId() != null && s.getStationId() == actualStationIdToDelete);

        assertTrue(stillExists);
    }

    @And("when all charging stations are requested the deleted station no longer appears in the station list")
    public void whenAllChargingStationsAreRequestedTheDeletedStationNoLongerAppearsInTheStationList() {
        String allInfo = stationManager.getAllStationInformation();
        assertFalse(allInfo.contains(stationNameToDelete));
    }
}
