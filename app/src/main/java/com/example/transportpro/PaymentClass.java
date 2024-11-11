package com.example.transportpro;

public class PaymentClass {
    int userId;
    String order_number;
    String invoice_number;
    String date;
    String payment_method;
    double payment_amount;
    String payment_status;

    public PaymentClass(){

    }

    public PaymentClass(int userId, String order_number, String invoice_number, String date, String payment_method, double payment_amount, String payment_status) {
        this.userId = userId;
        this.order_number = order_number;
        this.invoice_number = invoice_number;
        this.date = date;
        this.payment_method = payment_method;
        this.payment_amount = payment_amount;
        this.payment_status = payment_status;
    }

    public int getUserId() {
        return userId;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public String getDate() {
        return date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public double getPayment_amount() {
        return payment_amount;
    }

    public String getPayment_status() {
        return payment_status;
    }
}
