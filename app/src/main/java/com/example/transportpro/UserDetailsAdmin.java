package com.example.transportpro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsAdmin extends AppCompatActivity {

    private EditText editUsername, editFullname, editEmail, editContact;
    private TextView noAddressTextView;
    private Switch adminAccessSwitch;
    private Button saveButton,deactivateUserButton, viewUpdateAddressButton;
    private DatabaseReference userRef;
    private DatabaseReference addressRef;
    private String username;
    private int isDeletedAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        editUsername = findViewById(R.id.editUsername);
        editFullname = findViewById(R.id.editFullname);
        editEmail = findViewById(R.id.editEmail);
        editContact = findViewById(R.id.editContact);
        adminAccessSwitch = findViewById(R.id.adminAccessSwitch);

        viewUpdateAddressButton = findViewById(R.id.viewUpdateAddressButton);
        noAddressTextView = findViewById(R.id.noAddressTextView);

        saveButton = findViewById(R.id.saveUserButton);
        deactivateUserButton = findViewById(R.id.deactivateUserButton);

        username = getIntent().getStringExtra("username");
        if (username == null) {
            Toast.makeText(this, "Username is missing", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if username is null
            return;
        }

        addressRef = FirebaseDatabase.getInstance().getReference("Address").child(username);

        checkAddressExistence();

        String fullname = getIntent().getStringExtra("fullname");
        String email = getIntent().getStringExtra("email");
        String contact = getIntent().getStringExtra("contact");
        int isAdminAcc = getIntent().getIntExtra("isAdminAcc", 0);

        // Display the data
        editUsername.setText(username);
        editFullname.setText(fullname);
        editEmail.setText(email);
        editContact.setText(contact);
        adminAccessSwitch.setChecked(isAdminAcc == 1);

        userRef = FirebaseDatabase.getInstance().getReference("User").child(username);

        loadUserData();

        saveButton.setOnClickListener(v -> saveChanges());
        deactivateUserButton.setOnClickListener(v -> toggleAccountStatus());

        viewUpdateAddressButton.setOnClickListener(v -> {
            if (username != null) {
                Intent intent = new Intent(UserDetailsAdmin.this, ViewUpdateAddress.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Log.d("UserDetailsAdmin", "Username is null, cannot pass to ViewUpdateAddress");
            }
        });
    }

    private void saveChanges() {
        String updatedFullname = editFullname.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String updatedContact = editContact.getText().toString().trim();
        int updatedIsAdminAcc = adminAccessSwitch.isChecked() ? 1 : 0;

        userRef.child("fullname").setValue(updatedFullname);
        userRef.child("email").setValue(updatedEmail);
        userRef.child("contact").setValue(updatedContact);
        userRef.child("isAdminAcc").setValue(updatedIsAdminAcc)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserDetailsAdmin.this, "User details updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserDetailsAdmin.this, "Failed to update user details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isDeletedAcc = snapshot.child("isDeletedAcc").getValue(Integer.class);

                    // Update button appearance based on isDeletedAcc status
                    if (isDeletedAcc == 1) {
                        deactivateUserButton.setText("Reactivate Account");
                        deactivateUserButton.setBackgroundColor(Color.GREEN);
                        deactivateUserButton.setTextColor(Color.BLACK);
                    } else {
                        deactivateUserButton.setText("Deactivate Account");
                        deactivateUserButton.setBackgroundColor(Color.RED);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDetailsAdmin.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleAccountStatus() {
        if (isDeletedAcc == 1) {
            userRef.child("isDeletedAcc").setValue(0).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(UserDetailsAdmin.this, "Account reactivated successfully", Toast.LENGTH_SHORT).show();
                    deactivateUserButton.setText("Deactivate Account");
                    deactivateUserButton.setBackgroundColor(Color.RED);
                    isDeletedAcc = 0;
                } else {
                    Toast.makeText(UserDetailsAdmin.this, "Failed to reactivate account", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            userRef.child("isDeletedAcc").setValue(1).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(UserDetailsAdmin.this, "Account deactivated successfully", Toast.LENGTH_SHORT).show();
                    deactivateUserButton.setText("Reactivate Account");
                    deactivateUserButton.setBackgroundColor(Color.GREEN);
                    isDeletedAcc = 1;
                } else {
                    Toast.makeText(UserDetailsAdmin.this, "Failed to deactivate account", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkAddressExistence() {
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    viewUpdateAddressButton.setVisibility(View.VISIBLE);
                    noAddressTextView.setVisibility(View.GONE);
                } else {
                    viewUpdateAddressButton.setVisibility(View.GONE);
                    noAddressTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDetailsAdmin.this, "Failed to check address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backUserList(View view) {
        Intent intent = new Intent(UserDetailsAdmin.this, UserListAdmin.class);
        startActivity(intent);
    }


}