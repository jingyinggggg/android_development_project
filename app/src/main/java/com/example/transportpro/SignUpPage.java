package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportpro.databinding.SignUpPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpPage extends AppCompatActivity {

    SignUpPageBinding binding;
    String fullname, username, email, contact, password, Rpassword;
    FirebaseDatabase db;
    DatabaseReference reference;

    // Dialog Variables
    Dialog showDialog;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignUpPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = binding.nameInput.getText().toString();
                username = binding.usernameInput.getText().toString().toLowerCase(Locale.ROOT);
                email = binding.emailInput.getText().toString();
                contact = binding.phoneInput.getText().toString();
                password = binding.passwordInput.getText().toString();
                Rpassword = binding.reenterPasswordInput.getText().toString();

                if (!fullname.isEmpty() && !username.isEmpty() && !email.isEmpty() && !contact.isEmpty() && !password.isEmpty() && !Rpassword.isEmpty() && password.equals(Rpassword)) {
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("User");

                    // Query to find the maximum user ID in the database
                    reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                showAlertDialog(R.layout.sign_up_fail, 2);
                                Toast.makeText(SignUpPage.this, "Username is already used, please try another", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // Username is unique, proceed to create the user
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Count the number of users with the same username
                                        int numUsersWithSameUsername = (int) dataSnapshot.getChildrenCount();

                                        // Increment the maximum user ID to generate a new user ID
                                        int newUserId = numUsersWithSameUsername + 1;

                                        int isAdminAccount = 0;
                                        int isDeletedAccount = 0;

                                        // Create the User object with the new user ID
                                        User user = new User(isDeletedAccount, isAdminAccount, newUserId, fullname, username, email, contact, password);

                                        // Show the success dialog
                                        showAlertDialog(R.layout.sign_up_successful, 1);

                                        // Set the new user data in the database
                                        setUserInDatabase(username, user);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle database error
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error
                        }
                    });
                } else {
                    showAlertDialog(R.layout.sign_up_fail, 2);
                }
            }
        });

        // Password Validation
        // Get reference to the TextInputLayout and TextInputEditText for password
        TextInputLayout layoutPassword = findViewById(R.id.textInputLayoutPassword);
        TextInputEditText eTextPassword = findViewById(R.id.passwordInput);
        eTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if (password.length() >= 8) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(password);
                    boolean isPwdContainSpeChar = matcher.find();
                    layoutPassword.setHelperText("");
                    if (isPwdContainSpeChar) {
                        layoutPassword.setError("");
                    } else {
                        layoutPassword.setError("Weak Password. Please include a minimum of 1 special character");
                    }
                } else {
                    layoutPassword.setHelperText("***Password must be a minimum of 8 characters");
                    layoutPassword.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Validate Re-enter password
        // Get reference to the TextInputLayout and TextInputEditText for reenter password
        TextInputLayout textInputLayoutReenterPassword = findViewById(R.id.textInputLayoutRepeatPassword);
        TextInputEditText reenterPasswordInput = findViewById(R.id.reenterPasswordInput);

        reenterPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = eTextPassword.getText().toString();
                String reenterPassword = reenterPasswordInput.getText().toString();

                // If password does not match reenter password, error message displayed
                if (password.equals(reenterPassword)) {
                    textInputLayoutReenterPassword.setError(null);
                } else {
                    textInputLayoutReenterPassword.setError("Password does not match");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void backLoginPage(View view) {
        Intent loginPage = new Intent(this, LoginPage.class);
        startActivity(loginPage);
    }

    private void showAlertDialog(int myLayout, int type) {
        showDialog = new Dialog(SignUpPage.this);
        View layoutView = getLayoutInflater().inflate(myLayout, null);

        showDialog.setContentView(layoutView);
        showDialog.setCancelable(false);
        showDialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        // Sign up successful
        if (type == 1) {
            showDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_button_success));

            Button done = showDialog.findViewById(R.id.done);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backLoginPage(v);
                }
            });
        } else {
            showDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_button_fail));
            Button signUpFail = layoutView.findViewById(R.id.tryAgainButton);
            // Click on the try again button
            signUpFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog.dismiss();
                }
            });
        }

        showDialog.show();
    }

    // Separate method to set user data in the database
    private void setUserInDatabase(String username, User user) {
        reference.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("activity", "User register");
                    mFirebaseAnalytics.logEvent("Update_profile", bundle);
                    // Data was successfully written to the database
                    Log.d("Firebase", "Data write successful");
                    // Reset the input fields
                    binding.nameInput.setText("");
                    binding.usernameInput.setText("");
                    binding.emailInput.setText("");
                    binding.phoneInput.setText("");
                    binding.passwordInput.setText("");
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    if (e != null) {
                        Log.e("Firebase", "Data write failed: " + e.getMessage());
                        showAlertDialog(R.layout.sign_up_fail, 2);
                    }
                }
            }
        });
    }
}