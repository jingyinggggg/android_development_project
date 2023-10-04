package com.example.transportpro;

import android.os.Bundle;
import android.text.SpannableString;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.transportpro.ReceiverData;

public class Edit_receiver_fragment extends Fragment {
    TextView usethisAcc;
    EditText nameEditText;
    EditText mobileEditText;
    EditText emailEditText;
    EditText postcodeEditText;
    EditText add1EditText;
    EditText add2EditText;
    EditText add3EditText;

    // Initialize the SharedViewModel once
    SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receiver, container, false);

        usethisAcc = view.findViewById(R.id.useThisAccountadd);

        SpannableString content = new SpannableString("Use this account address");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        usethisAcc.setText(content);

        nameEditText = view.findViewById(R.id.nameTextBox);
        mobileEditText = view.findViewById(R.id.MobileTextBox);
        emailEditText = view.findViewById(R.id.EmailTextBox);
        postcodeEditText = view.findViewById(R.id.PostcodeTextBox);
        add1EditText = view.findViewById(R.id.add1TextBox);
        add2EditText = view.findViewById(R.id.add2TextBox);
        add3EditText = view.findViewById(R.id.add3TextBox);

        // Initialize the SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);




        return view;
    }

    private void setDataInSharedViewModel() {
        String name = nameEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String postcode = postcodeEditText.getText().toString();
        String add1 = add1EditText.getText().toString();
        String add2 = add2EditText.getText().toString();
        String add3 = add3EditText.getText().toString();

        // Create a ReceiverData object with the EditText data
        ReceiverData receiverData = new ReceiverData(name, mobile, email, postcode, add1, add2, add3);

        // Set receiver data in the shared ViewModel
        sharedViewModel.setReceiverData(receiverData);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Automatically call setDataInSharedViewModel when leaving the fragment
        setDataInSharedViewModel();
    }
}
