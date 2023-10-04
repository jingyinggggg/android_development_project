package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    private LinearLayout neworderBtn,orderHistory_btn,booking_btn;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        booking_btn = findViewById(R.id.booking_btn);
        booking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingPage(v);
            }
        });

        neworderBtn = findViewById(R.id.neworder_btn);
        neworderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,neworder.class);
                startActivity(intent);
            }
        });

        orderHistory_btn = findViewById(R.id.orderHistory_btn);
        orderHistory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,Orderhistory.class);
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.sidebar_notification){
                        Log.i("Menu drawer Tag", "Notification is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (item.getItemId() == R.id.sidebar_setting) {
                        Log.i("Menu drawer tag", "Setting is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (item.getItemId() == R.id.sidebar_logout) {
                        Log.i("Menu drawer tag", "log out is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                return true;
            }
        });

    }
    public void onOpensideBar(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void onClosesideBar(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void bookingPage(View view){
        Intent bookingPage = new Intent(HomePage.this, BookingPage.class);
        startActivity(bookingPage);
    }

}
