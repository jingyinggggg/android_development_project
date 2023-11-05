package com.example.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.transportpro.R;

public class update_warehouse extends AppCompatActivity {

    ImageButton header_button;

    Button confirm_dialog;
    Button cancel_dialog;

    Button update_order;
    Button update_warehouse;
    Button update_ready;
    Button clickedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_warehouse);

        // Find the TextView and Image Button in the header layout
        TextView headerTitle = findViewById(R.id.header_title);
        header_button = findViewById(R.id.header_btn);

        // Set the text based on the current page or context
        headerTitle.setText("Update Status");
        // Set the image button based on the current page or context
        header_button.setImageResource(R.drawable.back_button);

        /*Header Button function*/
        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_warehouse(view);
            }
        });


        confirm_dialog = findViewById(R.id.confirm_dialog);
        cancel_dialog = findViewById(R.id.cancel_dialog);
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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.success_dialog, null);

        // Find views in the custom layout
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);

        // Find the confirm_dialog button within the dialog view
        Button confirmDialogButton = dialogView.findViewById(R.id.confirm_dialog);

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
                    clickedButton.setBackgroundResource(R.drawable.btn_status_updated); // Replace with your desired background resource
                    clickedButton.setEnabled(false);
                    clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done_icon, 0, 0, 0);
                }
            }
        });

        // Customize the dialog content (e.g., set image and message)
        dialogMessage.setText("You have successfully changed your password.");

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
        Intent warehouseIntent = new Intent(update_warehouse.this, Warehouse.class);
        startActivity(warehouseIntent);
    }

    public void return_page(View v){
        Intent backIntent = new Intent(this,update_warehouse.class);
        startActivity(backIntent);
    }
}