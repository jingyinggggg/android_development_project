package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    }

    public void openLoginPage(){
        Intent login = new Intent(this, LoginPage.class);
        startActivity(login);
    }
}