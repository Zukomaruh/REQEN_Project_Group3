package org.example;

import org.example.enums.SessionStatus;

public class User {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private SessionStatus.UserRole role; // OWNER, CUSTOMER
    private boolean active;

    public boolean validateCredentials(String inputPassword) {
        return false;
    }

    public boolean isOwner() {
        return false;
    }

    public boolean isCustomer() {
        return false;
    }

    public void updateProfile(String email, String username) {

    }
}
