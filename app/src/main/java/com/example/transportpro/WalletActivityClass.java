package com.example.transportpro;

public class WalletActivityClass {
    int userId;
    String payment_number;
    String activity;
    String date;
    String amount;

    public WalletActivityClass(){

    }
    public WalletActivityClass(int userId, String payment_number, String activity, String date, String amount){
        this.userId = userId;
        this.payment_number = payment_number;
        this.activity = activity;
        this.date = date;
        this.amount = amount;

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPayment_number() {
        return payment_number;
    }

    public void setPayment_number(String payment_number) {
        this.payment_number = payment_number;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
