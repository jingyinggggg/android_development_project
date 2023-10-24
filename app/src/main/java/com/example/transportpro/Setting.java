package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Setting extends AppCompatActivity {
    public LinearLayout account;
    public LinearLayout password;
    public LinearLayout delete;
    public LinearLayout logout;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        account = (LinearLayout)findViewById(R.id.goAccountButton);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAccountPage(view);
            }
        });

        password = (LinearLayout)findViewById(R.id.goPasswordButton);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPasswordPage(view);
            }
        });

        delete = (LinearLayout)findViewById(R.id.goDeleteAccountButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goDeleteAccountPage(view);
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        logout = (LinearLayout)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(Setting.this,LoginPage.class);
                startActivity(intent);

                logout(view);
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

    public void backHomePage(View view){
        Intent loginPage = new Intent(this, HomePage.class);
        startActivity(loginPage);
    }
    public void logout(View view){
        Intent logout = new Intent(this, LoginPage.class);
        startActivity(logout);
    }
}