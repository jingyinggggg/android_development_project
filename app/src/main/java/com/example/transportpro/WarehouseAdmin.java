package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class WarehouseAdmin extends AppCompatActivity {

    ImageButton header_button;

    Button chn_allParcel;
    Button msia_allParcel;
    Button chn_trackNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        // Find the TextView and Image Button in the header layout
        TextView headerTitle = findViewById(R.id.header_title);
        header_button = findViewById(R.id.header_btn);

        // Set the text based on the current page or context
        headerTitle.setText("View Warehouse");
        // Set the image button based on the current page or context
        header_button.setImageResource(R.drawable.back_button);

        chn_allParcel = findViewById(R.id.chn_allParcel);
        chn_allParcel.setPaintFlags(chn_allParcel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        msia_allParcel = findViewById(R.id.msia_allParcel);
        msia_allParcel.setPaintFlags(msia_allParcel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        chn_trackNo = findViewById(R.id.chn_trackNo);

        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_homepage(view);
            }
        });

        chn_trackNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){redirect_update_warehouse(view);}
        });
    }

    public void redirect_homepage(View v){
        Intent homepageIntent = new Intent(WarehouseAdmin.this, AdminHomePage.class);
        startActivity(homepageIntent);
    }

    public void redirect_update_warehouse(View v){
        Intent updateWarehouseIntent = new Intent(WarehouseAdmin.this, UpdateWarehouseAdmin.class);
        startActivity(updateWarehouseIntent);
    }

}