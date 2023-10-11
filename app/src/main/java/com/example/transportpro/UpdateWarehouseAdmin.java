package com.example.transportpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateWarehouseAdmin extends AppCompatActivity {

    ImageButton backArrow;

    Button confirm_dialog;
    TextView cancel_dialog;

    Button update_order;
    Button update_warehouse;
    Button update_ready;
    Button clickedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_warehouse);

        // Find the TextView and Image Button in the header layout
        backArrow = findViewById(R.id.backArrow);
        /*Header Button function*/
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_warehouse(view);
            }
        });


        update_order = findViewById(R.id.update_order_status);
        update_warehouse = findViewById(R.id.update_warehouse_status);
        update_ready = findViewById(R.id.update_ready_status);


        /*Update Order, Update Warehouse and Update ready to ship button function*/
        update_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = update_order; // Store the clicked button
                showConfirmDialog(view);
            }
        });

        update_warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = update_warehouse; // Store the clicked button
                showConfirmDialog(view);
            }
        });

        update_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = update_ready; // Store the clicked button
                showConfirmDialog(view);
            }
        });

    }

    public void showConfirmDialog(View v){
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.success_dialog_admin, null);

        // Find views in the custom layout
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);

        // Find the confirm_dialog button within the dialog view
        Button confirmDialogButton = dialogView.findViewById(R.id.confirm_dialog);
        TextView cancelDialogText = dialogView.findViewById(R.id.cancel_dialog);
        // Set an OnClickListener for the confirm_dialog button
        confirmDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform the action when the confirm button is clicked
                // add code for updating the status

                // Dismiss the dialog
                AlertDialog dialog = (AlertDialog) view.getTag();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                // After update, change button background and disable it
                if (clickedButton != null) {
                    int color = Color.parseColor("#5FDF64");
                    clickedButton.setBackgroundColor(color); // Replace with your desired background resource
                    clickedButton.setEnabled(false);
                    clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done_icon, 0, 0, 0);
                }
            }
        });
        cancelDialogText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getIntent());
            }
        });

        // Create the custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);


        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Attach the dialog to the confirm_dialog button to dismiss it later
        confirmDialogButton.setTag(dialog);
    }

    public void redirect_warehouse(View v){
        Intent warehouseIntent = new Intent(UpdateWarehouseAdmin.this, WarehouseAdmin.class);
        startActivity(warehouseIntent);
    }

    public void return_page(View v){
        Intent backIntent = new Intent(this, UpdateWarehouseAdmin.class);
        startActivity(backIntent);
    }
}