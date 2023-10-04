package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NotificationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);
    }

    public void backLoginPage(View view) {
        Intent loginPage = new Intent(this, LoginPage.class);
        startActivity(loginPage);
    }
}