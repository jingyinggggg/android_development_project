package com.example.transportpro;

public class OrderClass {
    int userId;
    String order_number;
    String date;
    String track_number;
    public OrderClass() {

    }

    public OrderClass(int userId, String order_number, String date, String track_number) {
        this.userId = userId;
        this.order_number = order_number;
        this.date = date;
        this.track_number = track_number;
    }

    public String getTrack_number() {
        return track_number;
    }

    public String getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    public String getOrder_number() {
        return order_number;
    }
}
