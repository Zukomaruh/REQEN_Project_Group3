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
        boolean removed = removePricingRuleSafe(id);
        if (!removed) {
            System.out.println("Deletion failed! Pricing rule not found.");
        }
    }


    public boolean removePricingRuleSafe(int id) {
        boolean removed = pricingRules.removeIf(r -> r.getPricingId() == id);
        if (removed) {
            System.out.println("Pricing rule removed successfully");
        }
        return removed;
    }

}