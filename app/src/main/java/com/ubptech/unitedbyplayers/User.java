package com.ubptech.unitedbyplayers;

/**
 * Created by Kylodroid on 28-05-2020.
 */
public class User {
    private String fullName, phoneNumber, email, method, dateCreated, lastActive;

    User(String fullName, String phoneNumber, String email, String method,
         String dateCreated, String lastActive){
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.method = method;
        this.dateCreated = dateCreated;
        this.lastActive = lastActive;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLastActive() {
        return lastActive;
    }

    public String getMethod() {
        return method;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
