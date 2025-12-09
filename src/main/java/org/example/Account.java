package org.example;

import org.example.enums.AccountType;
import org.example.enums.SessionStatus;
import org.example.managementClasses.AccountManager;

import java.util.regex.Pattern;

public class Account {
    private long userId;
    private String username;
    private String email;
    private String password;
    private AccountType role; // OWNER, CUSTOMER
    private boolean active;

    private boolean isInputValid (String username, String email, String password, AccountType role){
        if (username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                role == null) {
            return false;
        }
        if (!username.matches("[a-zA-Z\\s]+")) {
            return false;
        }
        if (password.length() < 12) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    public Account(String username, String email, String password, AccountType role) {
        if(isInputValid(username, email, password, role)) {
            this.userId = System.currentTimeMillis();
            setUsername(username);
            setEmail(email);
            setPassword(password);
            setRole(role);
            this.active = true;
            AccountManager.getInstance().addAccount(this);
            System.out.println("Your account has been created");
        }else{
            System.out.println("Please enter valid username, email or password");
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if(Pattern.compile(emailRegex).matcher(email).matches()){
            this.email = email;
        }
    }

    public void setPassword(String password) {
        if(password.length() >= 12){
            this.password = password;
        }
    }

    public void setRole(AccountType role) {
        this.role = role;
    }

    public void setActive() {
        this.active = !active;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getRole() {
        return role;
    }

    public boolean getActive() {
        return active;
    }

    @Override
    public String toString() {
        return "userID: " + getUserId() + "\n" +
                "username: " + getUsername() + "\n" +
                "email: " + getEmail() + "\n" +
                "role: " + getRole() + "\n" +
                "status: " + (getActive() ? "active" : "inactive");
    }

    public void setType(AccountType role) {
        this.role = role;
    }
}