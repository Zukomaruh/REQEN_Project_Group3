import enums.StationStatus;
import enums.StationType;

import java.time.Duration;

public class ChargingStation {
    private Long stationId;
    private Long locationId;
    private String stationName;
    private StationType type; // AC, DC
    private Integer capacity; // kW
    private StationStatus status; // AVAILABLE, CHARGING, MAINTENANCE, OFFLINE
    private PricingStrategy pricing;

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

    public boolean isUnderMaintenance() {
        return false;
    }
}
