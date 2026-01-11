package org.example.managementClasses;

import org.example.Account;
import org.example.ChargingLocation;
import org.example.ChargingProcess;
import org.example.ChargingStation;
import org.example.enums.ChargingMode;
import org.example.enums.SessionStatus;
import org.example.enums.StationStatus;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChargingProcessManager {

    private static final ChargingProcessManager INSTANCE = new ChargingProcessManager();

    public static ChargingProcessManager getInstance() {
        return INSTANCE;
    }

    private final Map<Long, ChargingProcess> processes = new LinkedHashMap<>();
    private long sessionIdCounter = 1L;

    private ChargingProcessManager() {
    }

    /**
     * Startet einen neuen Ladevorgang (User Story 4.1).
     */
    public ChargingProcess startProcess(Long userId,
                                        Long stationId,
                                        String stationName,
                                        ChargingMode mode,
                                        int initialBatteryPercentage,
                                        int targetBatteryPercentage,
                                        int powerKW,
                                        int timeToFullMinutes) {

        AccountManager accountManager = AccountManager.getInstance();
        ChargingLocationManager chargingLocationManager = ChargingLocationManager.getInstance();
        StationManager stationManager = StationManager.getInstance();
        Account customer = accountManager.readAccount(userId);
        ChargingStation station = stationManager.findStationByName(stationName);

        double pricePerKWh = station.getPricing();
        float prepaidBalance = customer.getPrepaidBalance();

        int percentageToCharge = targetBatteryPercentage - initialBatteryPercentage;

        int minutesCharging =
                ((int) percentageToCharge / 100) * timeToFullMinutes;

        System.out.printf(
                "Time to target percentage of %d: %d minutes\n",
                targetBatteryPercentage,
                Math.round(minutesCharging)
        );

        double hoursCharging = minutesCharging / 60.0;
        double result = hoursCharging * powerKW;
        int chargedKWh = Integer.parseInt(String.valueOf(hoursCharging * powerKW));
        float totalCost = Float.parseFloat(String.valueOf(chargedKWh * pricePerKWh));

        System.out.printf("Total cost: %s\n", totalCost);

        if (prepaidBalance >= totalCost) {
            System.out.println("Start charging …");
            customer.setPrepaidBalance(prepaidBalance - totalCost);
            SessionStatus status = SessionStatus.ACTIVE;
            Long sessionId = System.currentTimeMillis();

            return new ChargingProcess(sessionId, userId, stationId, stationName, mode,
                    initialBatteryPercentage, targetBatteryPercentage, chargedKWh, minutesCharging,
                    status
                    );
        } else {
            System.out.println("Insufficient balance. Charging terminated.");
            SessionStatus status = SessionStatus.TERMINATED;
            Long sessionId = System.currentTimeMillis();
            targetBatteryPercentage = 0;
            powerKW = 0;

            return new ChargingProcess(sessionId, userId, stationId, stationName, mode,
                    initialBatteryPercentage, targetBatteryPercentage, chargedKWh, minutesCharging,
                    status
            );
        }
    }


    /**
     * Liefert einen Ladevorgang anhand der Session-ID.
     */
    public ChargingProcess getProcess(Long sessionId) {
        return processes.get(sessionId);
    }

    /**
     * Aktualisiert den Ladezustand für eine Session.
     */
    public void updateBatteryPercentage(Long sessionId, int newPercentage) {
        ChargingProcess process = processes.get(sessionId);
        if (process != null) {
            process.updateBatteryPercentage(newPercentage);
        }
    }

    /**
     * Markiert einen Ladevorgang als abgeschlossen.
     */
    public void completeProcess(Long sessionId) {
        ChargingProcess process = processes.get(sessionId);
        if (process != null) {
            process.complete();
        }
    }

    /**
     * Gibt eine unmodifizierbare Sicht auf alle Prozesse zurück.
     */
    public Map<Long, ChargingProcess> getAllProcesses() {
        return Collections.unmodifiableMap(processes);
    }

    /**
     * Hilfsmethode für Tests, um den Manager zurückzusetzen.
     */
    public void clear() {
        processes.clear();
        sessionIdCounter = 1L;
    }
}
