package com.example.transportpro;

public class BookingClass {
    int userId;
    String track_number;
    String category;
    String delivery_by;
    int quantity;
    String description;
    int collected;
    String date;
    int isChecked;
    double weight;
    int isPackOrder;
    public BookingClass(){

    }

    public BookingClass(int userId, String track_number, String category, String delivery_by, int quantity, String description, int collected, String date, int isChecked
                        ,double weight, int isPackOrder){
        this.date = date;
        this.collected = collected;
        this.userId = userId;
        this.track_number = track_number;
        this.category = category;
        this.delivery_by = delivery_by;
        this.quantity = quantity;
        this.description = description;
        this.isChecked = isChecked;
        this.weight = weight;
        this.isPackOrder = isPackOrder;
    }

    public double getWeight() {
        return weight;
    }

    public int getIsPackOrder() {
        return isPackOrder;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTrack_number() {
        return track_number;
    }

    public void setTrack_number(String track_number) {
        this.track_number = track_number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDelivery_by() {
        return delivery_by;
    }

    public void setDelivery_by(String delivery_by) {
        this.delivery_by = delivery_by;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
