package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class PaymentMethodDialog extends DialogFragment {
    public RadioButton transportPro_wallet;
    public RadioButton tng_wallet;
    public RadioButton onlineBanking;
    public RadioButton debit_credit_card;
    public Button pay;
    private RadioButton selectedRadioButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_method_dialog, container, false);
        super.onCreate(savedInstanceState);

        transportPro_wallet = view.findViewById(R.id.transportPro_wallet);
        tng_wallet = view.findViewById(R.id.tng_wallet);
        onlineBanking = view.findViewById(R.id.onlineBanking);
        debit_credit_card = view.findViewById(R.id.debit_credit_card);

        pay = view.findViewById(R.id.payButton);

        // Set an OnClickListener for each radio button to track the selected radio button
        transportPro_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = transportPro_wallet;
            }
        });

        tng_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = tng_wallet;
            }
        });

        onlineBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = onlineBanking;
            }
        });

        debit_credit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = debit_credit_card;
            }
        });

        // Set an OnClickListener for the "Pay" button
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedRadioButton != null) {
                    // If a radio button is selected, proceed to WalletPage
                    goWalletPage(view);
                } else {
                    // If no radio button is selected, show a message or handle the case as needed
                    // For example, display a Toast message
                    Toast.makeText(getActivity(), "Please select a payment method.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void goWalletPage(View view) {
        Intent walletPage = new Intent(requireActivity(), WalletPage.class);
        startActivity(walletPage);
    }
}
