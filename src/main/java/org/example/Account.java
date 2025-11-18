package org.example;

import org.example.enums.SessionStatus;

public class Account {
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

    public Long getUserId() {
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

    public SessionStatus.UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(SessionStatus.UserRole role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Account(String username, String email, String password, SessionStatus.UserRole role, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
    }
}