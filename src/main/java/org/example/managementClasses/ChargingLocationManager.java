package org.example.managementClasses;

import org.example.ChargingLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ChargingLocationManager (Singleton)
 * -----------------------------------
 * Verantwortlich für das CRUD-Management der Charging Locations.
 *  - createLocation(name, address)
 *  - getAllLocations()
 *  - clear()   (wird in den Cucumber-Tests für Reset verwendet)
 */
public class ChargingLocationManager {

    // ==========================
    // Singleton
    // ==========================

    private static final ChargingLocationManager INSTANCE = new ChargingLocationManager();

    public static ChargingLocationManager getInstance() {
        return INSTANCE;
    }

    private ChargingLocationManager() {
    }

    // ==========================
    // FIELDS
    // ==========================

    private final List<ChargingLocation> locations = new ArrayList<>();
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
            return null;
        }

        ChargingLocation location = new ChargingLocation(idCounter++, name.trim(), address.trim());
        locations.add(location);
        return location;
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
}
