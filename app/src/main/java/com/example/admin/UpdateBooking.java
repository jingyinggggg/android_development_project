package com.example.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.transportpro.R;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateBooking extends AppCompatActivity {


    ImageButton header_button;
    Button accept_booking;
    Button decline_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);

        /*Header Button and Header Word*/
            // Find the TextView and Image Button in the header layout
            TextView headerTitle = findViewById(R.id.header_title);
            header_button = findViewById(R.id.header_btn);

            // Set the text based on the current page or context
            headerTitle.setText("Update Booking");
            // Set the image button based on the current page or context
            header_button.setImageResource(R.drawable.back_button);

        /*Header Button Function*/
        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_view_booking(view);
            }
        });

        /*Description and character word count*/
            TextView descTextView = findViewById(R.id.desc);
            TextView charCountTextView = findViewById(R.id.charCount);

        /*Weight and Error message*/
        EditText weightEditText = findViewById(R.id.weight);
        TextView errorMessageTextView = findViewById(R.id.error_message);

        accept_booking = findViewById(R.id.accept_booking);
        decline_booking = findViewById(R.id.decline_booking);

        // Check for validation of the edit text of weight
        accept_booking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String weight = weightEditText.getText().toString().trim();

                if (weight.isEmpty()) {
                        // Set the error message to be visible with red-colored text
                        errorMessageTextView.setVisibility(View.VISIBLE);
                    } else {
                        // Hide the error message
                        errorMessageTextView.setVisibility(View.GONE);
                        redirect_view_booking(view);
                    }
                }
        });

        /*Count number of words in the Description*/
        descTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Count the number of characters
                int charCount = s.length();

                // Update the charCountTextView with the character count
                charCountTextView.setText(String.valueOf(charCount));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
    }

        public void redirect_view_booking(View v){
            Intent viewBookingIntent = new Intent(UpdateBooking.this, CustomerBookingPage.class);
            startActivity(viewBookingIntent);
        }
}
