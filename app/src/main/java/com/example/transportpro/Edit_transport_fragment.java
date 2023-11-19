package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_transport_fragment extends Fragment {


    RadioButton seabtn;
    RadioButton airBtn;
    LinearLayout seaL;
    LinearLayout airL;
    LinearLayout yesButton;
    LinearLayout noButton;
    int selectedButtonId;
    SharedViewModel sharedViewModel;
    FirebaseDatabase db;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    private static final String Order_Number = "orderNumber";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transport, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        seabtn = view.findViewById(R.id.seaBtn);
        airBtn = view.findViewById(R.id.airBtn);
        seaL = view.findViewById(R.id.seaLayout);
        airL = view.findViewById(R.id.airLayout);
        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);

        selectedButtonId = R.id.noButton;

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME,null);
        String order_number = sharedPreferences.getString(Order_Number,null);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("OrderHistory");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(username)){
                    final String transport_type = snapshot.child(username).child(order_number).child("transport_type").getValue(String.class);
                    final int is_sensitive = snapshot.child(username).child(order_number).child("sensitive_item").getValue(int.class);

                    final String name = snapshot.child(username).child(order_number).child("receiver_name").getValue(String.class);
                    final String mobile = snapshot.child(username).child(order_number).child("receiver_contact").getValue(String.class);
                    final String email = snapshot.child(username).child(order_number).child("receiver_email").getValue(String.class);
                    final String state_text = snapshot.child(username).child("state").getValue(String.class);
                    final String postcode_text = snapshot.child(username).child(order_number).child("receiver_postcode").getValue(String.class);
                    final String add1_text = snapshot.child(username).child(order_number).child("receiver_add1").getValue(String.class);
                    final String add2_text = snapshot.child(username).child(order_number).child("receiver_add2").getValue(String.class);
                    final String add3_text = snapshot.child(username).child(order_number).child("receiver_add3").getValue(String.class);

                    if(transport_type.equals("Air")){
                        airL.setBackgroundResource(R.drawable.order_selected);
                        airBtn.setChecked(true);
                    }else if(transport_type.equals("Sea")){
                        seaL.setBackgroundResource(R.drawable.order_selected);
                        seabtn.setChecked(true);
                    }
                    if(is_sensitive == 0){
                        noButton.setBackgroundResource(R.drawable.order_selected);
                    }
                    else if (is_sensitive == 1) {
                        yesButton.setBackgroundResource(R.drawable.order_selected);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seaL.setBackgroundResource(R.drawable.order_selected);


                airL.setBackgroundResource(R.drawable.order_rectangle);
                sharedViewModel.setTransportData("Sea");
            }
        });

        airBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                airL.setBackgroundResource(R.drawable.order_selected);


                seaL.setBackgroundResource(R.drawable.order_rectangle);
                sharedViewModel.setTransportData("Air");
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yesButton.setBackgroundResource(R.drawable.order_selected);
                noButton.setBackgroundResource(R.drawable.order_rectangle);
                sharedViewModel.setSensitiveItem("Yes");
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noButton.setBackgroundResource(R.drawable.order_selected);
                yesButton.setBackgroundResource(R.drawable.order_rectangle);
                sharedViewModel.setSensitiveItem("No");
            }
        });



        return view;
    }
    public void onButtonClick(View view) {
        if (view.getId() == R.id.yesButton) {
            selectedButtonId = R.id.yesButton;
            yesButton.setBackgroundResource(R.drawable.order_selected);
            noButton.setBackgroundResource(R.drawable.order_rectangle);

        } else if (view.getId() == R.id.noButton) {
            selectedButtonId = R.id.noButton;
            noButton.setBackgroundResource(R.drawable.order_selected);
            yesButton.setBackgroundResource(R.drawable.order_rectangle);
        }

        // Update the button colors
    }


}