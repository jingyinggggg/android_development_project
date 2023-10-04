package com.example.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AccountPage extends AppCompatActivity {
    public ImageButton setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        setting = (ImageButton) findViewById(R.id.backArrow);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backSettingPage();
            }
        });
    }

    public void backSettingPage(){
        Intent settingPage = new Intent(this, Setting.class);
        startActivity(settingPage);
    }
}