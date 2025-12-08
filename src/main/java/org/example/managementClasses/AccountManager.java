package org.example.managementClasses;

import org.example.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private List<Account> accounts;

    public AccountManager() {
        this.accounts = new ArrayList<>();
    }

    public void createAccount(Account account) {
        accounts.add(account);
    }

    public String readAccount(long userID) {
        for (Account account : accounts) {
            if (account.getUserId() == userID) {
                return account.toString();
            }
        }
        return "No Account with this ID exists";
    }
}
