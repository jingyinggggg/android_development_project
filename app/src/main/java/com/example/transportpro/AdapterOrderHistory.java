package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterOrderHistory extends RecyclerView.Adapter<AdapterOrderHistory.MyViewHolder> {
    Context context;
    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;
    String currentUserId; // The current user ID
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    private static final String Order_Number = "orderNumber";

    public AdapterOrderHistory(Context context, ArrayList<OrderHistoryClass> orderHistoryClassArrayList) {
        this.context = context;
        this.orderHistoryClassArrayList = orderHistoryClassArrayList;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public AdapterOrderHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.orderhistory_list, parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        OrderHistoryClass orderHistoryClass = orderHistoryClassArrayList.get(position);
        if(currentUserId != null && currentUserId.equals(String.valueOf(orderHistoryClass.getUserId()))){
            String order_number = orderHistoryClass.getOrder_number();
            if (order_number != null) {

                holder.category.setText(orderHistoryClass.getCategory());

                holder.order_number.setText(orderHistoryClass.getOrder_number());

                if(orderHistoryClass.getOrder_status().equals("Packing") || orderHistoryClass.getOrder_status().equals("Delivering")){
                    holder.history_container.setBackgroundResource(R.drawable.requesting_rec);
                    holder.status_image.setImageResource(R.drawable.pending);
                }
                else{
                    holder.history_container.setBackgroundResource(R.drawable.delivered_rec);
                    holder.status_image.setImageResource(R.drawable.delivered);
                    holder.order_status.setTextColor(ContextCompat.getColor(context, R.color.lightGreen));
                }

                holder.order_status.setText("Order is " + orderHistoryClass.getOrder_status());
                holder.order_date.setText(orderHistoryClass.getDate());

                if(orderHistoryClass.getTransport_type().equals("Air")){
                    holder.transport_type.setBackgroundResource(R.drawable.order_air);
                } else if (orderHistoryClass.getTransport_type().equals("Sea")) {
                    holder.transport_type.setBackgroundResource(R.drawable.order_ship);
                }

                holder.history_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Order_Number, orderHistoryClass.getOrder_number());
                        editor.apply();

                        Intent intent = new Intent(context, Edit_order.class);
                        context.startActivity(intent);
                    }
                });


            }


        }


    }

    @Override
    public int getItemCount() {
        return orderHistoryClassArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout history_container;
        TextView category, order_number,track_item_qty, order_status, order_date;
        ImageView transport_type, status_image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            history_container = itemView.findViewById(R.id.history_container);
            category = itemView.findViewById(R.id.category);
            order_number = itemView.findViewById(R.id.order_number);
            track_item_qty = itemView.findViewById(R.id.track_item_qty);
            order_status = itemView.findViewById(R.id.order_status);
            order_date = itemView.findViewById(R.id.order_date);
            transport_type = itemView.findViewById(R.id.transport_type);
            status_image = itemView.findViewById(R.id.status_image);
        }
    }
}
