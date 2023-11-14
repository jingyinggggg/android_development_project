package com.example.transportpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerBookingPage extends AppCompatActivity {

    ImageButton header_button;
    Button view_customer_details;

    ArrayList<BookingClass> bookingClassArrayList;

    DatabaseReference usersReference;
    DatabaseReference bookingReference;

    RecyclerView bookingRecyclerView;
    AdapterBooking adapterBooking;
    String username;
    ArrayList<String> users;

    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking);

        header_button = findViewById(R.id.backArrow);


        bookingRecyclerView = findViewById(R.id.viewBooking);

        bookingRecyclerView.setHasFixedSize(true);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingClassArrayList = new ArrayList<>();
        adapterBooking = new AdapterBooking(this, bookingClassArrayList,this);
        bookingRecyclerView.setAdapter(adapterBooking);

        usersReference = FirebaseDatabase.getInstance().getReference("User");
        bookingReference = FirebaseDatabase.getInstance().getReference("Booking");

        users = new ArrayList<>();
        usersReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    username = dataSnapshot.child("username").getValue(String.class);
                    int isAdmin = dataSnapshot.child("isAdminAcc").getValue(int.class);
                    if(isAdmin == 0){
                        users.add(username);
                    }
                }
                bookingClassArrayList.clear();
                for (String currentUser : users){
                    bookingReference.child(currentUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                int collected = dataSnapshot.child("collected").getValue(int.class);
                                if(collected == 0){
                                    BookingClass booking = dataSnapshot.getValue(BookingClass.class);
                                    bookingClassArrayList.add(booking);
                                }
                            }
                            adapterBooking.notifyDataSetChanged();
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
        Intent homepageIntent = new Intent(CustomerBookingPage.this, AdminHomePage.class);
        startActivity(homepageIntent);
    }

}
