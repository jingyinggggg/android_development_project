package com.example.transportpro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class UpdateBookingAdmin extends AppCompatActivity {

    ImageButton header_button;
    Button accept_booking;
    Button decline_booking;

    DatabaseReference usersReference;
    DatabaseReference reference;

    int userId;
    String trackNo;
    String username;
    double currentWeight;
    double weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);

        // Set the image button based on the current page or context
        header_button = findViewById(R.id.backArrow);

        /*Header Button Function*/
        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_view_booking(view);
            }
        });

        Intent from_booking = getIntent();
        if (from_booking != null)
        {
            Bundle extras = from_booking.getExtras();

            if (extras != null)
            {
                userId = extras.getInt("userId", 0);
                trackNo = extras.getString("trackNo");
            }
        }

        /*Customer Details*/
        TextView customerId = findViewById(R.id.customerId);
        TextView customerName = findViewById(R.id.customerName);

        /*Track Details*/
        TextView trackNo_display = findViewById(R.id.trackNo_display);
        TextView category = findViewById(R.id.category);
        TextView deliveryType = findViewById(R.id.deliver_type);
        TextView descTextView = findViewById(R.id.desc);
        EditText weightEditText = findViewById(R.id.weight);

        /*Description's character word count*/
        TextView charCountTextView = findViewById(R.id.charCount);

        /*Weight and Error message*/
        TextView errorMessageTextView = findViewById(R.id.error_message);

        accept_booking = findViewById(R.id.accept_booking);
        decline_booking = findViewById(R.id.decline_booking);

        reference = FirebaseDatabase.getInstance().getReference("Booking");
        usersReference = FirebaseDatabase.getInstance().getReference("User");

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    String currentUsername = userSnapshot.child("username").getValue(String.class);
                    int uId  = userSnapshot .child("userId").getValue(int.class);

                    if(userId == uId)
                    {
                        username = userSnapshot.child("username").getValue(String.class);
                        customerId.setText("Customer ID: " + userId);
                        customerName.setText("Customer name : "+username);

                        reference.child(username).child(trackNo).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String cat = snapshot.child("category").getValue(String.class);
                                String deli_by = snapshot.child("delivery_by").getValue(String.class);
                                String desc = snapshot.child("delivery_by").getValue(String.class);
                                currentWeight = snapshot.child("weight").getValue(double.class);

                                trackNo_display.setText(trackNo);
                                category.setText(cat);
                                deliveryType.setText(deli_by);
                                descTextView.setText(desc);
                                if (currentWeight != 0){
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    String formattedWeight = decimalFormat.format(currentWeight);

                                    weightEditText.setText(formattedWeight + " KG");
                                }


                                accept_booking.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Check for validation of the edit text of weight
                                        String weightString = weightEditText.getText().toString().trim();
                                        weightString = weightString.replace(" ", "");

                                        if (!TextUtils.isEmpty(weightString) && !"0".equals(weightString))
                                        {

                                            weight = Double.parseDouble(weightString);
                                            if (weight != currentWeight) {
                                                // Display a Toast indicating that the weight has changed
                                                Toast.makeText(UpdateBookingAdmin.this, "Weight has changed", Toast.LENGTH_SHORT).show();
                                                // Update the value of 'weight' in the database
                                                snapshot.child("weight").getRef().setValue(weight + " KG");
                                                snapshot.child("collected").getRef().setValue(1);

                                                // Update the previousWeight variable
                                                currentWeight = weight;
                                            }

                                            // Hide the error message
                                            errorMessageTextView.setVisibility(View.GONE);
                                            redirect_view_booking(view);

                                        }
                                        else
                                        {
                                            // Set the error message to be visible with red-colored text
                                            accept_booking.setOnClickListener(null);
                                            errorMessageTextView.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*Count number of words in the Description*/
        descTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Count the number of characters
                int charCount = s.length();

                // Update the charCountTextView with the character count
                charCountTextView.setText(String.valueOf(charCount));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
    }

    public void redirect_view_booking(View v){
        Intent viewBookingIntent = new Intent(UpdateBookingAdmin.this, CustomerBookingPage.class);
        startActivity(viewBookingIntent);
    }



}
