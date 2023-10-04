package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Orderhistory extends AppCompatActivity {

    ImageView back_btn;
    BottomNavigationView bottombar;
    LinearLayout order_req;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderhistory);


        back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Orderhistory.this,MainActivity.class);
                startActivity(intent);
            }
        });

        order_req = findViewById(R.id.order_req);
        order_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Orderhistory.this,Edit_order.class);
                startActivity(intent);
            }
        });

    }
}