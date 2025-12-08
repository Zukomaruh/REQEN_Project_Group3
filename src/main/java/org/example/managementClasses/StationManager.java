package org.example.managementClasses;

import org.example.ChargingLocation;
import org.example.ChargingStation;
import org.example.enums.StationStatus;
import org.example.enums.StationType;

import java.util.*;

public class StationManager {

    private Map<Long, ChargingLocation> locations = new HashMap<>();
    private Map<Long, ChargingStation> stations = new LinkedHashMap<>(); // keep insertion order for predictable output

    private long stationIdCounter = 1;

    // Register a location in the system (used by step definitions or external setup)
    public void addLocation(ChargingLocation location) {
        locations.put(location.getLocationId(), location);
    }

    public ChargingLocation getLocation(long locationId) {
        return locations.get(locationId);
    }

    public boolean locationExists(long locationId) {
        return locations.containsKey(locationId);
    }

    /**
     * Creates a station. If the location does not exist (locationId == 0 or not present),
     * the station will be created but not added to any location.
     *
     * Validation performed:
     *  - stationName must not be null or empty (caller must enforce)
     *
     * @return the created ChargingStation
     */
    public ChargingStation createStation(
            Long locationId,         // may be null or 0 to indicate "no location"
            String name,
            StationType type,
            int capacity,
            double pricing
    ) {
        // Validate: name must not be null/empty
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Creation failed! Please enter valid Station settings");
        }

        long newStationId = stationIdCounter++;

        ChargingStation station = new ChargingStation(
                newStationId,
                (locationId == null ? 0L : locationId),
                name,
                type,
                capacity,
                StationStatus.AVAILABLE,
                (float) pricing
        );

        stations.put(newStationId, station);

        // If a valid location id was provided and the location exists, add to it.
        if (locationId != null && locationId != 0 && locations.containsKey(locationId)) {
            ChargingLocation loc = locations.get(locationId);
            loc.addStation(station);
        }

        return station;
    }

    public ChargingStation findStationByName(String name) {
        if (name == null) return null;
        return stations.values().stream()
                .filter(s -> name.equalsIgnoreCase(s.getStationName()))
                .findFirst()
                .orElse(null);
    }

    public void updatePricingByName(String stationName, double newPrice) {
        ChargingStation station = findStationByName(stationName);

        if (station == null) {
            throw new IllegalArgumentException("Station not found");
        }

        station.setPricing((float) newPrice);
    }

    /**
     * Returns YAML-like formatted information for all stations in insertion order.
     * Price is formatted using comma as decimal separator and two digits.
     */
    public String getAllStationInformation() {
        StringBuilder sb = new StringBuilder();
        for (ChargingStation station : stations.values()) {
            // location name lookup (maybe missing)
            String locationName = "Unknown";
            long locId = station.getLocationId();
            if (locId != 0 && locations.containsKey(locId)) {
                ChargingLocation loc = locations.get(locId);
                if (loc != null && loc.getName() != null) {
                    locationName = loc.getName();
                }
            }

            // format price with comma decimal
            String priceStr = formatDoubleWithComma(station.getPricing());

            sb.append("---\n");
            sb.append("name: ").append(station.getStationName()).append("\n");
            sb.append("location: ").append(locationName).append("\n");
            sb.append("type: ").append(station.getType().name()).append("\n");
            sb.append("capacity: ").append(station.getCapacity()).append(" kWh").append("\n");
            sb.append("status: ").append(station.getStatus().name()).append("\n");
            sb.append("price ").append(priceStr).append(" EUR/kWh").append("\n");
        }
        if (!sb.isEmpty()) {
            sb.append("---\n");
        }
        return sb.toString();
    }

    private String formatDoubleWithComma(double value) {
        // Format with two decimals then replace '.' with ','
        String formatted = String.format(Locale.US, "%.2f", value);
        return formatted.replace('.', ',');
    }
}