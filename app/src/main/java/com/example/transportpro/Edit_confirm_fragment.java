package com.example.transportpro;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.Observer;

public class Edit_confirm_fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);

        // Initialize your TextViews and other views here
        TextView confirmTitleTextView = view.findViewById(R.id.titleTextView);
        TextView confirmTrackNumberTextView = view.findViewById(R.id.trackNumberTextView);
        TextView confirmQuantityTextView = view.findViewById(R.id.quantityTextView);
        TextView confirmCollectedTextView = view.findViewById(R.id.collectedTextView);
        TextView confirmWeightTextView = view.findViewById(R.id.weightTextView);
        LinearLayout confirmLinearLayout = view.findViewById(R.id.confirm_container);

        // Initialize shared ViewModel
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe changes in the text data
        sharedViewModel.getTextData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String textData) {
                // Check if data is empty and hide LinearLayout if needed
                if (textData.isEmpty()) {
                    confirmLinearLayout.setVisibility(View.GONE);
                } else {
                    // Parse and set the data to the respective TextViews in the Confirm tab
                    String[] lines = textData.split("\n");
                    if (lines.length >= 5) {
                        confirmTitleTextView.setText(lines[0]);
                        confirmTrackNumberTextView.setText(lines[2]); // Update index to match your data
                        confirmQuantityTextView.setText(lines[3]); // Update index to match your data
                        confirmCollectedTextView.setText(lines[5]); // Update index to match your data
                        confirmWeightTextView.setText(lines[6]); // Update index to match your data
                    }

                    confirmLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Observe changes in the selected transport mode
        sharedViewModel.getTransportData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectedTransport) {
                TextView transport_method = view.findViewById(R.id.transport_method);
                transport_method.setVisibility(View.VISIBLE);
                // Handle the selected transport mode
                transport_method.setText(selectedTransport);
            }
        });

        sharedViewModel.getReceiverData().observe(getViewLifecycleOwner(), new Observer<ReceiverData>() {
            @Override
            public void onChanged(ReceiverData receiverData) {
                LinearLayout receiver_detail_container = view.findViewById(R.id.receiver_detail_container);
                receiver_detail_container.setVisibility(View.VISIBLE);
                TextView name = view.findViewById(R.id.confirm_name);
                TextView mobile = view.findViewById(R.id.confirm_mobile);
                TextView email = view.findViewById(R.id.confirm_email);
                TextView postcode = view.findViewById(R.id.confirm_postcode);
                TextView add1 = view.findViewById(R.id.confirm_add);

                // Set the received data to TextViews
                name.setText(receiverData.getName());
                mobile.setText(receiverData.getMobile());
                email.setText(receiverData.getEmail());
                postcode.setText(receiverData.getPostcode());
                add1.setText(receiverData.getAdd1());
            }
        });


        return view;
    }
}
