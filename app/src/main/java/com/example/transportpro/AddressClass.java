package com.example.transportpro;

public class AddressClass {
    int userId;
    String username;
    String fullname;
    String contact;
    String email;
    String state;
    String postcode;
    String address1;
    String address2;
    String address3;

    public AddressClass(){

    }
    public AddressClass(int userId ,String username ,String fullname ,String contact ,String email , String state, String postcode ,String address1 ,String address2 ,String address3){
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
        this.contact = contact;
        this.email = email;
        this.state = state;
        this.postcode = postcode;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }

    public String getState() {
        return state;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }
}

