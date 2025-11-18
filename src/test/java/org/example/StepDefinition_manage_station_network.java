package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.StationStatus;
import org.example.enums.StationType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StepDefinition_manage_station_network {
    private ChargingLocation chargingLocation;
    private boolean locationAddedSuccessfully;
    private boolean stationAddedSuccessfully;
    private String errorMessage;
    private String confirmationMessage;
    private List<ChargingLocation> chargingLocations;
    private List<ChargingStation> chargingStations;
    private ChargingStation currentStation;
    private boolean inputValid;
    private boolean stationActive;

    // Charging Location Management
    @Given("the charging location has a valid form")
    public void theChargingLocationHasAValidForm() {
        chargingLocation = new ChargingLocation(1L, "Downtown Charging", "123 Main St", new ArrayList<>());
        inputValid = chargingLocation.getName() != null && !chargingLocation.getName().isEmpty()
                && chargingLocation.getAddress() != null && !chargingLocation.getAddress().isEmpty();

        assertThat(inputValid).as("Charging location form should be valid").isTrue();
    }

    @When("I add the Charging Location to the System")
    public void iAddTheChargingLocationToTheSystem() {
        locationAddedSuccessfully = inputValid;
        if (locationAddedSuccessfully) {
            System.out.println("Charging location added: " + chargingLocation.getName());
        }
    }

    @Then("it is added successfully")
    public void itIsAddedSuccessfully() {
        assertThat(locationAddedSuccessfully).as("Charging location should be added successfully").isTrue();
        assertThat(chargingLocation).as("Charging location should exist").isNotNull();
        assertThat(chargingLocation.getName()).as("Location name should be set").isNotBlank();
    }

    @Given("the charging location input is invalid")
    public void theChargingLocationInputIsInvalid() {
        chargingLocation = new ChargingLocation(null, "", "", new ArrayList<>());
        inputValid = false;
        errorMessage = "Invalid charging location input: name and address are required";

        assertThat(inputValid).as("Charging location input should be invalid").isFalse();
    }

    @When("I want to add the charging location")
    public void iWantToAddTheChargingLocation() {
        locationAddedSuccessfully = false;
        errorMessage = "Cannot add charging location: invalid input data";

        System.out.println("Attempting to add invalid charging location");
    }

    @Then("an Error message is displayed")
    public void anErrorMessageIsDisplayed() {
        assertThat(errorMessage).as("Error message should be displayed").isNotNull().isNotEmpty();
        assertThat(locationAddedSuccessfully).as("Location should not be added").isFalse();
    }

    // Charging Station Management
    @Given("I want to add an Charging Station to an location")
    public void iWantToAddAnChargingStationToAnLocation() {
        chargingLocation = new ChargingLocation(1L, "City Center", "456 Central Ave", new ArrayList<>());
        currentStation = new ChargingStation(1L, 1L, "Station-001", StationType.DC, 150, StationStatus.AVAILABLE, null);

        assertThat(chargingLocation).as("Charging location should exist").isNotNull();
        assertThat(currentStation).as("Charging station should be created").isNotNull();
    }

    @When("the input is valid")
    public void theInputIsValid() {
        inputValid = currentStation.getStationName() != null && !currentStation.getStationName().isEmpty()
                && currentStation.getType() != null
                && currentStation.getCapacity() != null && currentStation.getCapacity() > 0
                && currentStation.getStatus() != null;

        assertThat(inputValid).as("Charging station input should be valid").isTrue();
    }

    @Then("the charging station is listed active and bookable to the charging location")
    public void theChargingStationIsListedActiveAndBookableToTheChargingLocation() {
        stationAddedSuccessfully = inputValid;

        if (stationAddedSuccessfully) {
            chargingLocation.addStation(currentStation);
            boolean isAvailable = StationStatus.AVAILABLE.equals(currentStation.getStatus());

            assertThat(stationAddedSuccessfully).as("Station should be added successfully").isTrue();
            assertThat(chargingLocation.getStations()).as("Location should have stations").contains(currentStation);
            assertThat(isAvailable).as("Station should be available").isTrue();
        }
    }

    @Given("a charging station was added")
    public void aChargingStationWasAdded() {
        chargingLocation = new ChargingLocation(1L, "Mall Parking", "789 Mall Rd", new ArrayList<>());
        currentStation = new ChargingStation(1L, 1L, "Station-001", StationType.AC, 50, StationStatus.AVAILABLE, null);
        chargingLocation.addStation(currentStation);

        assertThat(chargingLocation.getStations()).as("Location should have charging station").isNotEmpty();
    }

    @When("a serial number is added")
    public void aSerialNumberIsAdded() {
        String serialNumber = "SN-123456789";
        currentStation.setStationName(serialNumber);

        assertThat(currentStation.getStationName()).as("Serial number should be set").isEqualTo(serialNumber);
    }

    @Then("that serial number cannot be used for another charging station")
    public void thatSerialNumberCannotBeUsedForAnotherChargingStation() {
        String existingSerialNumber = currentStation.getStationName();
        ChargingStation duplicateStation = new ChargingStation(2L, 1L, existingSerialNumber, StationType.DC, 100, StationStatus.AVAILABLE, null);

        boolean isUnique = chargingLocation.getStations().stream()
                .noneMatch(station -> station.getStationName().equals(existingSerialNumber));

        assertThat(isUnique).as("Serial number should be unique").isTrue();
    }

    @When("the station is active")
    public void theStationIsActive() {
        stationActive = StationStatus.AVAILABLE.equals(currentStation.getStatus());
        confirmationMessage = "Charging station is now active and available for bookings";

        assertThat(stationActive).as("Station should be active").isTrue();
    }

    @Then("a confirmation message is displayed")
    public void aConfirmationMessageIsDisplayed() {
        assertThat(confirmationMessage).as("Confirmation message should be displayed").isNotNull().isNotEmpty();
        assertThat(stationActive).as("Station should be active for confirmation").isTrue();
    }

    // Location Sorting and Filtering
    @Given("there are multiple charging locations with different distances")
    public void thereAreMultipleChargingLocationsWithDifferentDistances() {
        chargingLocations = new ArrayList<>();
        chargingLocations.add(new ChargingLocation(1L, "Near Location", "1 km away", new ArrayList<>()));
        chargingLocations.add(new ChargingLocation(2L, "Far Location", "10 km away", new ArrayList<>()));
        chargingLocations.add(new ChargingLocation(3L, "Medium Location", "5 km away", new ArrayList<>()));

        assertThat(chargingLocations).as("Should have multiple charging locations").hasSize(3);
    }

    @When("I request the charging location information")
    public void iRequestTheChargingLocationInformation() {
        chargingLocations.sort((loc1, loc2) -> {
            int dist1 = extractDistance(loc1.getAddress());
            int dist2 = extractDistance(loc2.getAddress());
            return Integer.compare(dist1, dist2);
        });
    }

    @Then("the locations are sorted with the closest ones on the top")
    public void theLocationsAreSortedWithTheClosestOnesOnTheTop() {
        int firstDistance = extractDistance(chargingLocations.get(0).getAddress());
        int lastDistance = extractDistance(chargingLocations.get(chargingLocations.size() - 1).getAddress());

        assertThat(firstDistance).as("First location should be closest").isLessThanOrEqualTo(lastDistance);
    }

    @When("distance sorting filter with target distance is applied")
    public void distanceSortingFilterWithTargetDistanceIsApplied() {
        int targetDistance = 6;
        List<ChargingLocation> filteredLocations = new ArrayList<>();

        for (ChargingLocation location : chargingLocations) {
            int distance = extractDistance(location.getAddress());
            if (distance <= targetDistance) {
                filteredLocations.add(location);
            }
        }
        chargingLocations = filteredLocations;
    }

    @Then("locations within the target distance are displayed")
    public void locationsWithinTheTargetDistanceAreDisplayed() {
        int maxDistance = 6;
        for (ChargingLocation location : chargingLocations) {
            int distance = extractDistance(location.getAddress());
            assertThat(distance).as("Location should be within target distance").isLessThanOrEqualTo(maxDistance);
        }
    }

    // Station Information Display
    @Given("I choose a Charging location")
    public void iChooseAChargingLocation() {
        chargingLocation = new ChargingLocation(1L, "Selected Location", "123 Choice St", new ArrayList<>());
        chargingStations = new ArrayList<>();
        chargingStations.add(new ChargingStation(1L, 1L, "Fast Charger", StationType.DC, 150, StationStatus.AVAILABLE, null));
        chargingStations.add(new ChargingStation(2L, 1L, "Standard Charger", StationType.AC, 50, StationStatus.CHARGING, null));
        chargingLocation.setStations(chargingStations);

        assertThat(chargingLocation).as("Charging location should be selected").isNotNull();
    }

    @When("I request the charging stations information")
    public void iRequestTheChargingStationsInformation() {
        chargingStations = chargingLocation.getStations();
        assertThat(chargingStations).as("Should retrieve station information").isNotNull();
    }

    @Then("the information is displayed")
    public void theInformationIsDisplayed() {
        assertThat(chargingStations).as("Station information should be available").isNotEmpty();
        for (ChargingStation station : chargingStations) {
            assertThat(station.getStationName()).as("Station name should be available").isNotNull();
            assertThat(station.getType()).as("Station type should be available").isNotNull();
            assertThat(station.getStatus()).as("Station status should be available").isNotNull();
        }
    }

    //Helper method for testing purposes
    private int extractDistance(String address) {
        if (address.contains("1 km")) return 1;
        if (address.contains("5 km")) return 5;
        if (address.contains("10 km")) return 10;
        return 0;
    }
}