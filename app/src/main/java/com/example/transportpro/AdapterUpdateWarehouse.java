package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
        holder.from_warehouse.setText("");

        // Check if the position index is within the bounds of both lists
        if (position < bookingClassList.size() ) {
            BookingClass booking = bookingClassList.get(position);
            processBooking(booking, sdf, currentDate, holder);
        }

        if (position < orderHistoryClassArrayList.size()) {
            OrderHistoryClass orderHist = orderHistoryClassArrayList.get(position);
            processOrderHistory(orderHist, sdf, currentDate, holder);
        }

        holder.update_status_button.setOnClickListener(view -> {
            showConfirmDialog(view, holder.update_status_button);
        });

    }
    private void processBooking(BookingClass booking, SimpleDateFormat sdf, Date currentDate, ViewHolder holder) {
        try {
            Date bookingDate = sdf.parse(booking.getDate());
            long diff = currentDate.getTime() - bookingDate.getTime();
            if (TimeUnit.MILLISECONDS.toDays(diff) > 1 && booking.getCollected() == 1) {
                holder.status_image.setImageResource(R.drawable.arr_warehouse);
                holder.order_status_date.setText(sdf.format(bookingDate));
                holder.order_status_title.setText(booking.getCategory() + " has arrived at warehouse");
                holder.from_warehouse.setText("From - China Warehouse");
            } else {
                holder.status_image.setImageResource(R.drawable.order_place);
                holder.order_status_date.setText(sdf.format(bookingDate));
                holder.order_status_title.setText("Booking for " + booking.getCategory() + " has been placed");
                holder.from_warehouse.setText("From - China Warehouse");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void processOrderHistory(OrderHistoryClass orderHist, SimpleDateFormat sdf, Date currentDate, ViewHolder holder) {
        try {
            Date orderHistDate = sdf.parse(orderHist.getDate());
            long diff = currentDate.getTime() - orderHistDate.getTime();
            if(orderHist.getOrder_status().equals("Packing") || orderHist.getOrder_status().equals("Delivering")){
                if (orderHist.getOrder_status().equals("Packing")){
                    holder.status_image.setImageResource(R.drawable.ready);
                    holder.order_status_date.setText(sdf.format(orderHistDate));
                    holder.order_status_title.setText("Shipping order " + orderHist.getOrder_number() + " has been placed");
                    holder.from_warehouse.setText("From - China Warehouse");
                }
                else if (orderHist.getOrder_status().equals("Delivering")){

                }
                if (TimeUnit.MILLISECONDS.toDays(diff) > 1) {
                    holder.status_image.setImageResource(R.drawable.ready);
                    holder.order_status_date.setText(sdf.format(orderHistDate));
                    holder.order_status_title.setText("Shipping order " + orderHist.getOrder_number() + orderHist.getCategory() +" is " + orderHist.getOrder_status());
                    holder.from_warehouse.setText("From - China Warehouse");
                }

            }else {
                holder.status_image.setImageResource(R.drawable.delivered);
                holder.order_status_date.setText(sdf.format(orderHistDate));
                holder.order_status_title.setText("Shipping order " + orderHist.getOrder_number() + " is " + orderHist.getOrder_status());
                holder.from_warehouse.setText("From - China Warehouse");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(bookingClassList.size(), orderHistoryClassArrayList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView status_image;
        TextView from_warehouse,order_status_title,order_status_date;
        Button update_status_button,clickedButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            status_image = itemView.findViewById(R.id.status_image);
            from_warehouse = itemView.findViewById(R.id.from_warehouse);
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

    public void showConfirmDialog(View v,Button clickedButton){
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.success_dialog_admin, null);

        // Find views in the custom layout
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);

        // Find the confirm_dialog button within the dialog view
        Button confirmDialogButton = dialogView.findViewById(R.id.confirm_dialog);
        TextView cancelDialogText = dialogView.findViewById(R.id.cancel_dialog);
        // Set an OnClickListener for the confirm_dialog button

        confirmDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add code for updating the status

                redirect_updateBooking();
                // Dismiss the dialog
                AlertDialog dialog = (AlertDialog) view.getTag();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                // After update, change button background and disable it
                if (clickedButton != null) {
                    int color = Color.parseColor("#5FDF64");
                    clickedButton.setBackgroundColor(color); // Replace with your desired background resource
                    clickedButton.setEnabled(false);
                    clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done_icon, 0, 0, 0);
                }
            }
        });
        cancelDialogText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = (AlertDialog) confirmDialogButton.getTag();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        // Create the custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);


        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Attach the dialog to the confirm_dialog button to dismiss it later
        confirmDialogButton.setTag(dialog);
    }
}
