package com.example.transportpro;

public class OrderHistoryClass {
    int userId;
    String order_number;
    String order_location;
    String category;
    String transport_type;
    int sensitive_item;
    String receiver_name;
    String receiver_contact;
    String receiver_email;
    String receiver_state;
    String eastORwest;
    String receiver_postcode;
    String receiver_add1;
    String receiver_add2;
    String receiver_add3;
    String order_status;
    double weight;
    String date;
    int parcel_quantity;
    double price;
    int isPay;

    public OrderHistoryClass(){

    };
    public OrderHistoryClass(int userId, String order_number, String order_location, String category, String transport_type, int sensitive_item, String receiver_name, String receiver_contact, String receiver_email, String receiver_state, String eastORwest, String receiver_postcode,
                             String receiver_add1, String receiver_add2, String receiver_add3, String order_status, double weight, String date, int parcel_quantity,
                             double price, int isPay) {
        this.userId = userId;
        this.order_number = order_number;
        this.order_location = order_location;
        this.category = category;
        this.transport_type = transport_type;
        this.sensitive_item = sensitive_item;
        this.receiver_name = receiver_name;
        this.receiver_contact = receiver_contact;
        this.receiver_email = receiver_email;
        this.receiver_state = receiver_state;
        this.eastORwest = eastORwest;
        this.receiver_postcode = receiver_postcode;
        this.receiver_add1 = receiver_add1;
        this.receiver_add2 = receiver_add2;
        this.receiver_add3 = receiver_add3;
        this.order_status = order_status;
        this.weight = weight;
        this.date = date;
        this.parcel_quantity = parcel_quantity;
        this.price = price;
        this.isPay = isPay;
    }

    public String getOrder_location() {
        return order_location;
    }

    public String getReceiver_state() {
        return receiver_state;
    }

    public String getEastORwest() {
        return eastORwest;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getIsPay() {
        return isPay;
    }

    public int getUserId() {
        return userId;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getTransport_type() {
        return transport_type;
    }

    public int getSensitive_item() {
        return sensitive_item;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public String getReceiver_contact() {
        return receiver_contact;
    }

    public String getReceiver_email() {
        return receiver_email;
    }

    public String getReceiver_postcode() {
        return receiver_postcode;
    }

    public String getReceiver_add1() {
        return receiver_add1;
    }

    public String getReceiver_add2() {
        return receiver_add2;
    }

    public String getReceiver_add3() {
        return receiver_add3;
    }

    public String getOrder_status() {
        return order_status;
    }

    public double getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public int getParcel_quantity() {
        return parcel_quantity;
    }
}
