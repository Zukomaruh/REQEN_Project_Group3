package org.example;

import org.example.enums.AccountType;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.ChargingLocationManager;

public class Main {
    public static void main(String[] args) {
        Account test = new Account("max", "max.gax@test.com", "123456789012345", AccountType.CUSTOMER);
        System.out.println(test.toString());
    }
}
