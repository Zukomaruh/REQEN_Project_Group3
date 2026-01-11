package org.example.managementClasses;

import org.example.PricingRules;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PricingManager {
    private final List<PricingRules> pricingRules = new ArrayList<>();

    public void addPricingRule(PricingRules rule) {
        if(rule.getValidFrom() == 0){
            System.out.println("Creation failed! Please enter valid pricing settings.");
        } else {
        pricingRules.add(rule);
        System.out.println("Pricing rule created successfully");
        rule.setActive(true);}
    }

    public List<PricingRules> getPricingRules() {
        return pricingRules;
    }

    public PricingRules getPricingRuleById(int id) {
        return pricingRules.stream().filter(r -> r.getPricingId() == id).findFirst().orElse(null);
    }

    public void removePricingRule(int id) {
        pricingRules.removeIf(r -> r.getPricingId() == id);
        System.out.println("Pricing rule removed successfully");
    }
}
