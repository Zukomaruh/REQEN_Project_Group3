package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.StationStatus;
import org.example.enums.StationType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinition_choose_charging_option {

    private ChargingLocation location;
    private List<ChargingStation> stations;
    private ChargingStation updatedStation;
    private ChargingLocation selectedLocation;

    // ---------------------------------------------------------
    // Scenario 1 – Show Charging Locations
    // ---------------------------------------------------------

    @Given("I have an account")
    public void iHaveAnAccount() {

        PricingStrategy pricing = new PricingStrategy(
                1L, 101L, 0.49, 1.00, 0.10,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );

        ChargingStation s1 = new ChargingStation(
                101L, 1L, "Station-A", StationType.DC, 150,
                StationStatus.AVAILABLE, pricing
        );

        ChargingStation s2 = new ChargingStation(
                102L, 1L, "Station-B", StationType.AC, 22,
                StationStatus.MAINTENANCE, pricing
        );

        stations = new ArrayList<>(List.of(s1, s2));

        location = new ChargingLocation(
                1L, "Main Hub", "123 Main St", stations
        );
    }

    @When("I request the information for Charging Locations")
    public void iRequestTheInformationForChargingLocations() {
        stations = location.getStations();
    }

    @Then("I can see all relevant information for each Charging Location")
    public void iCanSeeAllRelevantInformationForEachChargingLocation() {

        assertThat(stations).hasSize(2);

        assertThat(stations.get(0).getInformation())
                .contains("Station-A");

        assertThat(stations.get(1).getInformation())
                .contains("Station-B");
    }

    // ---------------------------------------------------------
    // Scenario 2 – Station information updates
    // ---------------------------------------------------------

    @Given("charging station data changes")
    public void chargingStationDataChanges() {
        updatedStation = stations.get(0);
        updatedStation.updateStatus(StationStatus.CHARGING);
    }

    @When("I request the information for Charging Locations again")
    public void iRequestTheInformationForChargingLocationsAgain() {
        stations = location.getStations();
    }

    @Then("the displayed information is updated")
    public void theDisplayedInformationIsUpdated() {

        assertThat(updatedStation.getInformation())
                .contains("CHARGING");
    }

    // ---------------------------------------------------------
    // Scenario 3 – View station details
    // ---------------------------------------------------------

    @Given("I have chosen a Charging Location")
    public void iHaveChosenAChargingLocation() {
        selectedLocation = location;
    }

    @When("I request information for the Locations Charging Stations")
    public void iRequestInformationForTheLocationsChargingStations() {
        stations = selectedLocation.getStations();
    }

    @Then("all current Charging Station details are displayed")
    public void allCurrentChargingStationDetailsAreDisplayed() {

        ChargingStation s = stations.get(0);

        assertThat(s.getStationName()).isEqualTo("Station-A");
        assertThat(s.getType()).isEqualTo(StationType.DC);
        assertThat(s.getCapacity()).isEqualTo(150);

        // status might be CHARGING if previous scenario was run
        assertThat(s.getStatus()).isIn(StationStatus.AVAILABLE, StationStatus.CHARGING);
    }
}
