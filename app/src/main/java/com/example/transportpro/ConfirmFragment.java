package com.example.transportpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfirmFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BookingClass> bookingClassArrayList;
    AdapterSelectOrder adapterSelectOrder;
    DatabaseReference database;
    String currentUserId, username;
    private AppCompatActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);

        // Get the current user ID from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("localstorage", Context.MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("userId", null);
            username = sharedPreferences.getString("userName", null);
        int user_id = Integer.parseInt(currentUserId);

        recyclerView = view.findViewById(R.id.confirm_container);
        database = FirebaseDatabase.getInstance().getReference("Booking").child(username);
        recyclerView.setHasFixedSize(true);

        bookingClassArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterSelectOrder = new AdapterSelectOrder(getContext(), bookingClassArrayList, activity, "ConfirmOrder");
        adapterSelectOrder.setCurrentUserId(currentUserId); // Set the currentUserId in the adapter
        recyclerView.setAdapter(adapterSelectOrder);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingClassArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingClass bookingClass = dataSnapshot.getValue(BookingClass.class);
                    if (bookingClass.getCollected() == 1) {
                        bookingClassArrayList.add(bookingClass);
                        recyclerView.setAdapter(adapterSelectOrder);
                    }

                }
                adapterSelectOrder.notifyDataSetChanged();

                if(bookingClassArrayList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Initialize shared ViewModel
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Observe changes in the selected transport mode
        sharedViewModel.getTransportData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectedTransport) {
                TextView transport_method = view.findViewById(R.id.transport_method);
                transport_method.setVisibility(View.VISIBLE);
                // Handle the selected transport mode
                transport_method.setText(selectedTransport);
            }
        });

        sharedViewModel.getSensitiveItem().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                TextView sensitive = view.findViewById(R.id.sensitive);
                sensitive.setVisibility(View.VISIBLE);
                sensitive.setText(s);

            }
        });

        sharedViewModel.getReceiverData().observe(getViewLifecycleOwner(), new Observer<ReceiverData>() {
            @Override
            public void onChanged(ReceiverData receiverData) {
                LinearLayout receiver_detail_container = view.findViewById(R.id.receiver_detail_container);
                receiver_detail_container.setVisibility(View.VISIBLE);
                TextView name = view.findViewById(R.id.confirm_name);
                TextView mobile = view.findViewById(R.id.confirm_mobile);
                TextView email = view.findViewById(R.id.confirm_email);
                TextView state = view.findViewById(R.id.state);
                TextView postcode = view.findViewById(R.id.confirm_postcode);
                TextView add1 = view.findViewById(R.id.confirm_add);
                TextView add2 = view.findViewById(R.id.confirm_add1);
                TextView add3 = view.findViewById(R.id.confirm_add2);

                // Set the received data to TextViews
                name.setText(receiverData.getName());
                mobile.setText(receiverData.getMobile());
                email.setText(receiverData.getEmail());
                state.setText(receiverData.getState());
                postcode.setText(receiverData.getPostcode());
                add1.setText(receiverData.getAdd1());
                add2.setText(receiverData.getAdd2());
                add3.setText(receiverData.getAdd3());
            }
        });


        return view;
    }
}
