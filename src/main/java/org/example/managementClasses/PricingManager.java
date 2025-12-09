package org.example.managementClasses;

import org.example.PricingRules;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PricingManager {
    private final List<PricingRules> pricingRules = new ArrayList<>();

    public void addPricingRule(PricingRules rule) {
        pricingRules.add(rule);
    }

    public List<PricingRules> getPricingRules() {
        return pricingRules;
    }

    public PricingRules getPricingRuleById(int id) {
        return pricingRules.stream().filter(r -> r.getPricingId() == id).findFirst().orElse(null);
    }

    public void removePricingRule(int id) {
        pricingRules.removeIf(r -> r.getPricingId() == id);
    }
}
