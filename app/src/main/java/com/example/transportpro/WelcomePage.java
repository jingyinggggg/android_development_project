package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    TextView logo;
    Button btn_getStarted;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    private static final String REMEMBER = "remember";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        //Set the style for the name of application
        logo = (TextView) findViewById(R.id.logo); //Find the logo id
        String logoText = "TransportPro"; //Assign the logo name
        SpannableString ss = new SpannableString(logoText);
        StyleSpan boldItalicSpan = new StyleSpan(Typeface.BOLD_ITALIC); //Set the text become bold and italic
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(51,134,197)); //Set the text color
        ss.setSpan(boldItalicSpan, 9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Set the specific text to bold and italic
        ss.setSpan(colorSpan, 9,12,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Set the specific text to certain color
        logo.setText(ss); //Apply the text style to the logo name

        btn_getStarted = (Button) findViewById(R.id.btn_GetStarted);
        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String id = sharedPreferences.getString(KEY_ID,null);
        String checkbox = sharedPreferences.getString(REMEMBER,null);

        if (id != null && checkbox != null){
            try {
                int userId = Integer.parseInt(id);
                if (checkbox != null) {
                    if (userId == 1) {
                        Intent adminPage = new Intent(this, AdminHomePage.class);
                        startActivity(adminPage);
                    } else {
                        Intent home = new Intent(this, HomePage.class);
                        startActivity(home);
                    }
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the exception, possibly by going to the login activity
                Intent login = new Intent(this, LoginPage.class);
                startActivity(login);
            }
        }
    }

    public void openLoginPage(){
        Intent login = new Intent(this, LoginPage.class);
        startActivity(login);
    }
}