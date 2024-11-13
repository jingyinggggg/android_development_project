package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {
    private EditText phoneInput, otpInput;
    private Button sendOtpButton, verifyOtpButton;
    private FirebaseAuth auth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification);

        phoneInput = findViewById(R.id.phoneInput);
        otpInput = findViewById(R.id.otpInput);
        sendOtpButton = findViewById(R.id.sendOtpButton);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);

        auth = FirebaseAuth.getInstance();

        sendOtpButton.setOnClickListener(v -> sendOtp());
        verifyOtpButton.setOnClickListener(v -> verifyOtp());
    }

    private void sendOtp() {
        String phoneNumber = phoneInput.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            phoneInput.setError("Enter a valid phone number");
            return;
        }

        // Check if the phone number exists in Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");
        usersRef.orderByChild("contact").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Phone number is registered, proceed with sending OTP
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(PhoneVerification.this)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                    // Auto-retrieval or instant verification is successful
                                    signInWithPhoneAuthCredential(credential);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    // Handle error
                                    Toast.makeText(PhoneVerification.this, "Failed to send OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                    // Code sent successfully, store the verification ID
                                    PhoneVerification.this.verificationId = verificationId;
                                    Toast.makeText(PhoneVerification.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else {
                    // Phone number is not registered, show an error message
                    Toast.makeText(PhoneVerification.this, "This phone number is not registered. Please register first.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PhoneVerification.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp() {
        String code = otpInput.getText().toString().trim();
        if (code.isEmpty() || verificationId == null) {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // OTP verification successful
                        Toast.makeText(this, "Phone verified successfully", Toast.LENGTH_SHORT).show();

                        // Get the username passed from LoginPage
                        String username = getIntent().getStringExtra("USERNAME");

                        // Redirect to HomePage with the verified username
                        Intent homeIntent = new Intent(PhoneVerification.this, HomePage.class);
                        homeIntent.putExtra("USERNAME", username); // Optional: pass username if needed
                        startActivity(homeIntent);
                        finish(); // Close PhoneVerification
                    } else {
                        Toast.makeText(this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void backLoginPage(View view) {
        Intent loginPage = new Intent(this, LoginPage.class);
        startActivity(loginPage);
    }
}
