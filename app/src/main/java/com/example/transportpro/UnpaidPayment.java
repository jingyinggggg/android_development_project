package com.example.transportpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UnpaidPayment extends Fragment {
    public Button selectPaymentButton;
    RecyclerView recyclerView;
    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;
    AdapterPayment adapterPayment;
    DatabaseReference database;
    String username;

    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unpaid_payment, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("localstorage", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("userName", null);

        recyclerView = view.findViewById(R.id.recycle_payment);
        database = FirebaseDatabase.getInstance().getReference("OrderHistory").child(username);
        recyclerView.setHasFixedSize(true);

        orderHistoryClassArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FragmentManager fragmentManager = getChildFragmentManager();

        adapterPayment = new AdapterPayment(getContext(), orderHistoryClassArrayList, "Unpaid",fragmentManager);
        recyclerView.setAdapter(adapterPayment);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderHistoryClassArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderHistoryClass orderHistoryClass = dataSnapshot.getValue(OrderHistoryClass.class);
                    orderHistoryClassArrayList.add(orderHistoryClass);
                }
                adapterPayment.notifyDataSetChanged();

                if (orderHistoryClassArrayList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

    public void showPaymentMethodSelectionDialog(View v){
        Bundle args = new Bundle();

        args.putString("priceText", "RM 100");

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