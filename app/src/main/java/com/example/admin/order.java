package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class order extends AppCompatActivity {

    ImageButton header_button;

    Button order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        /*Header Button and Header Word*/
        // Find the TextView and Image Button in the header layout
        TextView headerTitle = findViewById(R.id.header_title);
        header_button = findViewById(R.id.header_btn);

        // Set the text based on the current page or context
        headerTitle.setText("View Order");
        // Set the image button based on the current page or context
        header_button.setImageResource(R.drawable.back_button);

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
        Intent homepageIntent = new Intent(order.this, homepage.class);
        startActivity(homepageIntent);
    }

    public void redirect_order_request(View v){
        Intent orderRequestIntent = new Intent(order.this, order_request.class);
        startActivity(orderRequestIntent);
    }
}