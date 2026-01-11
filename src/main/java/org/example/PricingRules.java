package org.example;

import org.example.enums.PriceComponent;
import org.example.enums.StationType;

import java.util.ArrayList;
import java.util.List;

public class PricingRules {
    private int pricingId;
    private int locationId;
    private StationType stationType;
    private int validFrom;
    private int validTo;
    private List<PriceComponent> priceComponents = new ArrayList<>(); //KWH_AC, KWH_DC, CHARGING_MINUTES
    private boolean isActive = false;

    public int getPricingId() {
        return pricingId;
    }

    public void setPricingId(int pricingId) {
        this.pricingId = pricingId;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(int validFrom) {
            this.validFrom = validFrom;
    }

    public int getValidTo() {
        return validTo;
    }

    public void setValidTo(int validTo) {
            this.validTo = validTo;
    }

    public List<PriceComponent> getPriceComponents() {
        return priceComponents;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setLocationId(int locationId) {
        if(locationId > 0){
            this.locationId = locationId;
        }
    }
}
