package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Orderhistory extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;
    AdapterOrderHistory adapterOrderHistory;
    DatabaseReference database;
    String currentUserId;
    String username;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderhistory);
        SharedPreferences sharedPreferences = this.getSharedPreferences("localstorage", Context.MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("userId", null);
        username = sharedPreferences.getString("userName", null);

        recyclerView = findViewById(R.id.recycle);
        database = FirebaseDatabase.getInstance().getReference("OrderHistory").child(username);
        recyclerView.setHasFixedSize(true);

        orderHistoryClassArrayList =  new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterOrderHistory = new AdapterOrderHistory(this, orderHistoryClassArrayList);
        adapterOrderHistory.setCurrentUserId(currentUserId);
        recyclerView.setAdapter(adapterOrderHistory);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderHistoryClassArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderHistoryClass orderHistoryClass = dataSnapshot.getValue(OrderHistoryClass.class);
                    orderHistoryClassArrayList.add(orderHistoryClass);
                }
                adapterOrderHistory.notifyDataSetChanged();

                if(orderHistoryClassArrayList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Orderhistory.this, HomePage.class);
                startActivity(intent);
            }
        });


    }
}