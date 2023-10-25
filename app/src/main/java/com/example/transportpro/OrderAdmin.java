package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderAdmin extends AppCompatActivity {

    ImageView header_button;

    Button order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        /*Header Button and Header Word*/
        // Find the TextView and Image Button in the header layout
        header_button = findViewById(R.id.backArrow);

        /*Header Button Function*/
        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_homepage(view);
            }
        });

        order = findViewById(R.id.order_id);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_order_request(view);
            }
        });
    }

    public void redirect_homepage(View v){
        Intent homepageIntent = new Intent(OrderAdmin.this, AdminHomePage.class);
        startActivity(homepageIntent);
    }

    public void redirect_order_request(View v){
        Intent orderRequestIntent = new Intent(OrderAdmin.this, OrderRequestAdmin.class);
        startActivity(orderRequestIntent);
    }
}