import enums.StationStatus;

import java.time.LocalDateTime;

public class StationMonitoring {
    private Long monitoringId;
    private Long stationId;
    private Double currentPower;
    private Double voltage;
    private StationStatus realtimeStatus;
    private Integer connectedVehicles;
    private LocalDateTime timestamp;

    public void updateMetrics(Double power, Double voltage, StationStatus status) {

    }

    public boolean isStationWorking() {
        return false;
    }

    public MonitoringAlert checkForAlerts() {
        return null;
    }
}
