package com.example.transportpro;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        String username = user.getUsername();


        DatabaseReference orderHistRef = FirebaseDatabase.getInstance().getReference("OrderHistory");
        orderHistRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int deliveringCount =0;
                for (DataSnapshot order : snapshot.getChildren()) {
                    String orderStatus = order.child("order_status").getValue(String.class);
                    if ("Delivering".equals(orderStatus)) {
                        deliveringCount++;
                    }
                }
                if (deliveringCount == 0){
                    holder.trackNo.setText("Customer ID: " + id + "\nThere are no orders in delivery");
                }else{
                    holder.trackNo.setText("Customer ID: " + id + "\nOrders in Delivery: " + deliveringCount);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
