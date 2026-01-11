package org.example;

import org.example.enums.StationStatus;
import org.example.enums.StationType;
import org.example.managementClasses.ChargingLocationManager;

import java.time.Duration;
import java.util.List;

public class ChargingStation {
    private long stationId;
    private long locationId;
    private String stationName;
    private StationType type; // AC, DC
    private int capacity; // kW
    private StationStatus status; // AVAILABLE, CHARGING, MAINTENANCE, OFFLINE
    private float pricing;

    public ChargingStation(long locationId, String stationName, StationType type, Integer capacity, float pricing) {
        if(!stationName.isEmpty() && capacity < 350) {
            this.stationId = System.currentTimeMillis();
            this.locationId = locationId;
            List<ChargingLocation> locationStations = ChargingLocationManager.getInstance().getAllLocations();
            for (ChargingLocation locationStation : locationStations) {
                if (locationStation.getLocationId() == locationId) {
                    locationStation.addStation(this);
                    break;
                }
            }
            this.stationName = stationName;
            this.type = type;
            this.capacity = capacity;
            this.status = StationStatus.AVAILABLE;
            this.pricing = ChargingLocationManager.getInstance().getLocation(locationId).getPricing();
            System.out.println("Charging station created successfully");
        }else{
            System.out.println("Creation failed! Please enter valid Station settings");
        }
    }

    public ChargingStation(String stationName, StationType type, Integer capacity, float pricing) {
        if(!stationName.isEmpty() && capacity < 350 && pricing < 1.0) {
            this.stationId = System.currentTimeMillis();
            this.stationName = stationName;
            this.type = type;
            this.capacity = capacity;
            this.status = StationStatus.AVAILABLE;
            this.pricing = pricing;
            System.out.println("Charging station created successfully, it was not added to a location");
        }else{
            System.out.println("Creation failed! Please enter valid Station settings");
        }
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public StationType getType() {
        return type;
    }

    public void setType(StationType type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public StationStatus getStatus() {
        return status;
    }

    public void setStatus(StationStatus status) {
        this.status = status;
    }

    public float getPricing() {
        return pricing;
    }

    public void setPricing(float pricing) {
        this.pricing = pricing;
    }

    public boolean isAvailable() {
        return false;
    }

    public boolean supportsType(StationType type) {
        return false;
    }

    public Double calculateCost(Double energyConsumed, Duration duration) {
        return null;
    }

    public void updateStatus(StationStatus newStatus) {
        this.status = newStatus;
    }

    public String getInformation(){
        List<ChargingLocation> locationStations = ChargingLocationManager.getInstance().getAllLocations();
        String locationName = "no Location";
        for (ChargingLocation locationStation : locationStations) {
            if (locationStation.getLocationId() == locationId) {
                locationName = locationStation.getName();
                break;
            }
        }
        return String.format(
                "---%n" +
                        "name: %s%n" +
                        "location: %s%n" +
                        "type: %s%n" +
                        "capacity: %d kWh%n" +
                        "status: %s%n" +
                        "price: %.2f EUR/kWh%n" +
                        "---",
                stationName,
                locationName,
                type,
                capacity,
                status,
                pricing
        );
    }

    public boolean isUnderMaintenance() {
        return false;
    }
}
