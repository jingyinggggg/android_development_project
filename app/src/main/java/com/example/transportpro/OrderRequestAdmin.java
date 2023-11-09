package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class OrderRequestAdmin extends AppCompatActivity {

    ImageButton header_button;

    Button accept_order;
    Button decline_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);

        header_button = findViewById(R.id.backArrow);

        /*Header Button Function*/
        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_order(view);
            }
        });

        accept_order = findViewById(R.id.accept_order);
        decline_order = findViewById(R.id.decline_order);

        /*accept order button function */
        accept_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //accept order function here
                redirect_order(view);
            }
        });

        /*decline order button function*/
        decline_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decline order function
                redirect_order(view);
            }
        });
    }

    public void redirect_order(View v){
        Intent orderIntent = new Intent(OrderRequestAdmin.this, OrderAdmin.class);
        startActivity(orderIntent);
    }

}