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

public class AdapterOrderRequest extends RecyclerView.Adapter<AdapterOrderRequest.ViewHolder>{
    Context context;
    ArrayList<BookingClass> bookingClassArrayList;
    String username;
    String orderNo;
    private AppCompatActivity activity;

    public AdapterOrderRequest(Context context, ArrayList<BookingClass> bookingClassArrayList, AppCompatActivity activity){
        this.context = context;
        this.bookingClassArrayList = bookingClassArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterOrderRequest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_request_list,parent, false );
        return new AdapterOrderRequest.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderRequest.ViewHolder holder, int position) {
        BookingClass booking = bookingClassArrayList.get(position);

        holder.item_no.setText("Item " + (position + 1));

        String track_number = booking.getTrack_number();
        String category = booking.getCategory();
        int quantity = booking.getQuantity();
        String desc = booking.getDescription();
        double track_weight = booking.getWeight();

        holder.trackNo.setText(track_number);
        holder.track_cat.setText(category);
        holder.track_qty.setText(String.valueOf(quantity));
        holder.track_desc.setText(desc);
        holder.track_weight.setText(String.format("%.2f kg", track_weight));


    }

    @Override
    public int getItemCount() {
        return bookingClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_no, trackNo, track_cat, track_qty, track_desc, track_weight;

        public ViewHolder(@NonNull View ItemView) {

            super(ItemView);

            /*Track Details*/
            item_no   = ItemView.findViewById(R.id.item_no );
            trackNo  = ItemView.findViewById(R.id.trackNo );
            track_cat  = ItemView.findViewById(R.id.track_cat );
            track_qty  = ItemView.findViewById(R.id.track_qty );
            track_desc = ItemView.findViewById(R.id.track_desc );
            track_weight  = ItemView.findViewById(R.id.track_weight );
        }
    }
}
