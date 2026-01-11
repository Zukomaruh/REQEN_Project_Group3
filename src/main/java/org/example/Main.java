package org.example;

import org.example.enums.*;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.ChargingLocationManager;
import org.example.managementClasses.ChargingProcessManager;
import org.example.managementClasses.InvoiceManager;
import org.example.managementClasses.PricingManager;
import org.example.managementClasses.StationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        //variables
        ChargingLocation chargingLocation = new ChargingLocation("Meidling123", "Meidling 123");
        long locationId = chargingLocation.getLocationId();
        ChargingStation chargingStation = new ChargingStation(locationId, "Station A", StationType.AC, 50, 0.3f);
        System.out.println("\n=============================================================");

        System.out.println(ANSI_YELLOW + "\n================ EPIC 1 – Account Management ================" + ANSI_RESET);
        //1.1
        System.out.println(ANSI_GREEN + "\nInvalid user credentials entered when creating a new account" + ANSI_RESET);
        AccountManager accountManager = AccountManager.getInstance();
        Account invalid = new Account("Max Mustermann", "maxmustermann.com", "passw0rd123!", AccountType.CUSTOMER);
        System.out.println(ANSI_GREEN + "\nValid user credentials entered when creating a new account" + ANSI_RESET);
        Account account = new Account("Max Mustermann", "max@mustermann.com", "passw0rd123!", AccountType.CUSTOMER);
        accountManager.addAccount(account);

        //1.2
        System.out.println(ANSI_GREEN + "\nI want to read my account details." + ANSI_RESET);
        long userId = account.getUserId();
        Account readAccount = accountManager.readAccount(userId);
        System.out.println(readAccount);

        //1.3
        System.out.println(ANSI_GREEN + "\nI want to update my username to \"Maxi\"." + ANSI_RESET);
        accountManager.updateAccount("Maxi", userId);
        Account updatedAccount = accountManager.readAccount(userId);
        System.out.println();

        //1.4
        System.out.println(ANSI_GREEN + "\nI try to delete my account..." + ANSI_RESET);
        //Exception in thread
        //accountManager.deleteAccount(updatedAccount.getUserId(), updatedAccount.getPassword());
        System.out.println(ANSI_GREEN + "\n… but forgot that we still had an active charging process!" + ANSI_RESET);
        userId = updatedAccount.getUserId();
        String password = updatedAccount.getPassword();
        accountManager.readAccount(userId).setChargingProcess(0);
        accountManager.deleteAccount(userId, password);

        System.out.println(ANSI_YELLOW + "\n================ EPIC 2 – Prepaid Balance Management ================" + ANSI_RESET);
        //2.1
        System.out.println(ANSI_GREEN + "\nI want to start charging and select the payment method \"cash\" and the amount \"600\".\nBut they are invalid." + ANSI_RESET);
        account.setPaymentMethod("cash");
        account.setPrepaidAmount("600");
        System.out.println(ANSI_GREEN + "\nI correct it and get my selection confirmed." + ANSI_RESET);
        account.setPaymentMethod("paypal");
        account.setPrepaidAmount("500");
        account.getPaymentConfirmationMessage();
        accountManager.updatePrepaidBalance(account);

        //2.2
        System.out.println(ANSI_GREEN + "\nI want to charge my car at a station.\nBut my balance is too low to cover charging costs." + ANSI_RESET);
        chargingStation.setPricing(30);
        account.canStartCharging(chargingStation);
        System.out.println(ANSI_GREEN + "\nI top my balance up so that I can start charging." + ANSI_RESET);
        accountManager.readPrepaidBalance(account);
        account.setPrepaidAmount("100");
        account.getPaymentConfirmationMessage();
        accountManager.updatePrepaidBalance(account);
        account.canStartCharging(chargingStation);

        System.out.println(ANSI_YELLOW + "\n================ EPIC 3 – Manage Charging Station ================" + ANSI_RESET);
        StationManager stationManager = StationManager.getInstance();
        ChargingLocationManager locationManager = ChargingLocationManager.getInstance();
        ChargingLocation demoLocation = new ChargingLocation("Demo Location", "Demo Address 123");
        long demoLocationId = demoLocation.getLocationId();
        locationManager.addLocation(demoLocation);
        stationManager.addLocation(demoLocation);

        // US 3.1: Create charging station
        System.out.println(ANSI_GREEN + "\nAs the owner, I create a charging station without a name …" + ANSI_RESET);
        //Exception thread
        //ChargingStation invalidStation = stationManager.createStation(demoLocationId, null, StationType.AC, 100, 0.35);
        System.out.println(ANSI_GREEN + "\n… but forgot that a charging station must be given a name!" + ANSI_RESET);
        ChargingStation newStation = stationManager.createStation(demoLocationId, "SuperCharger1", StationType.AC, 100, 0.35);
        stationManager.addLocation(demoLocation);
        demoLocation.setLocationId(demoLocationId);

        // US 3.2: Read station information
        System.out.println(ANSI_GREEN + "\nI want to check the information of \"SuperCharger1\"." + ANSI_RESET);
        System.out.println(newStation.getInformation());

        // US 3.2: Read all stations
        System.out.println(ANSI_GREEN + "\nI want to check the information of all stations in the network." + ANSI_RESET);
        ChargingStation newStation2 = stationManager.createStation(demoLocationId, "SuperCharger2", StationType.AC, 200, 0.7);
        System.out.println(stationManager.getAllStationInformation());

        // US 3.3: Update station
        System.out.println(ANSI_GREEN + "\nUpdate pricing of \"SuperCharger1\" to 0.40 and check if latest shows." + ANSI_RESET);
        stationManager.updatePricingByName(newStation.getStationName(), 0.40);
        System.out.println(newStation.getInformation());

        System.out.println(ANSI_YELLOW + "\n================ EPIC 4 – Charging Process Management ================" + ANSI_RESET);
        account.setUserId(1001);
        accountManager.addAccount(account);
        long customerId = account.getUserId();
        ChargingProcessManager processManager = ChargingProcessManager.getInstance();
        long stationId = newStation.getStationId();
        String stationName = newStation.getStationName();

        // US 4.1: Start charging to target
        System.out.println(ANSI_GREEN + "\nUpdate battery percentage to 80%." + ANSI_RESET);
        ChargingProcess process = processManager.startProcess(customerId, stationId, stationName, ChargingMode.STANDARD, 40, 80, 50, 60);
        process.updateBatteryPercentage(80);
        process.complete();

        // US 4.2: Read status
        System.out.println(ANSI_GREEN + "\nI want to check the information of the charging process." + ANSI_RESET);
        //Exception in thread
        process.getChargingInformation();

        // US 4.2: Status updates increasing
        System.out.println(ANSI_GREEN + "\nI want to decrease my battery percentage …" + ANSI_RESET);
        //Exception in thread
        //process.updateBatteryPercentage(70);
        System.out.println(ANSI_GREEN + "\n… but only increasing is allowed!" + ANSI_RESET);

        // US 4.3: Update station status on start
        System.out.println(ANSI_GREEN + "\nI want to check if the charging status changes accordingly when starting a process." + ANSI_RESET);
        newStation.setStatus(StationStatus.AVAILABLE);
        System.out.println(ANSI_GREEN + "\nBefore starting charging, the station is: "+newStation.getStatus() + ANSI_RESET);
        processManager.startProcess(customerId, stationId, stationName, ChargingMode.ECO, 10, 50, 20, 90);
        System.out.println(ANSI_GREEN + "\nAfter starting charging, the station is: "+newStation.getStatus() + ANSI_RESET);

        // US 4.3: On finish
        process.complete();
        System.out.println(ANSI_GREEN + "\nWhen done charging, the station is: "+process.getStatus() + ANSI_RESET);

        // US 4.3: Reject on unavailable
        newStation.setStatus(StationStatus.MAINTENANCE);
        System.out.println(ANSI_GREEN + "\nWhen the station is under maintenance, charging is not possible and the status is set to: "+newStation.getStatus() + ANSI_RESET);
        // processManager.startProcess would return null for invalid

        System.out.println(ANSI_YELLOW + "\n================ EPIC 5 – Invoice Management ================" + ANSI_RESET); // Assuming EPIC 5 is Invoices based on US 5.x
        InvoiceManager invoiceManager = InvoiceManager.getInstance();

        // US 5.1: Read invoices
        System.out.println(ANSI_GREEN + "\nI want to read all my invoices from different charging stations …" + ANSI_RESET);
        Invoice inv1 = new Invoice(1001L, "Station A", "AC", 10, 60, 0.50, 5.00, new Date(), "PAID");
        Invoice inv2 = new Invoice(1001L, "Station B", "DC", 50, 30, 0.80, 40.00, new Date(System.currentTimeMillis() + 86400000), "PAID");
        invoiceManager.createInvoice(inv1);
        invoiceManager.createInvoice(inv2);
        List<Invoice> invoices = invoiceManager.getInvoicesForCustomer(1001L);
        for (Invoice invoice : invoices){
            System.out.println(invoice);
        }
        // Edge: No invoices
        System.out.println(ANSI_GREEN + "\n… but I don't have any invoices yet. So nothing is printed out." + ANSI_RESET);
        invoices = invoiceManager.getInvoicesForCustomer(9999L);

        System.out.println(ANSI_YELLOW + "\n================ EPIC 6 – Manage Charging Location ================" + ANSI_RESET);
        // US 6.1: Create valid
        System.out.println(ANSI_GREEN + "\nValid information entered when creating a new charging location." + ANSI_RESET);
        ChargingLocation funLocation = locationManager.createLocation("FunPark", "Party Street 99");

        // US 6.1: Invalid
        System.out.println(ANSI_GREEN + "\nInvalid information entered when creating a new charging location." + ANSI_RESET);
        ChargingLocation invalidAccount = locationManager.createLocation("", "");

        // US 6.2: Read all
        System.out.println(ANSI_GREEN + "\nList all locations!" + ANSI_RESET);
        List<ChargingLocation> allLocations = locationManager.getAllLocations();
        for (ChargingLocation loc : allLocations) {
            System.out.println(loc.toString());
        }
        // US 6.3: Update
        System.out.println(ANSI_GREEN + "\nI want to update the location name to MegaFunPark." + ANSI_RESET);
        funLocation.setName("MegaFunPark");
        System.out.println(locationManager.getLocation(funLocation.getName()));

        /*System.out.println(ANSI_YELLOW + "\n================ EPIC 8 – Manage Pricing ================" + ANSI_RESET);
        PricingManager pricingManager = new PricingManager();

        // US 8.1: Create pricing
        System.out.println(ANSI_GREEN + "\nValid information entered when creating a new pricing rule." + ANSI_RESET);
        PricingRules rule = new PricingRules();
        rule.setPricingId(1001);
        rule.setLocationId(1001);
        rule.setValidFrom(101025);
        rule.setValidTo(311224);
        rule.getPriceComponents().add(PriceComponent.KWH_AC);
        rule.getPriceComponents().add(PriceComponent.KWH_DC);
        rule.getPriceComponents().add(PriceComponent.CHARGING_MINUTES);
        pricingManager.addPricingRule(rule);

        // US 8.1: Invalid create
        System.out.println(ANSI_GREEN + "\nInvalid information entered when creating a new charging location." + ANSI_RESET);
        rule.setValidTo(321225);
        pricingManager.addPricingRule(rule);

        // US 8.2: Read current
        //System.out.println(ANSI_GREEN + "\nView current price: 0.30 EUR." + ANSI_RESET);
        // Assume associated with station

        // US 8.2: Display latest
        //System.out.println(ANSI_GREEN + "\nUpdate to 0.40, now shows latest." + ANSI_RESET);

        // US 8.2: Delete
        System.out.println(ANSI_GREEN + "\nI want to delete a rule." + ANSI_RESET);
        pricingManager.removePricingRule(1001);

        // US 8.3: Update immediate
        //System.out.println(ANSI_GREEN + "\nUpdate to 0.40: Ongoing sessions keep 0.30, new get 0.40." + ANSI_RESET);
        // Narrative, assume sessions

        // US 8.3: Schedule future
        //System.out.println(ANSI_GREEN + "\nUpdate all points to 0.40." + ANSI_RESET);

        // US 8.3: Multiple updates
        //System.out.println(ANSI_GREEN + "\nUpdate again to 0.50. Pricing party!" + ANSI_RESET);*/
    }
}