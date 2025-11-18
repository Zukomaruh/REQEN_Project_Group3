package org.example;

import org.example.enums.StationStatus;
import org.example.enums.StationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChargingStationAndLocationTest {

    private ChargingStation station;
    private ChargingLocation location;
    private List<ChargingStation> stations;

    @BeforeEach
    void setUp() {
        stations = new ArrayList<>();
        station = new ChargingStation(1L, 100L, "Station A", StationType.AC, 50, StationStatus.AVAILABLE, null);
        location = new ChargingLocation(100L, "Location 1", "123 Main St", stations);
    }

    // ---------------- ChargingStation tests ----------------
    @Test
    void testStationGettersAndSetters() {
        assertThat(station.getStationId()).isEqualTo(1L);
        assertThat(station.getLocationId()).isEqualTo(100L);
        assertThat(station.getStationName()).isEqualTo("Station A");
        assertThat(station.getType()).isEqualTo(StationType.AC);
        assertThat(station.getCapacity()).isEqualTo(50);
        assertThat(station.getStatus()).isEqualTo(StationStatus.AVAILABLE);

        station.setStationName("Station B");
        station.setCapacity(75);
        station.setStatus(StationStatus.CHARGING);

        assertThat(station.getStationName()).isEqualTo("Station B");
        assertThat(station.getCapacity()).isEqualTo(75);
        assertThat(station.getStatus()).isEqualTo(StationStatus.CHARGING);
    }

    @Test
    void testStationInformation() {
        String info = station.getInformation();
        assertThat(info).contains("StationID: 1")
                .contains("LocationID: 100")
                .contains("Name: Station A")
                .contains("Type: AC")
                .contains("Capacity: 50")
                .contains("Status: AVAILABLE");
    }

    @Test
    void testStationPlaceholderMethods() {
        // Current methods return default values
        assertThat(station.isAvailable()).isFalse();
        assertThat(station.isUnderMaintenance()).isFalse();
        assertThat(station.supportsType(StationType.DC)).isFalse();
        assertThat(station.calculateCost(10.0, Duration.ofHours(1))).isNull();

        station.updateStatus(StationStatus.MAINTENANCE);
        // Does nothing currently; just ensure it compiles
    }

    // ---------------- ChargingLocation tests ----------------
    @Test
    void testLocationGettersAndSetters() {
        assertThat(location.getLocationId()).isEqualTo(100L);
        assertThat(location.getName()).isEqualTo("Location 1");
        assertThat(location.getAddress()).isEqualTo("123 Main St");
        assertThat(location.getStations()).isEqualTo(stations);

        location.setName("New Location");
        location.setAddress("456 Elm St");

        assertThat(location.getName()).isEqualTo("New Location");
        assertThat(location.getAddress()).isEqualTo("456 Elm St");
    }

    @Test
    void testAddStation() {
        assertThat(location.getStations()).isEmpty();
        location.addStation(station);
        assertThat(location.getStations()).hasSize(1).contains(station);
    }

    @Test
    void testLocationPlaceholderMethods() {
        // Methods return default values
        assertThat(location.getTotalCapacity()).isEqualTo(0);
        assertThat(location.getAvailableCapacity()).isEqualTo(0);
        assertThat(location.getAvailableStations()).isNull();
    }
}
