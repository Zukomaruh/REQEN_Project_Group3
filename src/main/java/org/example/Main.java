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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
        AccountManager accountManager = AccountManager.getInstance();
        System.out.println(ANSI_GREEN + "\nI am Max. I am new to E.Power and want to register as a new User.\nBut I enter invalid user credentials." + ANSI_RESET);
        Account nonvalidAccount = new Account("123", "foo", "lessthan12", AccountType.CUSTOMER);
        System.out.println(ANSI_GREEN + "\nI correct it and get my registration confirmed." + ANSI_RESET);
        Account account = new Account("Max Mustermann", "max.mustermann@gmail.com", "123456789012", AccountType.CUSTOMER);
        accountManager.addAccount(account);

        //1.2
        System.out.println(ANSI_GREEN + "\nNow, I want to check on my account information." + ANSI_RESET);
        long userId = account.getUserId();
        Account readAccount = accountManager.readAccount(userId);
        System.out.println(readAccount);
        System.out.println(ANSI_GREEN + "\nThere I see: I put in the wrong email address! So I change it." + ANSI_RESET);
        account.setEmail("max.new@gmail.com");
        System.out.println(readAccount);

        //1.3
        System.out.println(ANSI_GREEN + "\nSome time later, my wife wants to take over my account, so we just change the username." + ANSI_RESET);
        accountManager.updateAccount("Max' wife", userId);
        Account updatedAccount = accountManager.readAccount(userId);
        System.out.println(updatedAccount);

        //1.4
        System.out.println(ANSI_GREEN + "\nShe suddenly gets anxious about AI having all our data and decides to delete our account …" + ANSI_RESET);
        //accountManager.deleteAccount(readAccount.getUserId(), readAccount.getPassword());
        System.out.println(ANSI_GREEN + "\n… but she forgot that we still had an active charging process!" + ANSI_RESET);
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
        chargingStation.setPricing(600);
        account.canStartCharging(chargingStation);
        System.out.println(ANSI_GREEN + "\nI top my balance up so that I can start charging." + ANSI_RESET);
        accountManager.readPrepaidBalance(account);
        account.setPrepaidAmount("100");
        account.getPaymentConfirmationMessage();
        accountManager.updatePrepaidBalance(account);
        account.canStartCharging(chargingStation);
    }
}