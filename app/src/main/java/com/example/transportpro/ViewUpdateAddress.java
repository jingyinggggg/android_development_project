package com.example.transportpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewUpdateAddress extends AppCompatActivity {

    private TextView usernameTextView;
    private EditText editAddressLine1,editAddressLine2,editAddressLine3, editState, editPostalCode;
    private Button saveAddressButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_update_address);

        usernameTextView = findViewById(R.id.usernameTextView);
        editAddressLine1 = findViewById(R.id.editAddressLine1);
        editAddressLine2 = findViewById(R.id.editAddressLine2);
        editAddressLine3 = findViewById(R.id.editAddressLine3);
        editState = findViewById(R.id.editState);
        editPostalCode = findViewById(R.id.editPostalCode);

        saveAddressButton = findViewById(R.id.saveAddressButton);

        username = getIntent().getStringExtra("username");
        Log.d("ViewUpdateAddress", "Username retrieved: " + username);

        if (username == null) {
            Toast.makeText(this, "Username is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadAddressData();

        saveAddressButton.setOnClickListener(v -> saveAddressChanges());
    }

    private void loadAddressData() {
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference("Address").child(username);
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String addressLine1 = snapshot.child("address1").getValue(String.class);
                    String addressLine2 = snapshot.child("address2").getValue(String.class);
                    String addressLine3 = snapshot.child("address3").getValue(String.class);
                    String state = snapshot.child("state").getValue(String.class);
                    String postalCode = snapshot.child("postcode").getValue(String.class);

                    Log.d("ViewUpdateAddress", "Address Line 1: " + addressLine1);
                    Log.d("ViewUpdateAddress", "Address Line 2: " + addressLine2);
                    Log.d("ViewUpdateAddress", "Address Line 3: " + addressLine3);
                    usernameTextView.setText(username);
                    editAddressLine1.setText(addressLine1);
                    editAddressLine2.setText(addressLine2);
                    editAddressLine3.setText(addressLine3);
                    editState.setText(state);
                    editPostalCode.setText(postalCode);
                }else {
                    Toast.makeText(ViewUpdateAddress.this, "Address data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewUpdateAddress.this, "Failed to load address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAddressChanges() {

        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference("Address").child(username);

        String updatedAddressLine1 = editAddressLine1.getText().toString().trim();
        String updatedAddressLine2 = editAddressLine2.getText().toString().trim();
        String updatedAddressLine3 = editAddressLine3.getText().toString().trim();
        String updatedCity = editState.getText().toString().trim();
        String updatedPostalCode = editPostalCode.getText().toString().trim();

        addressRef.child("address1").setValue(updatedAddressLine1);
        addressRef.child("address2").setValue(updatedAddressLine2);
        addressRef.child("address3").setValue(updatedAddressLine3);
        addressRef.child("city").setValue(updatedCity);
        addressRef.child("postalCode").setValue(updatedPostalCode)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewUpdateAddress.this, "Address updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ViewUpdateAddress.this, "Failed to update address", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void backUserDetails(View v){
        finish();
    }
}

