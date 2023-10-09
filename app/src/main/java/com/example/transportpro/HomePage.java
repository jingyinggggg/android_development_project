package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.transportpro.databinding.HomepageBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private BottomNavigationView bottomNavigationView;

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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        bottomNavigationView = findViewById(R.id.bottombar);

        bottomNavigationView.setSelectedItemId(R.id.bar_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bar_payment) {
                Intent intent = new Intent(HomePage.this,Payment.class).putExtra("from","homepage");
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_wallet) {
                Intent intent = new Intent(HomePage.this,WalletPage.class).putExtra("from","homepage");
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_profile) {
                Intent intent = new Intent(HomePage.this,AccountPage.class).putExtra("from","homepage");
                startActivity(intent);
            }
            return true;
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.sidebar_notification){
                        Log.i("Menu drawer Tag", "Notification is clicked");
                        Intent intent = new Intent(HomePage.this,NotificationPage.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (item.getItemId() == R.id.sidebar_setting) {
                        Log.i("Menu drawer tag", "Setting is clicked");
                        Intent intent = new Intent(HomePage.this,Setting.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (item.getItemId() == R.id.sidebar_logout) {
                        Log.i("Menu drawer tag", "log out is clicked");
                        Intent intent = new Intent(HomePage.this,LoginPage.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                return true;
            }
        });

    }
    public void homepage_redirect(View view){
        if (view.getId() == R.id.booking_btn){
            Intent intent = new Intent(HomePage.this,BookingPage.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.parcel_btn) {
            Intent intent = new Intent(HomePage.this,ParcelPage.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.neworder_btn) {
            Intent intent = new Intent(HomePage.this,neworder.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.orderHistory_btn) {
            Intent intent = new Intent(HomePage.this,Orderhistory.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.payment_btn) {
            Intent intent = new Intent(HomePage.this,Payment.class).putExtra("from","homepage");
            startActivity(intent);
        }
        else if (view.getId() == R.id.pricing_btn) {
            Intent intent = new Intent(HomePage.this,PricingPage.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.setting_btn) {
            Intent intent = new Intent(HomePage.this,Setting.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.profile_btn) {
            Intent intent = new Intent(HomePage.this,AccountPage.class).putExtra("from","homepage");
            startActivity(intent);
        }
        else if (view.getId() == R.id.noti_btn) {
            Intent intent = new Intent(HomePage.this,NotificationPage.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.topBar_wallet){
            Intent intent = new Intent(HomePage.this,WalletPage.class).putExtra("from","homepage");
            startActivity(intent);
        }
        else if(view.getId() == R.id.topBar_track){
            Intent intent = new Intent(HomePage.this,Orderhistory.class);
            startActivity(intent);
        }

    }
    public void onOpensideBar(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void onClosesideBar(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

}
