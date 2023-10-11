package com.example.transportpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerBookingPage extends AppCompatActivity {

    ImageButton header_button;
    Button view_customer_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking);

        header_button = (ImageButton) findViewById(R.id.backArrow);
        view_customer_details = (Button) findViewById(R.id.view_details);

        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_homepage(view);
            }
        });

        view_customer_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { redirect_updateBooking(view); }
        });

    }

    public void redirect_homepage(View v){
        Intent homepageIntent = new Intent(CustomerBookingPage.this, AdminHomePage.class);
        startActivity(homepageIntent);
    }

    public void redirect_updateBooking(View v){
        Intent updateBookingIntent = new Intent(CustomerBookingPage.this, UpdateBookingAdmin.class);
        startActivity(updateBookingIntent);
    }
}
