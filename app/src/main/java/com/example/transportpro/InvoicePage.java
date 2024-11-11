package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class InvoicePage extends AppCompatActivity {
    TextView inv_text, order_number_text, payment_method_text, payment_status_text, date_text, payment_amount_text;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_page);

        Intent intent = getIntent();

        String invoice_number = intent.getStringExtra("invoice");
        String order_number = intent.getStringExtra("order");
        String payment_method = intent.getStringExtra("payment_method");
        String payment_status = intent.getStringExtra("payment_status");
        String payment_amount = intent.getStringExtra("payment_amount");
        String date = intent.getStringExtra("date");

        inv_text = findViewById(R.id.inv_text);
        order_number_text = findViewById(R.id.order_number_text);
        payment_method_text = findViewById(R.id.payment_method_text);
        payment_status_text = findViewById(R.id.payment_status_text);
        payment_amount_text = findViewById(R.id.payment_amount);
        date_text = findViewById(R.id.date_text);

        inv_text.setText(invoice_number);
        order_number_text.setText(order_number);
        payment_method_text.setText(payment_method);
        payment_status_text.setText(payment_status);
        payment_amount_text.setText("- RM " + payment_amount);
        date_text.setText(date);

        back = findViewById(R.id.backArrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homepage = new Intent(InvoicePage.this, Payment.class);
                startActivity(homepage);
            }
        });




    }
}