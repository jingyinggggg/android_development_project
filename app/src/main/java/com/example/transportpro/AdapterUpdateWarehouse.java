package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AdapterUpdateWarehouse extends RecyclerView.Adapter<AdapterUpdateWarehouse.ViewHolder> {
    Context context;
    ArrayList<BookingClass> bookingClassList;
    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;
    String username;
    private AppCompatActivity activity;
    FirebaseDatabase db;
    DatabaseReference orderReference;
    FragmentManager fragmentManager;

    public AdapterUpdateWarehouse(Context context, ArrayList<BookingClass> bookingClassList, ArrayList<OrderHistoryClass> orderHistoryClassArrayList,String username,AppCompatActivity activity){
        this.context = context;
        this.bookingClassList = bookingClassList;
        this.orderHistoryClassArrayList = orderHistoryClassArrayList;
        this.activity = activity;
        this.username =username;
    }

    @NonNull
    @Override
    public AdapterUpdateWarehouse.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.update_warehouse_list,parent, false );
        return new AdapterUpdateWarehouse.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterUpdateWarehouse.ViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();

        // Clear any previous data in holder
        holder.status_image.setImageDrawable(null);
        holder.order_status_date.setText("");
        holder.order_status_title.setText("");

        // Check if the position index is within the bounds of both lists
        if (position < bookingClassList.size() ) {
            BookingClass booking = bookingClassList.get(position);
            processBooking(booking, sdf, currentDate, holder);
        }

        if (position < orderHistoryClassArrayList.size()) {
            OrderHistoryClass orderHist = orderHistoryClassArrayList.get(position);
            processOrderHistory(orderHist, sdf, currentDate, holder);
        }

    }
    private void processBooking(BookingClass booking, SimpleDateFormat sdf, Date currentDate, ViewHolder holder) {
        try {
            Date bookingDate = sdf.parse(booking.getDate());
            if (booking.getCollected() == 1) {
                int color = Color.parseColor("#5FDF64");
                holder.status_image.setImageResource(R.drawable.arr_warehouse);
                holder.order_status_date.setText(sdf.format(bookingDate));
                holder.order_status_title.setText(booking.getCategory() + " has arrived at warehouse");
                holder.update_status_button.setBackgroundColor(color);
                holder.update_status_button.setText("Completed");
                holder.update_status_button.setEnabled(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void processOrderHistory(OrderHistoryClass orderHist, SimpleDateFormat sdf, Date currentDate, ViewHolder holder) {
        try {
            int color = Color.parseColor("#A9A9A9");
            holder.update_status_button.setBackgroundColor(color);
            holder.update_status_button.setText("Update");
            holder.update_status_button.setEnabled(true);

            Date orderHistDate = sdf.parse(orderHist.getDate());
            if(orderHist.getOrder_location().equals("China Warehouse")){

                holder.status_image.setImageResource(R.drawable.ready);
                holder.order_status_date.setText(sdf.format(orderHistDate));
                holder.order_status_title.setText("Shipping order " + orderHist.getOrder_number() + " is at China Warehouse");


            }else if(orderHist.getOrder_location().equals("Malaysia Warehouse")) {
                holder.status_image.setImageResource(R.drawable.ready);
                holder.order_status_date.setText(sdf.format(orderHistDate));
                holder.order_status_title.setText("Shipping order " + orderHist.getOrder_number() + " is at Malaysia Warehouse");

            } else if (orderHist.getOrder_location().equals("Shipping Receiver Address")) {
                holder.status_image.setImageResource(R.drawable.ready);
                holder.order_status_date.setText(sdf.format(orderHistDate));
                holder.order_status_title.setText("Shipping order " + orderHist.getOrder_number() + " is Shipping to Receiver Address");

            } else{
                int gcolor = Color.parseColor("#5FDF64");
                holder.update_status_button.setBackgroundColor(gcolor);
                holder.update_status_button.setText("Completed");
                holder.update_status_button.setEnabled(false);
                holder.status_image.setImageResource(R.drawable.ready);
                holder.order_status_date.setText(sdf.format(orderHistDate));
                holder.order_status_title.setText("Shipping order " + orderHist.getOrder_number() + " is " + orderHist.getOrder_status() + " at Receiver Address");

            }
            holder.update_status_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WarehouseSuccessDialog.class);
                    intent.putExtra("ordernumber", orderHist.getOrder_number());
                    intent.putExtra("order_location", orderHist.getOrder_location());
                    intent.putExtra("username", username);
                    context.startActivity(intent);
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        int total_size = bookingClassList.size() + orderHistoryClassArrayList.size();
        return total_size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView status_image;
        TextView order_status_title,order_status_date;
        Button update_status_button,clickedButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            status_image = itemView.findViewById(R.id.status_image);
            order_status_title = itemView.findViewById(R.id.order_status_title);
            order_status_date = itemView.findViewById(R.id.order_status_date);
            update_status_button = itemView.findViewById(R.id.update_status_button);

        }
    }

    private void redirect_updateBooking(){
        Intent updateBookingIntent = new Intent(activity,UpdateBookingAdmin.class);
        /*updateBookingIntent.putExtra("userId", booking.userId);
        updateBookingIntent.putExtra("trackNo", booking.track_number);*/

        activity.startActivity(updateBookingIntent);
    }
}
