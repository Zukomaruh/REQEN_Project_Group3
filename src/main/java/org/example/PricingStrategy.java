package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

public class PricingStrategy {
    private Long pricingId;
    private Long stationId;
    private Double pricePerKwh;
    private Double fixedFee;
    private Double timeBasedRate; // per minute
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;

    public PricingStrategy(Long pricingId, Long stationId, Double pricePerKwh, Double fixedFee, Double timeBasedRate, LocalDateTime effectiveFrom, LocalDateTime effectiveTo) {
        this.pricingId = pricingId;
        this.stationId = stationId;
        this.pricePerKwh = pricePerKwh;
        this.fixedFee = fixedFee;
        this.timeBasedRate = timeBasedRate;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
    }

    public Long getPricingId() {
        return pricingId;
    }

    public void setPricingId(Long pricingId) {
        this.pricingId = pricingId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Double getPricePerKwh() {
        return pricePerKwh;
    }

    public void setPricePerKwh(Double pricePerKwh) {
        this.pricePerKwh = pricePerKwh;
    }

    public Double getFixedFee() {
        return fixedFee;
    }

    public void setFixedFee(Double fixedFee) {
        this.fixedFee = fixedFee;
    }

    public Double getTimeBasedRate() {
        return timeBasedRate;
    }

    public void setTimeBasedRate(Double timeBasedRate) {
        this.timeBasedRate = timeBasedRate;
    }

    public LocalDateTime getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDateTime effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDateTime getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDateTime effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public boolean isActive() {
        return false;
    }

    public Double calculateEnergyCost(Double energyConsumed) {
        return null;
    }

    public Double calculateTimeCost(Duration duration) {
        return null;
    }

    public Double calculateTotalCost(Double energyConsumed, Duration duration) {
        return null;
    }
}
