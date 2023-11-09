package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.ViewHolder> {

    Context context;
    ArrayList<BookingClass> bookingClassList;

    private AppCompatActivity activity;

    public AdapterBooking(Context context, ArrayList<BookingClass> bookingClassList, AppCompatActivity activity){
        this.context = context;
        this.bookingClassList = bookingClassList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customer_booking_list,parent, false );
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BookingClass booking = bookingClassList.get(position);
        int id = booking.getUserId();
        holder.customerId.setText("Customer ID: " + id);
        holder.viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { redirect_updateBooking(booking); }
        });

    }

    @Override
    public int getItemCount() {
        return bookingClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerId;
        Button viewDetailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customerId = itemView.findViewById(R.id.customerId);
            viewDetailsButton = itemView.findViewById(R.id.view_details);
        }
    }

    private void redirect_updateBooking(BookingClass booking){
        Intent updateBookingIntent = new Intent(activity,UpdateBookingAdmin.class);
        updateBookingIntent.putExtra("userId", booking.userId);
        updateBookingIntent.putExtra("trackNo", booking.track_number);

        activity.startActivity(updateBookingIntent);
    }
}
