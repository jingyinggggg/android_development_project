package com.example.transportpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AllPayment extends Fragment {
    public Button selectPaymentButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_payment, container, false);

        selectPaymentButton = view.findViewById(R.id.select_payment);
        selectPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectPaymentDialog();
            }

        });
        return view;
    }

    public void showSelectPaymentDialog(){
        PaymentMethodDialog dialogFragment = new PaymentMethodDialog();
        dialogFragment.show(getParentFragmentManager(), "PaymentMethodDialogFragment");
    }
}