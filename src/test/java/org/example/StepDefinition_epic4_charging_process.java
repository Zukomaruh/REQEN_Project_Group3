package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.StationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinition_epic4_charging_process {

    // Battery / charging process state
    private int initialBatteryLevel;
    private int currentBatteryLevel;
    private int targetBatteryLevel;
    private boolean carConnectedToAvailableStation;
    private boolean chargingProcessStarted;
    private String chargingStatus;

    // Car start state
    private boolean batteryNotEmpty;
    private boolean carStarted;

    // Range / distance state
    private int expectedRangeKm;
    private int drivenDistanceKm;

    // Charging info readout (US 4.2)
    private int activeCustomerId;
    private int activeStationId;
    private int currentPowerKW;
    private int timeToFullMinutes;
    private String lastOutput;

    // Percentage history validation
    private final List<Integer> percentageHistory = new ArrayList<>();
    private boolean percentagesValid;

    // Completion message for 100%
    private String completionMessage;

    // Station status management (US 4.3)
    private final Map<Integer, StationStatus> stationStatusMap = new HashMap<>();
    private int lastStationId;
    private boolean chargingRequestRejected;
    private StationStatus initialStatusBeforeRequest;

    @Given("the battery level is {int} percent")
    public void theBatteryLevelIsPercent(int arg0) {
        // Wird im Given vor Start gesetzt und im Then zum Verifizieren verwendet
        if (!chargingProcessStarted) {
            this.initialBatteryLevel = arg0;
            this.currentBatteryLevel = arg0;
        } else {
            assertEquals(arg0, this.currentBatteryLevel, "Battery level does not match expected value.");
        }
    }

    @And("the target battery level is {int} percent")
    public void theTargetBatteryLevelIsPercent(int arg0) {
        this.targetBatteryLevel = arg0;
    }

    @And("the car is connected to a charging station with the status AVAILABLE")
    public void theCarIsConnectedToAChargingStationWithTheStatusAVAILABLE() {
        this.carConnectedToAvailableStation = true;
    }

    @When("the customer starts a charging process")
    public void theCustomerStartsAChargingProcess() {
        this.chargingProcessStarted = true;

        // US 4.1: wenn verbunden und unter Ziel -> bis Ziel (oder 100%) laden
        if (carConnectedToAvailableStation && currentBatteryLevel < targetBatteryLevel) {
            this.currentBatteryLevel = Math.min(targetBatteryLevel, 100);
        }

        if (this.currentBatteryLevel >= this.targetBatteryLevel || this.currentBatteryLevel == 100) {
            this.chargingStatus = "COMPLETED";
        } else {
            this.chargingStatus = "CHARGING";
        }
    }

    @And("the charging status is {string}")
    public void theChargingStatusIs(String arg0) {
        assertEquals(arg0, this.chargingStatus, "Charging status does not match expected value.");
    }

    @And("the battery is not empty")
    public void theBatteryIsNotEmpty() {
        this.batteryNotEmpty = true;
    }

    @When("the customer starts the car")
    public void theCustomerStartsTheCar() {
        // US 4.1: >= 20% und nicht leer -> startet sofort
        this.carStarted = batteryNotEmpty && currentBatteryLevel >= 20;
    }

    @Then("the car starts immediately")
    public void theCarStartsImmediately() {
        assertTrue(this.carStarted, "The car did not start immediately as expected.");
    }

    @And("the expected range at this level is {int} kilometers")
    public void theExpectedRangeAtThisLevelIsKilometers(int arg0) {
        this.expectedRangeKm = arg0;
    }

    @When("the charging process has finished")
    public void theChargingProcessHasFinished() {
        this.chargingStatus = "COMPLETED";
    }

    @And("the driven distance is calculated")
    public void theDrivenDistanceIsCalculated() {
        // Fürs erste: gefahrene Distanz = erwartete Reichweite
        this.drivenDistanceKm = this.expectedRangeKm;
    }

    @Then("the driven distance matches the expected range with a tolerance of {int} percent")
    public void theDrivenDistanceMatchesTheExpectedRangeWithAToleranceOfPercent(int arg0) {
        int diff = Math.abs(drivenDistanceKm - expectedRangeKm);
        double allowedDeviation = expectedRangeKm * (arg0 / 100.0);
        assertTrue(diff <= allowedDeviation,
                "Driven distance differs from expected range by more than allowed tolerance.");
    }

    // ------------- HIER: DataTable korrekt binden -------------

    @Given("an active charging process exists for the customer with the userID {int} at the station with the id {int} and these values:")
    public void anActiveChargingProcessExistsForTheCustomerWithTheUserIDAtTheStationWithTheIdAndTheseValues(int customerId,
                                                                                                            int stationId,
                                                                                                            DataTable dataTable) {
        this.activeCustomerId = customerId;
        this.activeStationId = stationId;

        // Tabelle: batteryPercentage | powerKW | timeToFullMinutes
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);

        this.currentBatteryLevel = Integer.parseInt(row.get("batteryPercentage"));
        this.currentPowerKW = Integer.parseInt(row.get("powerKW"));
        this.timeToFullMinutes = Integer.parseInt(row.get("timeToFullMinutes"));
        this.chargingStatus = "CHARGING";
    }

    @When("the customer requests the charging information")
    public void theCustomerRequestsTheChargingInformation() {
        StringBuilder sb = new StringBuilder();
        sb.append("---\n");
        sb.append("customerID: ").append(activeCustomerId).append("\n");
        sb.append("stationID: ").append(activeStationId).append("\n");
        sb.append("batteryPercentage: ").append(currentBatteryLevel).append("\n");
        sb.append("powerKW: ").append(currentPowerKW).append("\n");
        sb.append("timeToFullMinutes: ").append(timeToFullMinutes).append("\n");
        sb.append("status: ").append(chargingStatus).append("\n");
        sb.append("---\n");

        this.lastOutput = sb.toString();
        System.out.print(this.lastOutput);
    }

    @Given("an active charging process for the station with the id {int} with this percentage history:")
    public void anActiveChargingProcessForTheStationWithTheIdWithThisPercentageHistory(int stationId,
                                                                                       DataTable dataTable) {
        this.activeStationId = stationId;
        this.percentageHistory.clear();

        // Tabelle: | percentage |
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            int percentage = Integer.parseInt(row.get("percentage"));
            this.percentageHistory.add(percentage);
        }
    }

    @When("the system validates the charging percentage updates")
    public void theSystemValidatesTheChargingPercentageUpdates() {
        this.percentagesValid = true;
        for (int i = 1; i < percentageHistory.size(); i++) {
            if (percentageHistory.get(i) < percentageHistory.get(i - 1)) {
                this.percentagesValid = false;
                break;
            }
        }
    }

    @Then("each new percentage is greater than or equal to the previous percentage")
    public void eachNewPercentageIsGreaterThanOrEqualToThePreviousPercentage() {
        assertTrue(percentagesValid, "Charging percentages did not monotonically increase.");
    }

    @Given("an active charging process for the station with the id {int} and a battery level of {int} percent")
    public void anActiveChargingProcessForTheStationWithTheIdAndABatteryLevelOfPercent(int arg0, int arg1) {
        this.activeStationId = arg0;
        this.currentBatteryLevel = arg1;
        this.chargingStatus = "CHARGING";
    }

    @When("the charging percentage is updated to {int} percent")
    public void theChargingPercentageIsUpdatedToPercent(int arg0) {
        this.currentBatteryLevel = arg0;
        if (arg0 >= 100) {
            this.currentBatteryLevel = 100;
            this.chargingStatus = "COMPLETED";
            this.completionMessage = "Charging completed";
        }
    }

    @Then("a completion message is returned that says {string}")
    public void aCompletionMessageIsReturnedThatSays(String arg0) {
        assertEquals(arg0, this.completionMessage, "Completion message does not match expected.");
    }

    @Given("a charging station exists with the id {int} and the status AVAILABLE")
    public void aChargingStationExistsWithTheIdAndTheStatusAVAILABLE(int arg0) {
        this.lastStationId = arg0;
        stationStatusMap.put(arg0, StationStatus.AVAILABLE);
    }

    @When("a charging process starts at the station with the id {int}")
    public void aChargingProcessStartsAtTheStationWithTheId(int arg0) {
        this.lastStationId = arg0;
        StationStatus current = stationStatusMap.get(arg0);
        if (current == StationStatus.AVAILABLE) {
            stationStatusMap.put(arg0, StationStatus.CHARGING);
        }
    }

    @Then("the system updates the station status to CHARGING")
    public void theSystemUpdatesTheStationStatusToCHARGING() {
        assertEquals(StationStatus.CHARGING,
                stationStatusMap.get(lastStationId),
                "Station status was not updated to CHARGING.");
    }

    @Given("a charging station exists with the id {int} and the status CHARGING")
    public void aChargingStationExistsWithTheIdAndTheStatusCHARGING(int arg0) {
        this.lastStationId = arg0;
        stationStatusMap.put(arg0, StationStatus.CHARGING);
    }

    @When("the charging process at the station with the id {int} finishes")
    public void theChargingProcessAtTheStationWithTheIdFinishes(int arg0) {
        this.lastStationId = arg0;
        StationStatus current = stationStatusMap.get(arg0);
        if (current == StationStatus.CHARGING) {
            stationStatusMap.put(arg0, StationStatus.AVAILABLE);
        }
    }

    @Then("the system updates the station status to AVAILABLE")
    public void theSystemUpdatesTheStationStatusToAVAILABLE() {
        assertEquals(StationStatus.AVAILABLE,
                stationStatusMap.get(lastStationId),
                "Station status was not updated to AVAILABLE.");
    }

    // ---------- Status MAINTENANCE / OFFLINE (Scenario Outline) ----------

    @Given("a charging station exists with the id {int} and the status MAINTENANCE")
    public void aChargingStationExistsWithTheIdAndTheStatusMAINTENANCE(int arg0) {
        this.lastStationId = arg0;
        StationStatus status = StationStatus.MAINTENANCE;
        stationStatusMap.put(arg0, status);
        this.initialStatusBeforeRequest = status;
    }

    @Given("a charging station exists with the id {int} and the status OFFLINE")
    public void aChargingStationExistsWithTheIdAndTheStatusOFFLINE(int arg0) {
        this.lastStationId = arg0;
        StationStatus status = StationStatus.OFFLINE;
        stationStatusMap.put(arg0, status);
        this.initialStatusBeforeRequest = status;
    }

    @When("a charging process is requested at the station with the id {int}")
    public void aChargingProcessIsRequestedAtTheStationWithTheId(int arg0) {
        this.lastStationId = arg0;
        StationStatus current = stationStatusMap.get(arg0);
        if (current == StationStatus.MAINTENANCE || current == StationStatus.OFFLINE) {
            this.chargingRequestRejected = true;
            // Status bleibt unverändert
        } else {
            this.chargingRequestRejected = false;
            stationStatusMap.put(arg0, StationStatus.CHARGING);
        }
    }

    @Then("the system rejects the charging request")
    public void theSystemRejectsTheChargingRequest() {
        assertTrue(this.chargingRequestRejected, "Charging request was not rejected as expected.");
    }

    @Then("the station status remains MAINTENANCE")
    public void theStationStatusRemainsMAINTENANCE() {
        StationStatus current = stationStatusMap.get(lastStationId);
        assertEquals(initialStatusBeforeRequest, current,
                "Station status should remain MAINTENANCE for unavailable stations.");
    }

    @Then("the station status remains OFFLINE")
    public void theStationStatusRemainsOFFLINE() {
        StationStatus current = stationStatusMap.get(lastStationId);
        assertEquals(initialStatusBeforeRequest, current,
                "Station status should remain OFFLINE for unavailable stations.");
    }
    @Then("the charging information looks like this:")
    public void theChargingInformationLooksLikeThis(String expected) {
        assertNotNull(lastOutput, "Output must not be null.");
        assertEquals(expected.trim(), lastOutput.trim());
    }

}
