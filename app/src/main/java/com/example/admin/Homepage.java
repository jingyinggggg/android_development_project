package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.transportpro.R;

public class Homepage extends AppCompatActivity {

    Button booking_page;
    Button warehouse_page;

    Button announcement_page;

    Button order_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        announcement_page = findViewById(R.id.post_announcement);
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
        Intent bookingIntent = new Intent(Homepage.this, CustomerBookingPage.class);
        startActivity(bookingIntent);
    }

    public void redirect_warehouse(View v){
        Intent warehouseIntent = new Intent(Homepage.this, Warehouse.class);
        startActivity(warehouseIntent);
    }

    public void redirect_order(View v){
        Intent orderIntent = new Intent(Homepage.this, Order.class);
        startActivity(orderIntent);
    }
}