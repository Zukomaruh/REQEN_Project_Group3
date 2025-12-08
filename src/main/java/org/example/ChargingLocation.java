package org.example;

import java.util.ArrayList;
import java.util.List;

public class ChargingLocation {
    private Long locationId;
    private String name;
    private String address;
    private List<ChargingStation> stations;

    public ChargingLocation(String name, String address) {
        this.locationId = System.currentTimeMillis();
        this.name = name;
        this.address = address;
        this.stations = new ArrayList<>();
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ChargingStation> getStations() {
        return stations;
    }

    public void setStations(List<ChargingStation> stations) {
        this.stations = stations;
    }

    public void addStation(ChargingStation station) {
        this.stations.add(station);
    }

    public void removeStation(Long stationId) {

    }

    public List<ChargingStation> getAvailableStations() {
        return null;
    }

    public int getTotalCapacity() {
        return 0;
    }

    public int getAvailableCapacity() {
        return 0;
    }
}
