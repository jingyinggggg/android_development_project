package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WarehouseAdmin extends AppCompatActivity {



    DatabaseReference usersRef;
    DatabaseReference bookingRef;
    DatabaseReference orderHistRef;

    RecyclerView warehouseRecycler;
    AdapterWarehouse adapterWarehouse;

    ArrayList<User>users;

    ImageView header_button;
    Button chn_trackNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        // Set the image button based on the current page or context
        header_button = findViewById(R.id.backArrow);

        warehouseRecycler = findViewById(R.id.warehouse_list);

        warehouseRecycler.setHasFixedSize(true);
        warehouseRecycler.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>();
        adapterWarehouse = new AdapterWarehouse(this, users,this);
        warehouseRecycler.setAdapter(adapterWarehouse);

        usersRef = FirebaseDatabase.getInstance().getReference("User");
        bookingRef = FirebaseDatabase.getInstance().getReference("Booking");
        orderHistRef = FirebaseDatabase.getInstance().getReference("OrderHistory");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> nonAdminUsers = new ArrayList<>();
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if (currentUser != null){
                        int isAdmin = dataSnapshot.child("isAdminAcc").getValue(Integer.class);
                        if(isAdmin == 0){
                            nonAdminUsers.add(currentUser);
                        }
                    }
                }
                for (User nonAdminUser : nonAdminUsers){
                    String username = nonAdminUser.getUsername();
                    bookingRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot bookingDataSnapshot : snapshot.getChildren()) {
                                BookingClass booking = bookingDataSnapshot.getValue(BookingClass.class);
                                if (booking != null ) {
                                    orderHistRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot orderHistSnapshot : snapshot.getChildren()){
                                                if (orderHistSnapshot != null){
                                                    users.add(nonAdminUser);
                                                    adapterWarehouse.notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    ;
                                    break; // Break if at least one uncollected booking is found for this user
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_homepage(view);
            }
        });

    }

    public void redirect_homepage(View v){
        Intent homepageIntent = new Intent(WarehouseAdmin.this, AdminHomePage.class);
        startActivity(homepageIntent);
    }

    public void redirect_update_warehouse(View v){
        Intent updateWarehouseIntent = new Intent(WarehouseAdmin.this, UpdateWarehouseAdmin.class);
        startActivity(updateWarehouseIntent);
    }

}