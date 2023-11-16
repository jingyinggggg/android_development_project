package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WarehouseSuccessDialog extends AppCompatActivity {

    Context context;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_dialog_admin);

        ImageView dialogImage = findViewById(R.id.dialog_image);
        TextView dialogMessage = findViewById(R.id.dialog_message);
        Button confirmDialogButton = findViewById(R.id.confirm_dialog);
        TextView cancelDialogText = findViewById(R.id.cancel_dialog);

        String order_number = getIntent().getStringExtra("ordernumber");
        String order_location = getIntent().getStringExtra("order_location");
        String username = getIntent().getStringExtra("username");

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("OrderHistory").child(username).child(order_number);

        if (order_location.equals("China Warehouse")) {
            dialogMessage.setText("Confirm update the current order location to Malaysia warehouse? \n(Order No: " + order_number + ") " + username);
            confirmDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.child("order_location").setValue("Malaysia Warehouse");
                    Intent intent = new Intent(WarehouseSuccessDialog.this, UpdateWarehouseAdmin.class);
                    startActivity(intent);
                }
            });
        } else if (order_location.equals("Malaysia Warehouse")) {
            dialogMessage.setText("Confirm update the current order location to shipping to receiver address? \n(Order No: " + order_number + ") " + username);
            confirmDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.child("order_location").setValue("Shipping Receiver Address");
                    Intent intent = new Intent(WarehouseSuccessDialog.this, UpdateWarehouseAdmin.class);
                    startActivity(intent);
                }
            });
        } else if (order_location.equals("Shipping Receiver Address")) {
            dialogMessage.setText("Confirm update the status to Complete Ship to Receiver Address? \n(Order No: " + order_number + ") " + username);
            confirmDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.child("order_location").setValue("Complete Ship to Receiver Address");
                    reference.child("order_status").setValue("Delivered");
                    Intent intent = new Intent(WarehouseSuccessDialog.this, UpdateWarehouseAdmin.class);
                    startActivity(intent);
                }
            });
        }

        cancelDialogText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WarehouseSuccessDialog.this, UpdateWarehouseAdmin.class);
                startActivity(intent);
            }
        });


    }
}
