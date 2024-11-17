package com.example.transportpro;

public class User {
    int userId;
    int isAdminAcc;
    int isDeletedAcc;
    String fullname;
    String username;
    String email;
    String contact;
    String password;
    private boolean hasAgreedToPrivacyPolicy;

    public User() {

    }

    public User(int isDeletedAcc,int isAdminAcc, int userId, String fullname, String username, String email, String contact, String password, boolean hasAgreedToPrivacyPolicy) {
        this.isDeletedAcc = isDeletedAcc;
        this.isAdminAcc = isAdminAcc;
        this.userId = userId;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.contact = contact;
        this.password = password;
        setHasAgreedToPrivacyPolicy(hasAgreedToPrivacyPolicy);
    }

    public int getIsDeletedAcc(){
        return isDeletedAcc;
    }
    public int getIsAdminAcc(){
        return isAdminAcc;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Example setter
    public void setHasAgreedToPrivacyPolicy(boolean hasAgreedToPrivacyPolicy) {
        this.hasAgreedToPrivacyPolicy = hasAgreedToPrivacyPolicy;
    }

    // Example getter
    public boolean hasAgreedToPrivacyPolicy() {
        return hasAgreedToPrivacyPolicy;
    }
}