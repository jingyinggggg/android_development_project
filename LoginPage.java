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

import org.mindrot.jbcrypt.BCrypt;

public class LoginPage extends AppCompatActivity {

    String username, password;
    LoginPageBinding binding;
    TextView logo, signUp;
    CheckBox rememberUser;
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

        //Set the style for the name of application
        logo = (TextView) findViewById(R.id.logoName); //Find the logo id
        String logoText = "TransportPro"; //Assign the logo name
        SpannableString ss = new SpannableString(logoText);
        StyleSpan boldItalicSpan = new StyleSpan(Typeface.BOLD_ITALIC); //Set the text become bold and italic
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(51,134,197)); //Set the text color
        ss.setSpan(boldItalicSpan, 9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Set the specific text to bold and italic
        ss.setSpan(colorSpan, 9,12,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Set the specific text to certain color
        logo.setText(ss); //Apply the text style to the logo name

        //Set the style for the sign up text view
        signUp = (TextView) findViewById(R.id.signUp);
        SpannableString signUpText = new SpannableString(signUp.getText().toString());
        signUpText.setSpan(new UnderlineSpan(), 0, signUpText.length(), 0);
        signUp.setText(signUpText);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String id = sharedPreferences.getString(KEY_ID,null);

        rememberUser = (CheckBox) findViewById(R.id.rememberMe);

        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = binding.usernameInput.getText().toString().toLowerCase(Locale.ROOT);
                password = binding.passwordInput.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(LoginPage.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(username)){

                                final String storedPasswordHash = snapshot.child(username).child("password").getValue(String.class);
                                final int getAdmin = snapshot.child(username).child("isAdminAcc").getValue(int.class);
                                final int getId = snapshot.child(username).child("userId").getValue(int.class);
                                final int getDeletedAcc = snapshot.child(username).child("isDeletedAcc").getValue(int.class);

                                if (BCrypt.checkpw(password, storedPasswordHash)) {
                                    if (getDeletedAcc == 0) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(KEY_ID, String.valueOf(getId));
                                        editor.putString(KEY_USERNAME, username);
                                        if (rememberUser.isChecked()) {
                                            editor.putString(REMEMBER, "true");
                                        }
                                        editor.apply();
                                        if (getAdmin == 1) {
                                            adminPage();
                                        } else {
                                            openHomePage();
                                        }
                                        Toast.makeText(LoginPage.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginPage.this, "This account has been deleted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginPage.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginPage.this, "Username not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle potential errors here
                        }
                    });
                }
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