package org.example.managementClasses;

import org.example.Account;
import org.example.ChargingProcess;
import org.example.Invoice;
import org.example.enums.ChargingMode;
import org.example.enums.SessionStatus;
import org.example.enums.StationStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
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
        StationManager stationManager = StationManager.getInstance();
        InvoiceManager invoiceManager = InvoiceManager.getInstance();
        Account account = AccountManager.getInstance().readAccount(customerId);
        float pricePerKWh = stationManager.findStationByName(stationName).getPricing();


        int percentageToCharge = targetBatteryPercentage - initialBatteryPercentage;
        double minutesCharging = ((double) percentageToCharge / 100) * timeToFullMinutes;
        double hoursCharging = minutesCharging / 60.0;
        double chargedKWh = hoursCharging * powerKW;
        double totalCost = chargedKWh * pricePerKWh;

        if (account == null) {return null;}
        if (
                stationId == null
                || stationName == null
                || stationName.isBlank()
                || mode == null
                || initialBatteryPercentage < 0
                || targetBatteryPercentage < 0
                || targetBatteryPercentage > 100
                || initialBatteryPercentage > targetBatteryPercentage
                || powerKW <= 0
                || account.getPrepaidBalance() < totalCost

        ) {
            System.out.printf("Charging terminated: Insufficient balance.\nMissing amount: %.2f"
                    ,Double.parseDouble(String.valueOf(totalCost-account.getPrepaidBalance())));
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
        process.setStatus(SessionStatus.ACTIVE);
        stationManager.findStationByName(stationName).setStatus(StationStatus.CHARGING);
        int minutesCharging2 = Integer.parseInt(String.valueOf(Math.round(minutesCharging)));
        Invoice invoice = new Invoice(customerId, stationName,mode.toString(),chargedKWh, minutesCharging2, pricePerKWh, totalCost, Date.from(Instant.now()), "PAID");
        invoiceManager.createInvoice(invoice);
        System.out.printf("Sufficient prepaid balance.\nCharging cost: %.2f\nBalance after charging: %.2f"
                ,Double.parseDouble(String.valueOf(totalCost))
                ,Double.parseDouble(String.valueOf(account.getPrepaidBalance()-totalCost)));
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
     * NEW: Returns true if there is any ACTIVE charging process for the given station.
     */
    public boolean hasActiveProcessForStation(long stationId) {
        for (ChargingProcess p : processes.values()) {
            if (p != null
                    && p.getStationId() != null
                    && p.getStationId() == stationId
                    && p.getStatus() == SessionStatus.ACTIVE)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Hilfsmethode für Tests, um den Manager zurückzusetzen.
     */
    public void clear() {
        processes.clear();
        sessionIdCounter = 1L;
    }
}