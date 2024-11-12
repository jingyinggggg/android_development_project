package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportpro.databinding.LoginPageBinding;
import com.example.transportpro.databinding.SignUpPageBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    String username, password;
    LoginPageBinding binding;
    TextView logo, signUp;
    CheckBox rememberUser;
    TextView forgotPassword;
    Button login;
    Button admin;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://transportpro-13407-default-rtdb.firebaseio.com/");
    SharedPreferences sharedPreferences;

    //sharedpreferences
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    private static final String REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logo = (TextView) findViewById(R.id.logoName);
        String logoText = "TransportPro";
        SpannableString ss = new SpannableString(logoText);
        StyleSpan boldItalicSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(51,134,197));
        ss.setSpan(boldItalicSpan, 9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(colorSpan, 9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        logo.setText(ss);

        signUp = (TextView) findViewById(R.id.signUp);
        SpannableString signUpText = new SpannableString(signUp.getText().toString());
        signUpText.setSpan(new UnderlineSpan(), 0, signUpText.length(), 0);
        signUp.setText(signUpText);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        rememberUser = (CheckBox) findViewById(R.id.rememberMe);

        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(v -> {
            username = binding.usernameInput.getText().toString().toLowerCase(Locale.ROOT);
            password = binding.passwordInput.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(LoginPage.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            }
            else if (password.isEmpty()) {
                Toast.makeText(LoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            }
            else {
                databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(username)) {
                            String getPassword = snapshot.child(username).child("password").getValue(String.class);
                            int getId = snapshot.child(username).child("userId").getValue(int.class);
                            int getAdmin = snapshot.child(username).child("isAdminAcc").getValue(int.class);

                            if (getPassword.equals(password)) {
                                // Username and password are correct
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_ID, String.valueOf(getId));
                                editor.putString(KEY_USERNAME, username);
                                if (rememberUser.isChecked()) {
                                    editor.putString(REMEMBER, "true");
                                }
                                editor.apply();

                                if (getAdmin == 1) {
                                    // If the user is an admin, redirect to admin page
                                    Intent adminPage = new Intent(LoginPage.this, AdminHomePage.class);
                                    startActivity(adminPage);
                                } else {
                                    // Redirect to PhoneVerification for OTP check as part of MFA
                                    Intent phoneVerification = new Intent(LoginPage.this, PhoneVerification.class);
                                    phoneVerification.putExtra("USERNAME", username); // Pass the username to PhoneVerification
                                    startActivity(phoneVerification);
                                }
                            } else {
                                // Password is incorrect
                                Toast.makeText(LoginPage.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Account does not exist
                            Toast.makeText(LoginPage.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        });
    }

    public void openHomePage(){
//        Intent home = new Intent(this, BookingPage.class);
        Intent home = new Intent(this, HomePage.class);
        startActivity(home);
    }

    public void backWelcomePage(View view){
        Intent backPage = new Intent(this, WelcomePage.class);
        startActivity(backPage);
    }

    public void signUpPage(View view){
        Intent signUpPage = new Intent(this, SignUpPage.class);
        startActivity(signUpPage);
    }
    public void adminPage(){
        Intent adminPage = new Intent(this, AdminHomePage.class);
        startActivity(adminPage);
    }
}