package com.example.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Setting extends AppCompatActivity {
    public ImageButton account;
    public ImageButton password;
    public ImageButton delete;
    public ImageButton logout;
    private Button test;
    private Button testPayment;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        account = (ImageButton)findViewById(R.id.goAccountButton);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAccountPage(view);
            }
        });

        password = (ImageButton)findViewById(R.id.goPasswordButton);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPasswordPage(view);
            }
        });

        delete = (ImageButton)findViewById(R.id.goDeleteAccountButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goDeleteAccountPage(view);
            }
        });

        test = (Button)findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAnnouncement(view);
            }
        });

        testPayment = (Button)findViewById(R.id.testPayment);
        testPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment(view);
            }
        });


    }
    public void goAccountPage(View view){
        Intent accountPage = new Intent(this, AccountPage.class);
        startActivity(accountPage);
    }

    public void goPasswordPage(View view){
        Intent password_securityPage = new Intent(this, PasswordSecurityPage.class);
        startActivity(password_securityPage);
    }

    public void goDeleteAccountPage(View view){
        Intent delete_account_page = new Intent(this, DeleteAccountPage.class);
        startActivity(delete_account_page);
    }

    public void postAnnouncement(View view){
        Intent post_announcement = new Intent(this, PostAnnouncement.class);
        startActivity(post_announcement);
    }

    public void payment(View view){
        Intent payment = new Intent(this, Payment.class);
        startActivity(payment);
    }
}