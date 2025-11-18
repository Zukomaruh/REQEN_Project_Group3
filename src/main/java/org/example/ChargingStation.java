package org.example;

import org.example.enums.StationStatus;
import org.example.enums.StationType;

import java.time.Duration;

public class ChargingStation {
    private Long stationId;
    private Long locationId;
    private String stationName;
    private StationType type; // AC, DC
    private Integer capacity; // kW
    private StationStatus status; // AVAILABLE, CHARGING, MAINTENANCE, OFFLINE
    private PricingStrategy pricing;

    public ChargingStation(Long stationId, Long locationId, String stationName, StationType type, Integer capacity, StationStatus status, PricingStrategy pricing) {
        this.stationId = stationId;
        this.locationId = locationId;
        this.stationName = stationName;
        this.type = type;
        this.capacity = capacity;
        this.status = status;
        this.pricing = pricing;
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

    public PricingStrategy getPricing() {
        return pricing;
    }

    public void setPricing(PricingStrategy pricing) {
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

    }

    public String getInformation(){
        return String.format("StationID: %d, LocationID: %d, Name: %s, Type: %s, Capacity: %d, Status: %s"
        ,stationId, locationId, stationName, type, capacity, status);
    }

    public boolean isUnderMaintenance() {
        return false;
    }
}
