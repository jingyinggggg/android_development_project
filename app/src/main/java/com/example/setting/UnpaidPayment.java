package com.example.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

public class UnpaidPayment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_unpaid_payment, container, false);


        Button selectPaymentButton = rootView.findViewById(R.id.pay);
        selectPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaymentMethodSelectionDialog(view);
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unpaid_payment, container, false);
    }

    public void showPaymentMethodSelectionDialog(View v){
        Dialog paymentMethodDialog = new Dialog(requireContext());
        paymentMethodDialog.setContentView(R.layout.payment_method_dialog);

        RadioButton transportPro_wallet = paymentMethodDialog.findViewById(R.id.transportPro_wallet);
        RadioButton tng_wallet = paymentMethodDialog.findViewById(R.id.tng_wallet);
        RadioButton onlineBanking = paymentMethodDialog.findViewById(R.id.onlineBanking);
        RadioButton debit_credit_card = paymentMethodDialog.findViewById(R.id.debit_credit_card);

        transportPro_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
            }
        });

        tng_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
            }
        });

        onlineBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
            }
        });

        debit_credit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
            }
        });

    }
}