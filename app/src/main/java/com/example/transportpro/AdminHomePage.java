package com.example.transportpro;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminHomePage extends AppCompatActivity {

    Button booking_page;
    Button warehouse_page;

    Button order_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        booking_page = (Button) findViewById(R.id.booking_page);
        warehouse_page = (Button)findViewById(R.id.warehouse);
        order_page = findViewById(R.id.order_request);

        booking_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_booking(view);
            }
        });

        warehouse_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_warehouse(view);
            }
        });

        order_page.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){redirect_order(view);}
        });

    }

    public void redirect_booking(View v) {
        Intent bookingIntent = new Intent(AdminHomePage.this, CustomerBookingPage.class);
        startActivity(bookingIntent);
    }

    public void redirect_warehouse(View v){
        Intent warehouseIntent = new Intent(AdminHomePage.this, WarehouseAdmin.class);
        startActivity(warehouseIntent);
    }

    public void redirect_order(View v){
        Intent orderIntent = new Intent(AdminHomePage.this, OrderAdmin.class);
        startActivity(orderIntent);
    }
}