package org.example;

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

    private final Long locationId;
    private String name;
    private String address;
    private final List<ChargingStation> stations = new ArrayList<>();

    public ChargingLocation(Long locationId, String name, String address) {
        this.locationId = Objects.requireNonNull(locationId, "locationId must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.address = Objects.requireNonNull(address, "address must not be null");
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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
        stations.add(newStation);
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
}
