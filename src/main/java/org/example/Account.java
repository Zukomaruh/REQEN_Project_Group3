package org.example;

import org.example.enums.AccountType;
import org.example.enums.ChargingMode;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.ChargingProcessManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Account {
    private long userId;
    private String username;
    private String email;
    private String password;
    private AccountType role; // OWNER, CUSTOMER
    private boolean active;
    private float prepaidAmount = 0;
    private String paymentMethod;
    private float prepaidBalance = 0;
    private ArrayList <String> paymentMethods = new ArrayList<String>
            (Arrays.asList("credit card", "debit card", "paypal", "bank transfer", "apple pay", "google pay"));


    private float balance = 0f;
    private boolean chargingProcess = false;
    private LocalDateTime chargingProcessTime;

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

    public void setUserId(long userId) {this.userId = userId;}

    public boolean setPrepaidAmount(String prepaidAmount) {
        if (prepaidAmount.isEmpty()) {System.out.println("Amount must not be empty"); return false;}
        if (!prepaidAmount.matches("^\\d+(.\\d+)?$")) {System.out.println("Amount must contain only numbers"); return false;}
        float amount = Float.parseFloat(prepaidAmount);
        if (amount < 20) {System.out.println("Amount must be at least 20"); return false;}
        if (amount > 500) {System.out.println("Amount must not exceed 500"); return false;}
        this.prepaidAmount = amount;
        return true;
    }

    public boolean setPaymentMethod(String paymentMethod) {
        if (paymentMethod.isEmpty()) {System.out.println("Payment Method must not be empty"); return false;}
        if (paymentMethod.matches(".*\\d.*") || !paymentMethod.matches("[a-zA-Z ]+")) {System.out.println("Payment Method must contain only letters"); return false;}
        if (!paymentMethods.contains(paymentMethod)) {System.out.println("Select valid payment method"); return false;}
        this.paymentMethod = paymentMethod;
        return true;
    }

    public void setPrepaidBalance(float prepaidBalance) {
        this.prepaidBalance = prepaidBalance;
    }

    public float getPrepaidBalance() {
        return prepaidBalance;
    }

    public String getPaymentMethod() {return paymentMethod;}

    public long getUserId() {return userId;}

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

    public float getPrepaidAmount() {return prepaidAmount;}

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

    public void getPaymentConfirmationMessage() {
        System.out.println("Selected amount: "+this.prepaidAmount+ "%nSelected payment method: "+this.paymentMethod);
    }

    public boolean canStartCharging(ChargingStation chargingStation) {
        float pricing = chargingStation.getPricing();
        if (this.getPrepaidBalance() < pricing) {
            System.out.println("Charging terminated: Insufficient balance");
            return false;
        }
        return true;
    }

    public void updateBalance(float amount) {
        if(amount > 0){
            this.balance += amount;
        } else if (role == AccountType.OWNER) {
            this.balance += amount;
        }
    }

    public float getBalance() {
        return balance;
    }

    public void startChargingProcess(long stationId, String stationName, ChargingMode mode,int currentBattery, int targetBattery, int powerKW, int timeToFullMinutes) {
        ChargingProcessManager.getInstance().startProcess(userId, stationId, stationName, mode, currentBattery, targetBattery, powerKW, timeToFullMinutes);
    }

    public void setChargingProcess(int timeToFinish) {
        if(!chargingProcess){
            chargingProcess = true;
            this.chargingProcessTime = LocalDateTime.now().plusMinutes(timeToFinish);
        }
    }

    public boolean readChargingProcess(){
        if(chargingProcess && chargingProcessTime.isAfter(LocalDateTime.now())){
            this.chargingProcess = false;
        }
        return chargingProcess;
    }
}