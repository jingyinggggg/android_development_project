package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WarehouseSuccessDialog extends AppCompatActivity {

    Context context;
    FirebaseDatabase db;
    DatabaseReference reference;
    String username,order_number,order_location;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_dialog_admin);

        ImageView dialogImage = findViewById(R.id.dialog_image);
        TextView dialogMessage = findViewById(R.id.dialog_message);
        Button confirmDialogButton = findViewById(R.id.confirm_dialog);
        TextView cancelDialogText = findViewById(R.id.cancel_dialog);

        order_number = getIntent().getStringExtra("ordernumber");
        order_location = getIntent().getStringExtra("order_location");
        username = getIntent().getStringExtra("username");

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("OrderHistory").child(username).child(order_number);
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User").child(username);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userId = snapshot.child("userId").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                    reference.child("order_status").setValue("Delivered").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (username!=null) {
                                String title = "order";
                                String type = "order_delivered";
                                String content = order_number;
                                String imageResId = String.valueOf(R.drawable.ship);

                                int is_read = 0;

                                DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("Notification").child(username).child(title).child(content);

                                NotificationClass notificationClass = new NotificationClass(userId,imageResId, title, type, content, is_read);
                                notificationReference.setValue(notificationClass);
                            }
                        }
                    });

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
