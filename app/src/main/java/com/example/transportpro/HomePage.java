package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

public class HomePage extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView username_text, copy;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference reference;
    //sharedpreferences
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    private static final String REMEMBER = "remember";
    String username, userid;

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

        getUserDetail();
        getTracking();

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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Log.i("Menu drawer tag", "log out is clicked");
                    Intent intent = new Intent(HomePage.this,LoginPage.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        username_text = findViewById(R.id.username);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        userid = sharedPreferences.getString(KEY_ID,null);
        username = sharedPreferences.getString(KEY_USERNAME,null);

        if(userid != null && username != null){
            username_text.setText("Welcome " + username.toUpperCase());
        }
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Wallet");

        reference.child(username).child("wallet_balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The "wallet_balance" exists for the user with the given userid
                    double walletBalance = dataSnapshot.getValue(double.class);
                    TextView wallet_balance= findViewById(R.id.walletBalance);

                    String balance = String.format("%.2f", walletBalance);
                    wallet_balance.setText("RM" + balance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        copy = findViewById(R.id.copy);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Warehouse", "广东省佛山市 南海区狮山镇 狮岭村新马国际C仓黄生转ID0");
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(HomePage.this, "Warehouse address copied", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(HomePage.this, NewOrder.class);
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
    public void getUserDetail(){

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("User");

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        userid = sharedPreferences.getString(KEY_ID,null);
        username = sharedPreferences.getString(KEY_USERNAME,null);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(username)){
                    String email = snapshot.child(username).child("email").getValue(String.class);

                    NavigationView navigationView = findViewById(R.id.navigationView);
                    View headerView = navigationView.getHeaderView(0);
                    TextView usernameTextView = headerView.findViewById(R.id.username_text);
                    TextView emailTextView = headerView.findViewById(R.id.email_text);

                    usernameTextView.setText(username);
                    emailTextView.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getTracking(){db = FirebaseDatabase.getInstance();
        reference = db.getReference("OrderHistory");

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        userid = sharedPreferences.getString(KEY_ID, null);
        username = sharedPreferences.getString(KEY_USERNAME, null);


        final AtomicInteger notDeliveredCount = new AtomicInteger(0); // Initialize the count to zero

        reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Iterate through each child under the "username" node
                    for (DataSnapshot orderNumberSnapshot : snapshot.getChildren()) {
                        // Check if the "order_status" field exists
                        if (orderNumberSnapshot.child("order_status").exists()) {
                            String orderStatus = orderNumberSnapshot.child("order_status").getValue(String.class);

                            if (!"Delivered".equals(orderStatus)) {
                                // The order is not delivered, increment the count
                                notDeliveredCount.getAndIncrement();
                            }
                        }
                    }
                }
                TextView track = findViewById(R.id.track_qty);
                track.setText(notDeliveredCount.get() + " ");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });

    }
}
