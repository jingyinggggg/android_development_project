package com.example.transportpro;

public class NotificationClass {

    int userId;
    int booking_status;
    int parcel_status;
    int order_status;
    int is_maintainence;


    public NotificationClass(){
        // Default constructor required for Firebase
    }

    public NotificationClass(int userId, int booking_status, int parcel_status, int order_status, int is_maintainence){
        this.userId = userId;
        this.booking_status = booking_status;
        this.parcel_status = parcel_status;
        this.order_status = order_status;
        this.is_maintainence = is_maintainence;
    }

    //Getter
    public int getUserId() {return userId; }

    public int getBooking_status() { return booking_status; }

    public int getParcel_status() { return parcel_status; }

    public int getOrder_status() { return order_status; }

    public int getIs_maintainence() { return is_maintainence; }



    //Setter
    public void setUserId(int userId) {this.userId = userId;}

    public void setBooking_status(int booking_status) {this.booking_status = booking_status;}

    public void setParcel_status(int parcel_status) {this.parcel_status = parcel_status;}

    public void setOrder_status(int order_status) {this.order_status = order_status;}

    public void setIs_maintainence(int is_maintainence) {this.is_maintainence = is_maintainence;}
}
