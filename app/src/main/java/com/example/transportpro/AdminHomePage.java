package com.example.transportpro;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminHomePage extends AppCompatActivity {
    ImageView log_out;
    Button booking_page;
    Button warehouse_page;
    Button order_page;
    Button post_announcement;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        booking_page = (Button) findViewById(R.id.booking_page);
        warehouse_page = (Button)findViewById(R.id.warehouse);
        order_page = findViewById(R.id.order_request);
        post_announcement = findViewById(R.id.post_announcement);
        log_out = findViewById(R.id.header_btn);

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

        post_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { redirect_post(view); }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", "admin");
                bundle.putString("activity", "Log out");

                mFirebaseAnalytics.logEvent("LogOut", bundle);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent bookingIntent = new Intent(AdminHomePage.this, LoginPage.class);
                startActivity(bookingIntent);
            }
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
    public void redirect_post(View v){
        Intent postIntent = new Intent(AdminHomePage.this, PostAnnouncement.class);
        startActivity(postIntent);
    }
}