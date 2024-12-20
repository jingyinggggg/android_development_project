package com.example.transportpro;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
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
    DatabaseReference bookingReference;

    int userId;
    String trackNo;
    String username;
    double previousWeight;
    double weight;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
        TextView quantity = findViewById(R.id.quantity);
        TextView descTextView = findViewById(R.id.desc);
        EditText weightEditText = findViewById(R.id.weight);

        /*Description's character word count*/
        TextView charCountTextView = findViewById(R.id.charCount);

        /*Weight and Error message*/
        TextView errorMessageTextView = findViewById(R.id.error_message);

        accept_booking = findViewById(R.id.accept_booking);
        decline_booking = findViewById(R.id.decline_booking);

        bookingReference = FirebaseDatabase.getInstance().getReference("Booking");
        usersReference = FirebaseDatabase.getInstance().getReference("User");

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    int uId  = userSnapshot .child("userId").getValue(int.class);
                    if(userId == uId)
                    {
                        username = userSnapshot.child("username").getValue(String.class);
                        customerId.setText("Customer ID: " + userId);
                        customerName.setText("Customer name : "+username);

                        bookingReference.child(username).child(trackNo).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String cat = snapshot.child("category").getValue(String.class);
                                String deli_by = snapshot.child("delivery_by").getValue(String.class);
                                String desc = snapshot.child("description").getValue(String.class);
                                int quan =  snapshot.child("quantity").getValue(int.class);
                                previousWeight = snapshot.child("weight").getValue(double.class);
                                String quan_text = String.valueOf(quan);

                                trackNo_display.setText(trackNo);
                                category.setText(cat);
                                deliveryType.setText(deli_by);
                                quantity.setText(quan_text);
                                descTextView.setText(desc);


                                accept_booking.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        // Check for validation of the edit text of weight
                                        String weightString = weightEditText.getText().toString().trim();
                                        weightString = weightString.replace(" ", "");
                                        if (!TextUtils.isEmpty(weightString) && !"0".equals(previousWeight))
                                        {
                                            // Convert the weight to an integer before storing in the database
                                            weight = Double.parseDouble(weightString);

                                            if (weight != previousWeight) {
                                                // Display a Toast indicating that the weight has changed
                                                Toast.makeText(UpdateBookingAdmin.this, "Weight has changed", Toast.LENGTH_SHORT).show();

                                                // Update the value of 'weight' in the database with the integer value
                                                bookingReference.child(username).child(trackNo).child("weight").setValue(weight);
                                                bookingReference.child(username).child(trackNo).child("collected").setValue(1);

                                                // Update the previousWeight variable
                                                previousWeight = weight;

                                                if (username!=null){
                                                    String title = "booking";
                                                    String type = "parcel_collected,reminder";
                                                    String content = trackNo;
                                                    String imageResId  = String.valueOf(R.drawable.reminder);

                                                    int is_read = 0;
                                                    logUpdateBooking(username, "accept");
                                                    DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("Notification").child(username).child(title).child(content);

                                                    NotificationClass notificationClass = new NotificationClass(userId,imageResId ,title,type,content,is_read);
                                                    notificationReference.setValue(notificationClass);
                                                    redirect_view_booking(view);

                                                }
                                            }
                                            else
                                            {
                                                errorMessageTextView.setVisibility(View.VISIBLE);
                                                errorMessageTextView.setText("Please enter a different weight");
                                            }

                                            // Hide the error message
                                            errorMessageTextView.setVisibility(View.GONE);

                                        }
                                        else
                                        {
                                            // Set the error message to be visible with red-colored text
                                            errorMessageTextView.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                                decline_booking.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        // Create an EditText for the user to enter description
                                        final EditText input = new EditText(UpdateBookingAdmin.this);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                        input.setLayoutParams(lp);

                                        // Create an AlertDialog.Builder with this context
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBookingAdmin.this);
                                        builder.setView(input);
                                        builder.setTitle("Enter Description (10 words max)");
                                        builder.setMessage("Please enter the reason for declining the order: ");

                                        // Set up the buttons
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String description = input.getText().toString().trim(); // Get user input

                                                String[] words = description.split("\\s+");
                                                if (description.isEmpty() || words.length >= 10) {
                                                    Toast.makeText(UpdateBookingAdmin.this, "Please enter a valid description with the length of 10 words.", Toast.LENGTH_SHORT).show();
                                                    return; // Stop further execution
                                                }

                                                // Proceed with the rest of the logic only if description is not empty
                                                // User clicked 'No' button, perform the action
                                                if (username != null) {
                                                    String title = "booking";
                                                    String type = "parcel_declined,"+description;
                                                    String content = trackNo;
                                                    String imageResId = String.valueOf(R.drawable.parcel_declined);

                                                    int is_read = 0;
                                                    DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("Notification").child(username).child(title).child(content);

                                                    NotificationClass notificationClass = new NotificationClass(userId, imageResId, title, type, content, is_read);
                                                    notificationReference.setValue(notificationClass);

                                                    bookingReference.child(username).child(trackNo).getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            logUpdateBooking(username, "decline");
                                                            Toast.makeText(UpdateBookingAdmin.this, "The Booking " + trackNo + " has been declined", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(e -> Toast.makeText(UpdateBookingAdmin.this, "Failed to decline booking.", Toast.LENGTH_SHORT).show());
                                                    redirect_view_booking(view);
                                                }else {
                                                    Toast.makeText(UpdateBookingAdmin.this, "Error: User not identified.", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        });

                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                        // Show the AlertDialog
                                        builder.show();

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

    public void logUpdateBooking(String username, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        if (action == "accept"){
            mFirebaseAnalytics.logEvent("accept_booking", bundle);
        }
        else if (action == "decline"){
            mFirebaseAnalytics.logEvent("decline_booking", bundle);
        }

    }
}