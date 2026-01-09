package org.example.managementClasses;

import org.example.Account;
import org.example.enums.AccountType;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private static final AccountManager INSTANCE = new AccountManager();
    public static AccountManager getInstance(){return INSTANCE;}
    private final List<Account> accounts;
    public AccountManager() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        if(!accounts.contains(account)){
            accounts.add(account);
        }
    }

    public void updatePrepaidBalance(Account account){
        float amount = account.getPrepaidAmount();
        if (account.getPrepaidBalance() == 0){
            account.setPrepaidBalance(amount);
            System.out.printf("Prepaid Balance successfully updated to %.2f!", amount);
        } else {
            float prepaidBalance = Math.round(account.getPrepaidBalance() + amount);
            account.setPrepaidBalance(prepaidBalance);
            System.out.printf("Prepaid Balance successfully updated to %.2f!", prepaidBalance);
        }
    }


    public void createAccount(String username, String email, String password, AccountType type){
        Account account = new Account(username, email, password, type);
        addAccount(account);
    }

    /*public Account login(String username, String password){
        for (Account account : accounts){
            if (account.getUsername().equals(username)
                    && account.getPassword().equals(password)
                        && account.getActive())
            {return account;}
        }
        return null;
    }*/

    public Account readAccount(long userID) {
        for (Account account : accounts) {
            if (account.getUserId() == userID) {
                return account;
            }
        }
        System.out.println("No Account with this ID exists");
        return null;
    }

    public List<Account> readAccounts(){
        return accounts;
    }

    public void updateAccount(String username, long userID){
        if(!username.isEmpty()){
            for (Account account : accounts) {
                if (account.getUserId() == userID) {
                    account.setUsername(username);
                }
            }
        }
    }

    public void updateAccount(AccountType role, long userId) {
        for (Account account : accounts) {
            if (account.getUserId() == userId) {
                account.setType(role);
            }
        }
    }

    public void updateAccount(boolean active, long userId) {
        for (Account account : accounts) {
            if (account.getUserId() == userId) {
                account.setActive();
            }
        }
    }
}
