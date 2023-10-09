package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BookingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_page);

        //Show message for user when they submit a booking
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Booking submitted!");

//                Intent home = new Intent(BookingPage.this, LoginPage.class);
                Intent home = new Intent(BookingPage.this, HomePage.class);
//                Intent home = new Intent(BookingPage.this, NotificationPage.class);
                startActivity(home);
            }
        });
    }

    public void showToast(String text){
        Toast.makeText(BookingPage.this, text, Toast.LENGTH_SHORT).show();
    }

    public void backLoginPage(View view){
        Intent loginPage = new Intent(this, LoginPage.class);
        startActivity(loginPage);
    }

    public void backHomePage(View view){
        Intent homePage = new Intent(BookingPage.this, HomePage.class);
        startActivity(homePage);
    }
}