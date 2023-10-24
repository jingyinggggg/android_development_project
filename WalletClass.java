package com.example.transportpro;

public class WalletClass {
    int userId;
    double wallet_balance;

    public WalletClass(){

    }
    public WalletClass(int userId, double wallet_balance){
        this.userId = userId;
        this.wallet_balance = wallet_balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(int wallet_balance) {
        this.wallet_balance = wallet_balance;
    }
}
