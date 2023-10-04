package com.example.transportpro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TransportFragment extends Fragment {


    RadioButton seabtn;
    RadioButton airBtn;
    LinearLayout seaL;
    LinearLayout airL;
    LinearLayout yesButton;
    LinearLayout noButton;
    int selectedButtonId;
    SharedViewModel sharedViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transport, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        seabtn = view.findViewById(R.id.seaBtn);
        airBtn = view.findViewById(R.id.airBtn);
        seaL = view.findViewById(R.id.seaLayout);
        airL = view.findViewById(R.id.airLayout);
        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);

        selectedButtonId = R.id.noButton;

        seabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seaL.setBackgroundResource(R.drawable.order_selected);


                airL.setBackgroundResource(R.drawable.order_rectangle);
                sharedViewModel.setTransportData("Sea");
            }
        });

        airBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                airL.setBackgroundResource(R.drawable.order_selected);


                seaL.setBackgroundResource(R.drawable.order_rectangle);
                sharedViewModel.setTransportData("Air");
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yesButton.setBackgroundResource(R.drawable.order_selected);
                noButton.setBackgroundResource(R.drawable.order_rectangle);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noButton.setBackgroundResource(R.drawable.order_selected);
                yesButton.setBackgroundResource(R.drawable.order_rectangle);
            }
        });



        return view;
    }
    public void onButtonClick(View view) {
        if (view.getId() == R.id.yesButton) {
            selectedButtonId = R.id.yesButton;
            yesButton.setBackgroundResource(R.drawable.order_selected);
            noButton.setBackgroundResource(R.drawable.order_rectangle);

        } else if (view.getId() == R.id.noButton) {
            selectedButtonId = R.id.noButton;
            noButton.setBackgroundResource(R.drawable.order_selected);
            yesButton.setBackgroundResource(R.drawable.order_rectangle);
        }

        // Update the button colors
    }


}