package org.example;

import org.example.enums.AccountType;
import org.example.enums.StationType;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.ChargingLocationManager;
import org.example.managementClasses.StationManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to EloNet");
        // User Story 1.1 Create Account
        Account validAccount = new Account("Max Mustermann", "max.musterman@gmail.com", "123456789012", AccountType.CUSTOMER);
        AccountManager.getInstance().addAccount(validAccount);
        long userId = validAccount.getUserId(); // Because ID is a long (timestamp) we store it - but its unique and secure ;)

        // User Story 1.2 read Account
        System.out.println("Read Account:");
        System.out.println(AccountManager.getInstance().readAccount(userId).toString());
        System.out.println();

        // User Story 1.3 update Account
        System.out.println("Updated Account");
        AccountManager.getInstance().updateAccount(false, userId);
        AccountManager.getInstance().updateAccount("Maxime Musterfrau", userId);
        System.out.println(AccountManager.getInstance().readAccount(userId).toString());
        System.out.println();

        // User Story 6.1 Create Charging Location
        System.out.println("Create Charging Location");
        ChargingLocation validLocation = new ChargingLocation("Westbahnhof", "Mariahilferstraße 187, 1150 Wien");
        ChargingLocationManager.getInstance().addLocation(validLocation);

        // User Story 6.2 Read Charging Location
        System.out.println("Read Charging Location");
        System.out.println(ChargingLocationManager.getInstance().getAllLocations().get(0).toString());
        System.out.println();

        // User Story 6.3 Update Charging Location
        System.out.println("Updated Charging Location:");
        ChargingLocationManager.getInstance().getAllLocations().get(0).setName("Hauptzentrale");
        System.out.println();

        // User Story 7.1 Create Charging Station
        System.out.println("Create Charging Station:");
        ChargingStation validStation = new ChargingStation("station01", StationType.AC, 150, 0.35F);
        //StationManager.getInstance().createStation(validLocation.getLocationId(), "station01", StationType.AC, 150, 0.35F);
        System.out.println();

        // User Story 7.2 Read Charging Station
        System.out.println("Read Charging Station");
        System.out.println(StationManager.getInstance().getAllStationInformation());
        System.out.println();

        // User Story 7.3 Update Charging Station
        System.out.println("Update Charging Station");
        validStation.setPricing(0.50F);
        System.out.println(validStation.toString());
        System.out.println();
    }
}
