package com.example.transportpro;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectFragment extends Fragment {
    CheckBox checkBox;
    LinearLayout linearLayout;
    SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select, container, false);

        checkBox = view.findViewById(R.id.checkbox);
        linearLayout = view.findViewById(R.id.order_itemDesc);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passTextDataToConfirmTab();
                    linearLayout.setBackgroundResource(R.drawable.order_selected);
                } else {
                    clearConfirmTabContent();
                    linearLayout.setBackgroundResource(R.drawable.order_rectangle);
                }
            }
        });

        return view;
    }

    private void passTextDataToConfirmTab() {
        // Get the text data from the LinearLayout
        StringBuilder textData = new StringBuilder();

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);

            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                textData.append(textView.getText()).append("\n");
            } else if (childView instanceof LinearLayout) {
                LinearLayout innerLinearLayout = (LinearLayout) childView;
                for (int j = 0; j < innerLinearLayout.getChildCount(); j++) {
                    View innerChildView = innerLinearLayout.getChildAt(j);
                    if (innerChildView instanceof TextView) {
                        TextView innerTextView = (TextView) innerChildView;
                        textData.append(innerTextView.getText()).append("\n");
                    }
                }
            }
        }

        // Set the text data in the shared ViewModel
        sharedViewModel.setTextData(textData.toString());
    }

    private void clearConfirmTabContent() {
        // Clear the text data in the shared ViewModel
        sharedViewModel.setTextData("");
    }
}
