package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;


public class PricingPage extends AppCompatActivity {

    Float price;
    String[] transportationType = {"Air Cargo", "Ship Cargo"}; //Transportation type array
    String[] area = {"West", "East"}; //Area type array
    AutoCompleteTextView transportationSelectType, areaSelectType;
    TextInputEditText weightInput, displayPrice;
    ArrayAdapter<String> adapterTransportations, adapterAreas;
    Button check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pricing_page);

        //Find the transportationType field
        transportationSelectType = findViewById(R.id.transportationTypeText);
        //Initialize the dropdown list for user to select the transportation type
        adapterTransportations = new ArrayAdapter<String>(PricingPage.this, R.layout.dropdown_list, transportationType);
        transportationSelectType.setAdapter(adapterTransportations);

        //Find the areaType field
        areaSelectType = findViewById(R.id.areaTypeText);
        //Initialize the dropdown list for user to select the area type
        adapterAreas = new ArrayAdapter<String>(PricingPage.this, R.layout.dropdown_list, area);
        areaSelectType.setAdapter(adapterAreas);

        //Find the weightInput field
        weightInput = findViewById(R.id.weightText);

        //Find the check button
        check = findViewById(R.id.check);

        // Define colors for the enabled and disabled states
        int enabledColor = Color.parseColor("#3D3F99");
        int disabledColor = Color.GRAY;

        // Create a ColorStateList for the button's background
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled}, // enabled
                        new int[]{-android.R.attr.state_enabled} // disabled
                },
                new int[]{
                        enabledColor,
                        disabledColor
                }
        );

        // Set the color state list as the button's background
        check.setBackgroundTintList(colorStateList);

        //Disable the check button
        check.setEnabled(false);

        //Add TextWatcher to the weightInput field
        weightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Check if all three input fields are not null or empty
                boolean enableButton = !s.toString().isEmpty() && !transportationSelectType.getText().toString().isEmpty() && !areaSelectType.getText().toString().isEmpty();

                //Enable check button
                check.setEnabled(enableButton);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Add OnItemSelectedListener to transportationSelectType and areaSelectType fields
        transportationSelectType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Check if all three input fields are not null or empty
                boolean enableButton = !weightInput.getText().toString().isEmpty() && !transportationSelectType.getText().toString().isEmpty() && !areaSelectType.getText().toString().isEmpty();

                //Enable the check button
                check.setEnabled(enableButton);
            }
        });

        areaSelectType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Check if all three input fields are not null or empty
                boolean enableButton = !weightInput.getText().toString().isEmpty() && !transportationSelectType.getText().toString().isEmpty() && !areaSelectType.getText().toString().isEmpty();

                //Enable the check button
                check.setEnabled(enableButton);
            }
        });

        //Add a click listener to the check button
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Convert the selected area to string
                String selectedArea = areaSelectType.getText().toString();

                //Convert the selectedTransportation to string
                String selectedTransportation = transportationSelectType.getText().toString();

                //Convert the weightInput to string then to float
                String convertWeightInputToString = weightInput.getText().toString();
                Float convertWeightToFloat = Float.parseFloat(convertWeightInputToString);

                //Call the calculateShippingCost method to calculate the shipping cost based on the user input
                price = calculateShippingCost(selectedTransportation, convertWeightToFloat, selectedArea);

                //Display the price into two decimal places
                DecimalFormat df = new DecimalFormat("0.00");
                displayPrice = findViewById(R.id.priceText);
                displayPrice.setText("RM " + df.format(price));
            }
        });

    }

    private float calculateShippingCost(String transport, Float weight, String area){

        float finalCost = 0;

        //Calculation format is based on the price list that provided in the app
        if(transport != null && weight > 0 && area != null){
            if(transport.equals("Air Cargo")){
                if(area.equals("West")){
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 20;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 18) + 100;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 15) + 100 + 90;
                    }
                } else{
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 23;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 21) + 115;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 18) + 115 + 105;
                    }
                }
            } else{
                if(area.equals("West")){
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 12;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 10) + 60;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 7) + 60 + 50;
                    }
                } else{
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 14;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 12) + 70;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 9) + 70 + 60;
                    }
                }
            }
        }else{
            finalCost = 0;
        }

        return  finalCost;

    }

    public void backHomePage(View view){
        Intent loginPage = new Intent(this, HomePage.class);
        startActivity(loginPage);
    }

}