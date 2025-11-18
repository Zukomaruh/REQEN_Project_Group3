package org.example;

import org.example.enums.ChargingMode;
import org.example.enums.SessionStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class ChargingSession {
    private Long sessionId;
    private Long userId;
    private Long stationId;
    private SessionStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double energyConsumed; // kWh
    private Duration duration;
    private Double totalCost;
    private ChargingMode chargingMode;

    public void startSession() {

    }

    public void endSession() {

    }

    public void interruptSession() {

    }

    public Duration getDuration() {
        return null;
    }

    public boolean isActive() {
        return false;
    }

    public Double calculateEnergyCost() {
        return null;
    }

    public Double calculateTimeCost() {
        return null;
    }
}
