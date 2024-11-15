package com.example.transportpro;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminHomePage extends AppCompatActivity {
    ImageView log_out;
    Button booking_page;
    Button warehouse_page;
    Button order_page;
    Button post_announcement;
    Button manage_user;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private FirebaseAnalytics mFirebaseAnalytics;

    private static final long TIMEOUT_PERIOD = 3 * 60 * 1000; // 3 minutes in milliseconds
    private Handler inactivityHandler = new Handler();
    private Runnable inactivityRunnable;
    private boolean isAutoLogout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        booking_page = (Button) findViewById(R.id.booking_page);
        warehouse_page = (Button)findViewById(R.id.warehouse);
        order_page = findViewById(R.id.order_request);
        post_announcement = findViewById(R.id.post_announcement);
        manage_user = findViewById(R.id.manage_user);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        log_out = findViewById(R.id.header_btn);

        // Set up inactivityRunnable for auto logout
        inactivityRunnable = () -> {
            isAutoLogout = true;
            logOutUser();
        };

        booking_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetInactivityTimer();
                redirect_booking(view);
            }
        });

        warehouse_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetInactivityTimer();
                redirect_warehouse(view);
            }
        });

        order_page.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                resetInactivityTimer();
                redirect_order(view);
            }
        });

        post_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetInactivityTimer();
                redirect_post(view);
            }
        });
        manage_user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                resetInactivityTimer();
                redirect_manage_user(view);
            }
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

    // Start the inactivity timer
    private void startInactivityTimer() {
        inactivityHandler.postDelayed(inactivityRunnable, TIMEOUT_PERIOD);
    }

    // Reset the inactivity timer
    private void resetInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable);
        startInactivityTimer();
    }

    // Log out the user
    private void logOutUser() {
        if (isAutoLogout) {
            Toast.makeText(this, "You have been logged out due to inactivity.", Toast.LENGTH_LONG).show();
        }

        // Clear shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to Login Page
        Intent intent = new Intent(AdminHomePage.this, LoginPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startInactivityTimer(); // Start the timer when activity resumes
    }

    @Override
    protected void onPause() {
        super.onPause();
        inactivityHandler.removeCallbacks(inactivityRunnable); // Stop the timer when activity is paused
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

    public void redirect_manage_user(View view) {
        Intent intent = new Intent(this, UserListAdmin.class);
        startActivity(intent);
    }


}