package org.example;

import org.example.enums.AccountType;
import org.example.managementClasses.AccountManager;

public class Main {
    public static void main(String[] args) {
        Account account1 = new Account("Max Mustermann", "max.mustermann@email.com","123456789012", AccountType.CUSTOMER);
        System.out.println(account1.toString());
        AccountManager system = new AccountManager();
        system.createAccount(account1);
        Account account2 = new Account("Fiona Fantasie", "fion.fanta@email.com", "123456789012", AccountType.OWNER);
        system.createAccount(account2);
        System.out.println(system.readAccount(account2.getUserId()));
    }
}
