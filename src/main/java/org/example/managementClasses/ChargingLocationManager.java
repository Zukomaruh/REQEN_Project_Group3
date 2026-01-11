package org.example.managementClasses;

import org.example.ChargingLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChargingLocationManager {

    // ==========================
    // Singleton
    // ==========================

    private static final ChargingLocationManager INSTANCE = new ChargingLocationManager();

    public static ChargingLocationManager getInstance() {
        return INSTANCE;
    }

    private ChargingLocationManager()  {
    }



    // ==========================
    // FIELDS
    // ==========================

    private List<ChargingLocation> locations = new ArrayList<>();
    private long idCounter = 1L;


    // ==========================
    // CRUD – CREATE
    // ==========================

    /**
     * Legt einen neuen ChargingLocation an, falls Name und Adresse gültig sind.
     * Bei ungültigem Input wird null zurückgegeben.
     */
    public ChargingLocation createLocation(String name, String address) {
        if (name == null || name.isBlank() || address == null || address.isBlank()) {
            System.out.println("Invalid Input, Location not created");
            return null;
        }

        ChargingLocation location = new ChargingLocation(name.trim(), address.trim());
        locations.add(location);
        System.out.println("Charging Location created successfully");
        return location;
    }

    public void addLocation(ChargingLocation newLocation){
        locations.add(newLocation);
    }

    // ==========================
    // CRUD – READ
    // ==========================

    /**
     * Liefert eine unveränderliche Liste aller gespeicherten Charging Locations.
     */
    public List<ChargingLocation> getAllLocations() {
        return Collections.unmodifiableList(locations);
    }

    // ==========================
    // CRUD – DELETE
    // ==========================

    /**
     * Deletes a location only if it contains no charging stations.
     * IMPORTANT: because locations may be stored multiple times in the list
     * (due to current constructor + createLocation behavior), we remove all matches.
     *
     * @return true if deleted, false if rejected or not found
     */
    public boolean deleteLocation(long locationId) {
        ChargingLocation loc = findById(locationId);
        if (loc == null) return false;

        // Reject deletion if there are stations
        if (loc.getStations() != null && !loc.getStations().isEmpty()) {
            return false;
        }

        // Remove ALL entries with that ID (handles duplicates)
        return locations.removeIf(l ->
                l.getLocationId() != null && l.getLocationId() == locationId
        );
    }

    /**
     * Helper: find location by id.
     */
    public ChargingLocation findById(long locationId) {
        for (ChargingLocation loc : locations) {
            if (loc.getLocationId() != null && loc.getLocationId() == locationId) {
                return loc;
            }
        }
        return null;
    }

    // ==========================
    // TEST / RESET
    // ==========================

    /**
     * Löscht alle gespeicherten Locations und setzt den ID-Zähler zurück.
     * Wird in den BDD-Tests genutzt, um jeden Scenario-Run sauber zu starten.
     */
    public void clear() {
        locations.clear();
        idCounter = 1L;
    }

    public ChargingLocation getLocation(String name) {
        for (ChargingLocation location : locations) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }

    public ChargingLocation getLocation(long locationId) {
        for (ChargingLocation location : locations){
            if(location.getLocationId().equals(locationId)){
                return location;
            }
        }
        return null;
    }
}
