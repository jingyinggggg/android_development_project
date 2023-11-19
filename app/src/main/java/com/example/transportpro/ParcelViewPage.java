package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ParcelViewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parcel_view_page);

        TextView description = findViewById(R.id.descriptionInput);
        String descriptionInput = "Color: Blue\nSize: M";
        description.setText(descriptionInput);

        TextView characterCount = findViewById(R.id.characterCountTextView);

        // Calculate and set the character count when setting the text
        int currentLength = descriptionInput.length();
        int maxLength = 160;
        String counterText = currentLength + "/" + maxLength;
        characterCount.setText(counterText);

        Button done = findViewById(R.id.doneButton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backParcelPage(v);
            }
        });

    }

    public void backParcelPage(View view){
        Intent parcelPage = new Intent(this, ParcelPage.class);
        startActivity(parcelPage);
    }
}