package com.example.transportpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterWallet extends RecyclerView.Adapter<AdapterWallet.MyViewHolder> {
    Context context;
    ArrayList<WalletActivityClass> walletActivityClassArrayList;
    String currentUserId; // The current user ID
    private AppCompatActivity activity;

    public AdapterWallet(Context context, ArrayList<WalletActivityClass> walletActivityClassArrayList, AppCompatActivity activity){
        this.context = context;
        this.walletActivityClassArrayList = walletActivityClassArrayList;
        this.activity = activity;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public AdapterWallet.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.wallet_activity_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WalletActivityClass walletActivityClass = walletActivityClassArrayList.get(position);

            holder.activity.setText(walletActivityClass.getActivity());
            holder.date.setText(walletActivityClass.getDate());
            holder.amount.setText(walletActivityClass.getAmount());

    }

    @Override
    public int getItemCount() {
        return walletActivityClassArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView activity, date, amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.wallet_activity);
            date = itemView.findViewById(R.id.wallet_date);
            amount = itemView.findViewById(R.id.wallet_amount);
        }
    }

}
