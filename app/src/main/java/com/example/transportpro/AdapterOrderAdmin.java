package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import java.util.HashMap;

public class AdapterOrderAdmin extends RecyclerView.Adapter<AdapterOrderAdmin.ViewHolder> {

    Context context;
    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;
    String username;
    String orderNo;
    int parcel_qty;

    private AppCompatActivity activity;

    public AdapterOrderAdmin(Context context, ArrayList<OrderHistoryClass> orderHistoryClassArrayList, AppCompatActivity activity){
        this.context = context;
        this.orderHistoryClassArrayList = orderHistoryClassArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterOrderAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_admin_list,parent, false );
        return new AdapterOrderAdmin.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderAdmin.ViewHolder holder, int position) {

        OrderHistoryClass order = orderHistoryClassArrayList.get(position);
        if (order != null){
            orderNo = order.getOrder_number();
            parcel_qty = order.getParcel_quantity();

            holder.order_id.setText("Order ID: " + orderNo + "\nNo of Tracks: " +parcel_qty+ "\nCustomer ID: " +order.userId );

            holder.order_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirect_updateOrder(order);
                }
            });
        }else {
            holder.order_id.setText("No Order found" );

            holder.order_id.setOnClickListener(null);
        }


    }

    @Override
    public int getItemCount() {
        return orderHistoryClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button order_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order_id = itemView.findViewById(R.id.order_id);
        }
    }

    private void redirect_updateOrder(OrderHistoryClass order){
        Intent updateOrderIntent = new Intent(activity, OrderRequestAdmin.class);
        updateOrderIntent.putExtra("userId", order.userId);
        updateOrderIntent.putExtra("order_number", order.getOrder_number());
        updateOrderIntent.putExtra("parcel_qty",order.getParcel_quantity());

        activity.startActivity(updateOrderIntent);
    }
}
