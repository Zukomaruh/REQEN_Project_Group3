package org.example.managementClasses;

import org.example.ChargingProcess;
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
    public ChargingProcess startProcess(Long customerId,
                                        Long stationId,
                                        String stationName,
                                        ChargingMode mode,
                                        int initialBatteryPercentage,
                                        int targetBatteryPercentage,
                                        int powerKW,
                                        int timeToFullMinutes) {

        if (customerId == null || stationId == null
                || stationName == null || stationName.isBlank()
                || mode == null
                || initialBatteryPercentage < 0 || targetBatteryPercentage < 0 || targetBatteryPercentage > 100
                || initialBatteryPercentage > targetBatteryPercentage
                || powerKW <= 0
        ) {
            return null;
        }

        long sessionId = sessionIdCounter++;
        ChargingProcess process = new ChargingProcess(
                sessionId,
                customerId,
                stationId,
                stationName,
                mode,
                initialBatteryPercentage,
                targetBatteryPercentage,
                powerKW,
                timeToFullMinutes,
                SessionStatus.ACTIVE
        );

        processes.put(sessionId, process);
        StationManager stationManager = StationManager.getInstance();
        stationManager.findStationByName(stationName).setStatus(StationStatus.CHARGING);
        return process;
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
