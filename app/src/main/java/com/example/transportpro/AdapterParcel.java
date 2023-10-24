package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterParcel extends RecyclerView.Adapter<AdapterParcel.MyViewHolder> {

    Context context;
    ArrayList<BookingClass> bookingClassArrayList;
    String currentUserId; // The current user ID
    private AppCompatActivity activity;
    String parcel;
    public AdapterParcel(Context context, ArrayList<BookingClass> bookingClassArrayList, AppCompatActivity activity, String parcel) {
        this.activity = activity;
        this.context = context;
        this.bookingClassArrayList = bookingClassArrayList;
        this.parcel = parcel;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public AdapterParcel.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.booking_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterParcel.MyViewHolder holder, int position) {
        BookingClass bookingClass = bookingClassArrayList.get(position);
        int collected = 1;
        // Check if the item belongs to the current user
        if (currentUserId != null && currentUserId.equals(String.valueOf(bookingClass.getUserId()))) {
            // Check the value of the 'parcel' variable and display items accordingly
            if (parcel.equals("A") || (parcel.equals("R") && bookingClass.getCollected() == 0) || (parcel.equals("C") && bookingClass.getCollected() == 1)) {
                holder.category.setText(bookingClass.getCategory());
                holder.quantity.setText(String.valueOf(bookingClass.getQuantity()));
                holder.tracknumber.setText(bookingClass.getTrack_number());
                if (bookingClass.getCollected() == collected) {
                    holder.collected.setText("Collected");
                    holder.collected.setTextColor(ContextCompat.getColor(context, R.color.lightGreen));
                    holder.background.setBackgroundResource(R.drawable.delivered_rec);
                    holder.status.setImageResource(R.drawable.collected);
                    holder.redirect.setImageResource(R.drawable.arrow_circle_right_collected);
                } else {
                    holder.collected.setText("Requsting");
                }

                holder.background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ParcelEditPage.class);
                        intent.putExtra("track_number", bookingClass.getTrack_number());
                        intent.putExtra("category", bookingClass.getCategory());
                        intent.putExtra("delivery_by", bookingClass.getDelivery_by());
                        intent.putExtra("quantity", String.valueOf(bookingClass.getQuantity()));
                        intent.putExtra("description", bookingClass.getDescription());

                        if (bookingClass.getCollected() == collected) {
                            intent.putExtra("collected", "1");
                        }
                        context.startActivity(intent);
                    }
                });

                holder.date.setText(bookingClass.getDate());
            } else {
                // Handle items that don't belong to the current user (you can hide or display them accordingly)
                holder.background.setVisibility(View.GONE);
                // Or set specific text, e.g., holder.category.setText("Not your item");
            }
        }else {
            // Handle items that don't belong to the current user (you can hide or display them accordingly)
            holder.background.setVisibility(View.GONE);
            // Or set specific text, e.g., holder.category.setText("Not your item");
        }
    }

    @Override
    public int getItemCount() {
        return bookingClassArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView category, quantity, tracknumber, collected, date;
        RelativeLayout background;
        ImageView status;
        ImageButton redirect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            redirect = itemView.findViewById(R.id.editButton);
            background = itemView.findViewById(R.id.parcel1);
            status = itemView.findViewById(R.id.requestIcon);
            category = itemView.findViewById(R.id.productName1);
            quantity = itemView.findViewById(R.id.productQuantity);
            tracknumber = itemView.findViewById(R.id.trackingNumber1);
            collected = itemView.findViewById(R.id.request);
            date = itemView.findViewById(R.id.date1);
        }
    }
}
