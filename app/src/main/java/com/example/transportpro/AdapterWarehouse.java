package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterWarehouse extends RecyclerView.Adapter<AdapterWarehouse.ViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    private AppCompatActivity activity;
    SharedPreferences sharedPreferences;

    public AdapterWarehouse(Context context, ArrayList<User> userArrayList, AppCompatActivity activity){
        this.context = context;
        this.userArrayList = userArrayList;
        this.activity = activity;
        this.sharedPreferences = context.getSharedPreferences("WarehousePrefs", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public AdapterWarehouse.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.warehouse_list,parent, false );
        return new AdapterWarehouse.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterWarehouse.ViewHolder holder, int position) {

        User user = userArrayList.get(position);
        int id = user.getUserId();

        holder.trackNo.setText("Customer ID: " + id );
        holder.trackNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { redirect_updateWarehouse(user); }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button trackNo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNo = itemView.findViewById(R.id.trackNo);
        }
    }

    private void redirect_updateWarehouse(User users){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent updateWarehouse = new Intent(activity,UpdateWarehouseAdmin.class);
        editor.putString("warehouse:UserID", String.valueOf(users.userId));
        editor.putString("warehouse:username", users.username);
        editor.apply();

        activity.startActivity(updateWarehouse);
    }
}
