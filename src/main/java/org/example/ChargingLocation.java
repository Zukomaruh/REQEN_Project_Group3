package org.example;

import org.example.managementClasses.ChargingLocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ChargingLocation
 * ----------------
 * Repräsentiert einen Charging Location Eintrag im Station Network.
 * Enthält:
 *  - eine generierte locationId
 *  - einen Namen
 *  - eine Adresse
 *  - eine Liste von Stations (hier nur als Namen gespeichert, z.B. "station1")
 */
public class ChargingLocation {

    private Long locationId;
    private String name;
    private String address;
    private List<ChargingStation> stations = new ArrayList<>();
    private float pricing;

    public ChargingLocation(String name, String address) {
        this.locationId = System.currentTimeMillis();
        this.name = name;
        this.address = address;
        this.stations = new ArrayList<>();
        ChargingLocationManager.getInstance().addLocation(this);
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

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStations(List<ChargingStation> stations) {
        this.stations = stations;
    }

    /**
     * Gibt eine unveränderliche Sicht auf die Stations-Liste zurück.
     */
    public List<ChargingStation> getStations() {
        return Collections.unmodifiableList(stations);
    }

    /**
     * Fügt einen Station-Namen hinzu (nur für Tests / Demo).
     */
    public void addStation(ChargingStation newStation) {
        if (!stations.contains(newStation)) {
                stations.add(newStation);
        }
    }

    @Override
    public String toString() {
        return "ChargingLocation{" +
                "locationId=" + locationId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", stations=" + stations +
                '}';
    }

    public float getPricing() {
        return pricing;
    }

    public void setPricing(float pricing) {
        if(pricing < 0){
            this.pricing = pricing;
        }
        for (ChargingStation station : stations) {
            station.setPricing(pricing);
        }
    }
}
