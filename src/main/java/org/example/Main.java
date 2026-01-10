package org.example;

import org.example.enums.AccountType;
import org.example.enums.ChargingMode;
import org.example.enums.PriceComponent;
import org.example.enums.StationType;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.ChargingLocationManager;
import org.example.managementClasses.ChargingProcessManager;
import org.example.managementClasses.InvoiceManager;
import org.example.managementClasses.PricingManager;
import org.example.managementClasses.StationManager;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to EloNet");
        System.out.println();

        // =========================================================
        // EPIC 1 – Account Management
        // User Story 1.1 – Create Account
        // User Story 1.2 – Read Account
        // User Story 1.3 – Update Account
        // =========================================================
        System.out.println("User Story 1.1 – Create Account");
        AccountManager accountManager = AccountManager.getInstance();

        Account validAccount = new Account(
                "Max Mustermann",
                "max.mustermann@gmail.com",
                "123456789012",
                AccountType.CUSTOMER
        );
        accountManager.addAccount(validAccount);
        long userId = validAccount.getUserId(); // unique account ID
        System.out.println("Created Account:");
        System.out.println(validAccount);
        System.out.println();

        System.out.println("User Story 1.2 – Read Account");
        Account readAccount = accountManager.readAccount(userId);
        System.out.println("Read Account:");
        System.out.println(readAccount);
        System.out.println();

        System.out.println("User Story 1.3 – Update Account");
        // deactivate account
        accountManager.updateAccount(false, userId);
        // change username
        accountManager.updateAccount("Maxime Musterfrau", userId);
        Account updatedAccount = accountManager.readAccount(userId);
        System.out.println("Updated Account:");
        System.out.println(updatedAccount);
        System.out.println();

        // =========================================================
        // EPIC 6 – Station Network Management (Charging Locations)
        // User Story 6.1 – Create Charging Location
        // User Story 6.2 – Read Charging Locations
        // User Story 6.3 – Update Charging Location
        // =========================================================
        ChargingLocationManager locationManager = ChargingLocationManager.getInstance();

        System.out.println("User Story 6.1 – Create Charging Location");
        ChargingLocation validLocation = locationManager.createLocation(
                "Westbahnhof",
                "Mariahilferstraße 187, 1150 Wien"
        );
        System.out.println("Created Charging Location:");
        System.out.println(validLocation);
        System.out.println();

        System.out.println("User Story 6.2 – Read Charging Locations");
        for (ChargingLocation loc : locationManager.getAllLocations()) {
            System.out.println(loc);
        }
        System.out.println();

        System.out.println("User Story 6.3 – Update Charging Location");
        if (validLocation != null) {
            validLocation.setName("Hauptzentrale");
            System.out.println("Updated Charging Location:");
            System.out.println(validLocation);
        }
        System.out.println();

        // =========================================================
        // EPIC 3 – Charging Station Management
        // User Story 3.1 / 7.1 – Create Charging Station
        // User Story 3.2 / 7.2 – Read Charging Station
        // User Story 3.3 / 7.3 – Update Charging Station
        // =========================================================
        StationManager stationManager = StationManager.getInstance();

        System.out.println("User Story 3.1 – Create Charging Station");
        // make sure StationManager knows the location
        stationManager.addLocation(validLocation);

        ChargingStation validStation = stationManager.createStation(
                validLocation.getLocationId(),
                "station01",
                StationType.AC,
                150,
                0.35
        );
        System.out.println("Created Charging Station:");
        System.out.println(validStation);
        System.out.println();

        System.out.println("User Story 3.2 – Read Charging Station");
        System.out.println("All Station Information:");
        System.out.println(StationManager.getInstance().getAllStationInformation());
        System.out.println();

        System.out.println("User Story 3.3 – Update Charging Station");
        if (validStation != null) {
            validStation.setPricing(0.50F);
            System.out.println("Updated Charging Station:");
            System.out.println(validStation);
        }
        System.out.println();

        // =========================================================
        // EPIC 4 – Charging Process Management
        // User Story 4.1 – Start Charging Process
        // User Story 4.2 – Read Charging Status
        // User Story 4.3 – Update Charging Status
        // =========================================================
        ChargingProcessManager processManager = ChargingProcessManager.getInstance();

        System.out.println("User Story 4.1 – Start Charging Process");
        Long stationId = validStation != null ? validStation.getStationId() : 1010L;
        ChargingProcess process = processManager.startProcess(
                userId,
                stationId,
                validStation != null ? validStation.getStationName() : "station01",
                ChargingMode.FAST,
                40,   // initial battery level
                80,   // target battery level
                11,   // power in kW
                30    // time to full in minutes
        );
        System.out.println("Charging process started with sessionId: " + process.getSessionId());
        System.out.println("Battery after start: " + process.getBatteryPercentage()
                + "%, status: " + process.getStatus());
        System.out.println();

        System.out.println("User Story 4.2 – Read Charging Status");
        ChargingProcess sameProcess = processManager.getProcess(process.getSessionId());
        System.out.println("Current charging status:");
        System.out.println("Battery: " + sameProcess.getBatteryPercentage() + "%");
        System.out.println("Mode: " + sameProcess.getMode());
        System.out.println("Status: " + sameProcess.getStatus());
        System.out.println();

        System.out.println("User Story 4.3 – Update Charging Status");
        processManager.updateBatteryPercentage(process.getSessionId(), 100);
        processManager.completeProcess(process.getSessionId());
        ChargingProcess completed = processManager.getProcess(process.getSessionId());
        System.out.println("After completion:");
        System.out.println("Battery: " + completed.getBatteryPercentage() + "%");
        System.out.println("Status: " + completed.getStatus());
        completed.setExpectedRangeKm(200);
        completed.setDrivenDistanceKm(195);
        System.out.println("Driven distance within 5% tolerance: "
                + completed.isDrivenDistanceWithinTolerance(5));
        System.out.println();

        // =========================================================
        // EPIC 8 – Manage Pricing of Charging Stations
        // User Story 8.1 – Create Pricing
        // User Story 8.2 – Update Pricing
        // =========================================================
        System.out.println("User Story 8.1 – Create Pricing Rule");
        PricingManager pricingManager = new PricingManager();

        PricingRules pricingRule = new PricingRules();
        pricingRule.setPricingId(1001);
        if (validLocation != null) {
            pricingRule.setLocationId(validLocation.getLocationId().intValue());
        }
        pricingRule.setValidFrom(20240101); // simple numeric date representation
        pricingRule.setValidTo(20241231);
        pricingRule.getPriceComponents().add(PriceComponent.KWH_AC);
        pricingRule.setActive(true);

        pricingManager.addPricingRule(pricingRule);
        System.out.println("Created Pricing Rule:");
        System.out.println("pricingId: " + pricingRule.getPricingId());
        System.out.println("locationId: " + pricingRule.getLocationId());
        System.out.println("validFrom: " + pricingRule.getValidFrom());
        System.out.println("validTo: " + pricingRule.getValidTo());
        System.out.println("active: " + pricingRule.isActive());
        System.out.println("priceComponents: " + pricingRule.getPriceComponents());
        System.out.println();

        System.out.println("User Story 8.2 – Update Pricing Rule (activate/deactivate)");
        pricingRule.setActive(false);
        System.out.println("Pricing rule active after update: " + pricingRule.isActive());
        System.out.println();

        // =========================================================
        // EPIC 9 – Invoice Management (FIXED für US 5.1/5.2)
        // =========================================================
        System.out.println("User Story 5.1 & 5.2 – Read and Sort Invoices");
        InvoiceManager invoiceManager = InvoiceManager.getInstance();

        // Beispiel-Rechnung erstellen
        Invoice demoInvoice = new Invoice(
                12345L,                 // Customer ID
                "Demo Station",         // Station Name
                "FAST",                 // Mode
                50.0,                   // kWh
                30,                     // Duration
                0.50,                   // Price
                25.00,                  // Total
                new Date(),             // Start Time
                "PAID"                  // Status
        );
        invoiceManager.createInvoice(demoInvoice);

        System.out.println("Invoice History:");
        System.out.println(invoiceManager.viewHistory());

        System.out.println("Demo finished.");
    }
}
