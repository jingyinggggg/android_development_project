package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderAdmin extends AppCompatActivity {

    ImageView header_button;
    Button order;

    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;

    DatabaseReference usersReference;
    DatabaseReference bookingReference;
    DatabaseReference orderReference;
    DatabaseReference orderHistReference;

    RecyclerView orderRecyclerView;
    AdapterOrderAdmin adapterOrderAdmin;
    String username;
    int userId;

    private boolean foundPackingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        /*Header Button */
        header_button = findViewById(R.id.backArrow);

        orderRecyclerView = findViewById(R.id.viewOrder);

        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderHistoryClassArrayList = new ArrayList<>();
        adapterOrderAdmin = new AdapterOrderAdmin(this, orderHistoryClassArrayList,this);
        orderRecyclerView.setAdapter(adapterOrderAdmin);

        usersReference = FirebaseDatabase.getInstance().getReference("User");
        bookingReference = FirebaseDatabase.getInstance().getReference("Booking");
        orderReference = FirebaseDatabase.getInstance().getReference("Order");
        orderHistReference = FirebaseDatabase.getInstance().getReference("OrderHistory");


        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AtomicInteger pendingAsyncOperations = new AtomicInteger(0);
                orderHistoryClassArrayList.clear();
                foundPackingOrder = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String currentUsername  = dataSnapshot.child("username").getValue(String.class);
                    int currentUserId = dataSnapshot.child("userId").getValue(int.class);

                    if(dataSnapshot.child("isAdminAcc").getValue(int.class) == 0 ){
                        pendingAsyncOperations.incrementAndGet();
                        orderHistReference.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean hasPackingOrder = false;

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    String order_status = dataSnapshot.child("order_status").getValue(String.class);
                                    int order_user = dataSnapshot.child("userId").getValue(int.class);
                                    if("Packing".equals(order_status)  && currentUserId == order_user ){
                                        OrderHistoryClass orderHist = dataSnapshot.getValue(OrderHistoryClass.class);
                                        if(orderHist != null) {
                                            hasPackingOrder = true;
                                            orderHistoryClassArrayList.add(orderHist);
                                        }
                                    }
                                }
                                if (hasPackingOrder) {
                                    // Add only one null object if no order is in "Packing" status
                                    foundPackingOrder = true;
                                }
                                if (pendingAsyncOperations.decrementAndGet() == 0) {
                                    if (!foundPackingOrder) {
                                        orderHistoryClassArrayList.add(null); // Add null if no packing orders found
                                    }
                                    // All asynchronous operations are done, update the adapter
                                    adapterOrderAdmin.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("OrderAdmin", "Database error: " + error.getMessage());
                            }
                        });
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*Header Button Function*/
        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_homepage(view);
            }
        });

    }

    public void redirect_homepage(View v){
        Intent homepageIntent = new Intent(OrderAdmin.this, AdminHomePage.class);
        startActivity(homepageIntent);
    }

}