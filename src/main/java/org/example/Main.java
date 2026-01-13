package org.example;

import org.example.enums.*;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.ChargingLocationManager;
import org.example.managementClasses.ChargingProcessManager;
import org.example.managementClasses.InvoiceManager;
import org.example.managementClasses.PricingManager;
import org.example.managementClasses.StationManager;

import java.time.LocalDate;
import java.util.*;

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
        //managers
        ChargingProcessManager processManager = ChargingProcessManager.getInstance();
        AccountManager accountManager = AccountManager.getInstance();
        StationManager stationManager = StationManager.getInstance();
        ChargingLocationManager locationManager = ChargingLocationManager.getInstance();
        InvoiceManager invoiceManager = InvoiceManager.getInstance();
        //objects
        ChargingLocation Meidling123 = new ChargingLocation("Meidling123", "Meidling 123");
        ChargingStation StationA = new ChargingStation(Meidling123.getLocationId(), "Station A", StationType.AC, 50, 0.3f);
        ChargingLocation Simmering = new ChargingLocation("Simmering123", "Simmering 123");
        ChargingStation StationC = new ChargingStation(Meidling123.getLocationId(), "Station C", StationType.AC, 50, 0.3f);
        //add objects to managers
        stationManager.addLocation(Meidling123);
        stationManager.createStation(Meidling123.getLocationId(), StationA.getStationName(), StationA.getType(), StationA.getCapacity(), StationA.getPricing());
        //set variables
        Meidling123.setPricing(1.5f);
        System.out.println("\n=============================================================");

        System.out.println(ANSI_YELLOW + "\n================ EPIC 1 – Account Management ================" + ANSI_RESET);
        //1.1
        System.out.println(ANSI_GREEN + "\nInvalid user credentials entered when creating a new account" + ANSI_RESET);
        Account invalid = new Account("Max Mustermann", "maxmustermann.com", "passw0rd123!", AccountType.CUSTOMER);
        System.out.println(ANSI_GREEN + "\nValid user credentials entered when creating a new account" + ANSI_RESET);
        Account account = new Account("Max Mustermann", "max@mustermann.com", "passw0rd123!", AccountType.CUSTOMER);
        accountManager.addAccount(account);

        //1.2
        System.out.println(ANSI_GREEN + "\nI want to read my account details." + ANSI_RESET);
        long userId = account.getUserId();
        System.out.println(accountManager.readAccount(userId));

        //1.3
        System.out.println(ANSI_GREEN + "\nI want to update my username to \"Maxi\"." + ANSI_RESET);
        accountManager.updateAccount("Maxi", userId);
        System.out.println(accountManager.readAccount(userId));

        //1.4
        System.out.println(ANSI_GREEN + "\nI try to delete my account..." + ANSI_RESET);
        //Exception in thread
        //accountManager.deleteAccount(updatedAccount.getUserId(), updatedAccount.getPassword());
        System.out.println(ANSI_GREEN + "\n… but forgot that we still had an active charging process!" + ANSI_RESET);
        String password = account.getPassword();
        accountManager.readAccount(userId).setChargingProcess(0);
        accountManager.deleteAccount(userId, password);

        System.out.println(ANSI_YELLOW + "\n================ EPIC 2 – Prepaid Balance Management ================" + ANSI_RESET);
        //2.1
        System.out.println(ANSI_GREEN + "\nI want to start charging and select the payment method \"cash\" and the amount \"600\".\nBut they are invalid." + ANSI_RESET);
        account.setPaymentMethod("cash");
        account.setPrepaidAmount("600");
        System.out.println(ANSI_GREEN + "\nI correct it and get my selection confirmed." + ANSI_RESET);
        account.setPaymentMethod("paypal");
        account.setPrepaidAmount("20");
        account.getPaymentConfirmationMessage();
        accountManager.updatePrepaidBalance(account);

        //2.2
        System.out.println(ANSI_GREEN + "\nI want to charge my car at a station.\nBut my balance is too low to cover charging costs." + ANSI_RESET);
        account.setPrepaidBalance(0);
        accountManager.addAccount(account);
        ChargingProcess process = processManager.startProcess(
                userId,
                StationA.getStationId(),
                StationA.getStationName(),
                ChargingMode.STANDARD,
                20,
                90,
                50,
                60
        );
        System.out.println(ANSI_GREEN + "\nI top my balance up so that I can start charging." + ANSI_RESET);
        accountManager.readPrepaidBalance(account);
        account.setPrepaidAmount("100");
        account.getPaymentConfirmationMessage();
        accountManager.updatePrepaidBalance(account);
        process = processManager.startProcess(
                userId,
                StationA.getStationId(),
                StationA.getStationName(),
                ChargingMode.STANDARD,
                20,
                90,
                50,
                60
        );
        process.complete();

        System.out.println();
        System.out.println(ANSI_YELLOW + "\n================ EPIC 3 – Manage Charging Station ================" + ANSI_RESET);
        ChargingLocation Hietzing123 = new ChargingLocation("Hietzing 123", "Hietzing 123");
        long hietzing123Id = Hietzing123.getLocationId();
        locationManager.addLocation(Hietzing123);
        stationManager.addLocation(Hietzing123);

        // US 3.1: Create charging station
        System.out.println(ANSI_GREEN + "\nAs the owner, I create a charging station without a name …" + ANSI_RESET);
        //Exception thread
        //ChargingStation invalidStation = stationManager.createStation(demoLocationId, null, StationType.AC, 100, 0.35);
        System.out.println(ANSI_GREEN + "\n… but forgot that a charging station must be given a name!" + ANSI_RESET);
        ChargingStation StationB = stationManager.createStation(hietzing123Id, "StationB", StationType.AC, 100, 0.35);
        stationManager.addLocation(Hietzing123);
        Hietzing123.setLocationId(hietzing123Id);

        // US 3.2: Read station information
        System.out.println(ANSI_GREEN + "\nI want to check the information of \"StationB\"." + ANSI_RESET);
        System.out.println(StationB.getInformation());

        // US 3.2: Read all stations
        System.out.println(ANSI_GREEN + "\nI want to check the information of all stations in the network." + ANSI_RESET);
        System.out.println(stationManager.getAllStationInformation());

        // US 3.3: Update station
        System.out.println(ANSI_GREEN + "\nUpdate pricing of \"StationB\" to 1.2 and check if latest shows." + ANSI_RESET);
        stationManager.updatePricingByName(StationB.getStationName(), 1.20);
        System.out.println(StationB.getInformation());

        // US 7.4: Delete station
        System.out.println(ANSI_GREEN + "\nDelete the charging station \"StationC\"!" + ANSI_RESET);
        boolean stationDeletionStatus = stationManager.deleteStationByStationId(StationC.getStationId(), processManager);
        if(stationDeletionStatus) {
            System.out.println("The charging station was successfully deleted!");
        }
        else {
            System.out.println("The charging station was not successfully deleted!");
        }

        System.out.println(ANSI_YELLOW + "\n================ EPIC 4 – Charging Process Management ================" + ANSI_RESET);
        long customerId = account.getUserId();
        long stationId = StationB.getStationId();
        String stationName = StationB.getStationName();

        // US 4.1: Start charging to target
        System.out.println(ANSI_GREEN + "\nUpdate battery percentage to 80%." + ANSI_RESET);
        process = processManager.startProcess(customerId, stationId, stationName, ChargingMode.FAST, 40, 80, 50, 60);
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
        StationB.setStatus(StationStatus.AVAILABLE);
        System.out.println(ANSI_GREEN + "\nBefore starting charging, the station is: "+StationB.getStatus() + ANSI_RESET);
        process = processManager.startProcess(customerId, stationId, stationName, ChargingMode.ECO, 10, 50, 20, 90);
        System.out.printf(ANSI_GREEN + "\nAfter starting charging, the station is: %s and the session is: %s",StationB.getStatus(), process.getStatus() + ANSI_RESET);

        // US 4.3: On finish
        process.complete();
        System.out.println();
        System.out.printf(ANSI_GREEN + "\nWhen done charging, the station is: %s and the session is: %s",StationB.getStatus(), process.getStatus() + ANSI_RESET);

        // US 4.3: Reject on unavailable
        StationB.setStatus(StationStatus.MAINTENANCE);
        System.out.println();
        System.out.println(ANSI_GREEN + "\nWhen the station is under maintenance, charging is not possible and the status is set to: "+StationB.getStatus() + ANSI_RESET);
        // processManager.startProcess would return null for invalid

        System.out.println(ANSI_YELLOW + "\n================ EPIC 5 – Invoice Management ================" + ANSI_RESET); // Assuming EPIC 5 is Invoices based on US 5.x

        // US 5.1: Read invoices
        System.out.println(ANSI_GREEN + "\nI want to read all my invoices from different charging stations …" + ANSI_RESET);
        List<Invoice> invoices = invoiceManager.getInvoicesForCustomer(account.getUserId());
        for (Invoice invoice : invoices){
            System.out.println(invoice);
        }
        // Edge: No invoices
        System.out.println(ANSI_GREEN + "\n… but I don't have any invoices yet. So nothing is printed out." + ANSI_RESET);
        invoices = invoiceManager.getInvoicesForCustomer(9999L);

        // US 9.4: Delete
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -7);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Invoice inv3 = new Invoice(1001L, "Station B", "DC", 50, 30, 0.80, 40.00, cal.getTime(), "PAID");
        invoiceManager.createInvoice(inv3);
        System.out.println(ANSI_GREEN + "\nNevermind! There is one invoice. But it is about to become seven years old. Will it be deleted?" + ANSI_RESET);
        boolean invoiceDeletionStatus = invoiceManager.deleteInvoice(inv3);
        if(invoiceDeletionStatus) {
            System.out.println("The invoice was successfully deleted!");
        }
        else {
            System.out.println("The invoice was not successfully deleted!");
        }

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

        // US 6.4: Delete
        System.out.println(ANSI_GREEN + "\nI want to delete the charging location \"MegaFunPark\"." + ANSI_RESET);
        boolean locationDeletionStatus = locationManager.deleteLocation(funLocation.getLocationId());
        if(locationDeletionStatus) {
            System.out.println("The charging location was successfully deleted!");
        }
        else {
            System.out.println("The charging location was not successfully deleted!");
        }

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
        //System.out.println(ANSI_GREEN + "\nUpdate again to 0.50. Pricing party!" + ANSI_RESET);
    */}
}