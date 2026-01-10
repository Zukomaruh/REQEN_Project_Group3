package org.example;

import org.example.enums.ChargingMode;
import org.example.enums.SessionStatus;
import org.example.managementClasses.AccountManager;

public class ChargingProcess {

    private Long sessionId;
    private Long customerId;
    private Long stationId;
    private String stationName;
    private ChargingMode mode;

    private int batteryPercentage;
    private int targetBatteryPercentage;
    private int powerKW;
    private int timeToFullMinutes;

    // Für US 4.1 – Reichweite
    private int expectedRangeKm;
    private int drivenDistanceKm;

    private SessionStatus status;

    public ChargingProcess(Long sessionId,
                           Long customerId,
                           Long stationId,
                           String stationName,
                           ChargingMode mode,
                           int batteryPercentage,
                           int targetBatteryPercentage,
                           int powerKW,
                           int timeToFullMinutes,
                           SessionStatus status) {
        if(AccountManager.getInstance().readAccount(customerId) != null){
            AccountManager.getInstance().readAccount(customerId).setChargingProcess(timeToFullMinutes);
        }
        this.sessionId = sessionId;
        this.customerId = customerId;
        this.stationId = stationId;
        this.stationName = stationName;
        this.mode = mode;
        this.batteryPercentage = batteryPercentage;
        this.targetBatteryPercentage = targetBatteryPercentage;
        this.powerKW = powerKW;
        this.timeToFullMinutes = timeToFullMinutes;
        this.status = status;
    }

    // --- Kernlogik ---

    /**
     * Aktualisiert den Ladezustand. Prozentwert darf nicht sinken.
     * Erreicht oder übersteigt der Wert Ziel oder 100%, wird der Status auf COMPLETED gesetzt.
     */
    public void updateBatteryPercentage(int newPercentage) {
        if (newPercentage < batteryPercentage) {
            // wir ignorieren sinkende Werte – US 4.2: „only increases“
            return;
        }

        if (newPercentage > 100) {
            newPercentage = 100;
        }

        this.batteryPercentage = newPercentage;

        if (batteryPercentage >= targetBatteryPercentage || batteryPercentage == 100) {
            this.status = SessionStatus.COMPLETED;
            this.timeToFullMinutes = 0;
        }
    }

    public void complete() {
        this.status = SessionStatus.COMPLETED;
        this.batteryPercentage = Math.max(batteryPercentage, targetBatteryPercentage);
        if (this.batteryPercentage > 100) {
            this.batteryPercentage = 100;
        }
        this.timeToFullMinutes = 0;
    }

    /**
     * Prüft, ob die gefahrene Distanz innerhalb der gegebenen Toleranz zum erwarteten Wert liegt.
     */
    public boolean isDrivenDistanceWithinTolerance(int tolerancePercent) {
        int allowedDiff = (int) Math.round(expectedRangeKm * (tolerancePercent / 100.0));
        int diff = Math.abs(drivenDistanceKm - expectedRangeKm);
        return diff <= allowedDiff;
    }

    // --- Getter / Setter ---

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public ChargingMode getMode() {
        return mode;
    }

    public void setMode(ChargingMode mode) {
        this.mode = mode;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public int getTargetBatteryPercentage() {
        return targetBatteryPercentage;
    }

    public void setTargetBatteryPercentage(int targetBatteryPercentage) {
        this.targetBatteryPercentage = targetBatteryPercentage;
    }

    public int getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(int powerKW) {
        this.powerKW = powerKW;
    }

    public int getTimeToFullMinutes() {
        return timeToFullMinutes;
    }

    public void setTimeToFullMinutes(int timeToFullMinutes) {
        this.timeToFullMinutes = timeToFullMinutes;
    }

    public int getExpectedRangeKm() {
        return expectedRangeKm;
    }

    public void setExpectedRangeKm(int expectedRangeKm) {
        this.expectedRangeKm = expectedRangeKm;
    }

    public int getDrivenDistanceKm() {
        return drivenDistanceKm;
    }

    public void setDrivenDistanceKm(int drivenDistanceKm) {
        this.drivenDistanceKm = drivenDistanceKm;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
