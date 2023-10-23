package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ParcelEditPage extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parcel_edit_page);

        Intent intent = getIntent();
        String trackNumber = intent.getStringExtra("track_number");
        String category = intent.getStringExtra("category");
        String deliveryBy = intent.getStringExtra("delivery_by");
        String quantity = intent.getStringExtra("quantity");
        String description = intent.getStringExtra("description");
        String collected = intent.getStringExtra("collected");

        TextInputEditText trackingNumberInput = findViewById(R.id.trackingNumberInput);
        TextInputEditText categoryInput = findViewById(R.id.categoryInput);
        TextInputEditText deliveryInput = findViewById(R.id.deliveryInput);
        TextInputEditText quantityInput = findViewById(R.id.quantityInput);
        TextInputEditText descriptionInput = findViewById(R.id.descriptionInput);

        trackingNumberInput.setText(trackNumber);
        categoryInput.setText(category);
        deliveryInput.setText(deliveryBy);
        quantityInput.setText(quantity);
        descriptionInput.setText(description);

        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        LinearLayout orderButton = findViewById(R.id.orderButton);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        if (collected != null){
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);
        }
        else {
            db = FirebaseDatabase.getInstance();
            reference = db.getReference("Booking").child(username);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(trackNumber)) {
                                DatabaseReference userReference = reference.child(trackNumber);

                                if(!quantityInput.getText().toString().isEmpty() && !trackingNumberInput.getText().toString().isEmpty()) {

                                    int quantity = Integer.parseInt(quantityInput.getText().toString());

                                    if (quantity > 0) {
                                        int user_id = Integer.parseInt(userid);
                                        String track_Number = trackingNumberInput.getText().toString();
                                        String category = categoryInput.getText().toString().toString();
                                        String deliveryBy = deliveryInput.getText().toString().toString();
                                        String description = descriptionInput.getText().toString().toString();

                                        int collected = 0;
                                        Date date = new Date();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                                        String formattedDate = dateFormat.format(date);
                                        int isChecked = 0;
                                        double weight = 0.000;
                                        int isPackOrder = 0;


                                        BookingClass booking = new BookingClass(user_id, track_Number, category, deliveryBy, quantity, description, collected, formattedDate, isChecked, weight, isPackOrder);
                                        reference.child(trackNumber).removeValue();
                                        reference.child(track_Number).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ParcelEditPage.this, "Parcel has been updated successfully!", Toast.LENGTH_SHORT).show();

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        backParcelPage(v);
                                                    }
                                                }, 2000);

                                            }
                                        });

                                    } else {
                                        Toast.makeText(ParcelEditPage.this, "Quantity cannot be 0 !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(ParcelEditPage.this, "Track number and quantity cannot be empty!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(trackNumber)) {
                                // If the track number exists, delete the data
                                reference.child(trackNumber).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ParcelEditPage.this, "Parcel has been deleted successfully!", Toast.LENGTH_SHORT).show();

                                                // Navigate back to the previous page after a delay
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        backParcelPage(v);
                                                    }
                                                }, 2400);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle the case where the data couldn't be deleted
                                                Toast.makeText(ParcelEditPage.this, "Failed to delete the parcel.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Handle the case where the track number doesn't exist in the database
                                Toast.makeText(ParcelEditPage.this, "Parcel with this track number does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle any database error here
                        }
                    });
                }
            });
        }



    }

    public void backParcelPage(View view){
        Intent parcelPage = new Intent(this, ParcelPage.class);
        startActivity(parcelPage);
    }

}