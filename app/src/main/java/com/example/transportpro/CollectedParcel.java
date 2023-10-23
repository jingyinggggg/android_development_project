package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CollectedParcel extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BookingClass> bookingClassArrayList;
    AdapterParcel adapterParcel;
    DatabaseReference database;
    String currentUserId, username;
    private AppCompatActivity activity;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_collected_parcel, container, false);
        // Get the current user ID from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("localstorage", Context.MODE_PRIVATE);

        currentUserId = sharedPreferences.getString("userId", null);

        username = sharedPreferences.getString("userName", null);

        recyclerView = view.findViewById(R.id.recycle_parcel);
        database = FirebaseDatabase.getInstance().getReference("Booking").child(username);
        recyclerView.setHasFixedSize(true);

        bookingClassArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterParcel = new AdapterParcel(getContext(), bookingClassArrayList, activity, "C");
        adapterParcel.setCurrentUserId(currentUserId); // Set the currentUserId in the adapter
        recyclerView.setAdapter(adapterParcel);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingClassArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingClass bookingClass = dataSnapshot.getValue(BookingClass.class);
                    bookingClassArrayList.add(bookingClass);
                }

                adapterParcel.notifyDataSetChanged();

                if(bookingClassArrayList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}