package com.example.transportpro;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    TextView logo, signUp;
    CheckBox rememberUser;
    Button login;
    Button admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

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

        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        rememberUser = (CheckBox) findViewById(R.id.rememberMe);
        rememberUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(LoginPage.this, "Checked",Toast.LENGTH_SHORT).show();
                } else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(LoginPage.this, "Unchecked",Toast.LENGTH_SHORT).show();
                }
            }
        });

        admin = (Button) findViewById(R.id.admin);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent admin = new Intent(LoginPage.this,AdminHomePage.class);
                startActivity(admin);
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


}