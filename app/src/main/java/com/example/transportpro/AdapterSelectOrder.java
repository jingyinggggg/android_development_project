package com.example.transportpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
import java.util.List;

public class AdapterSelectOrder extends RecyclerView.Adapter<AdapterSelectOrder.MyViewHolder> {
    Context context;
    ArrayList<BookingClass> bookingClassArrayList;
    String currentUserId; // The current user ID
    private AppCompatActivity activity;
    String callFrom;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    private static final String Order_Number = "orderNumber";
    FirebaseDatabase db;
    DatabaseReference orderReference;
    String order_number;




    public AdapterSelectOrder(Context context, ArrayList<BookingClass> bookingClassArrayList, AppCompatActivity activity, String callFrom){
        this.context = context;
        this.bookingClassArrayList = bookingClassArrayList;
        this.activity = activity;
        this.callFrom = callFrom;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public AdapterSelectOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.select_orderlist, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelectOrder.MyViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        BookingClass bookingClass = bookingClassArrayList.get(position);

        if(currentUserId!= null && currentUserId.equals(String.valueOf(bookingClass.getUserId()))){
            int collected = 1;

            if(callFrom == "ConfirmOrder" || callFrom == "SelectOrder") {
                if (callFrom == "ConfirmOrder" && bookingClass.getIsChecked() == 1) {
                    holder.checkBox.setVisibility(View.GONE);
                } else if (callFrom == "ConfirmOrder" && bookingClass.getIsChecked() == 0) {
                    holder.background.setVisibility(View.GONE);
                } else {
                    holder.checkBox.setVisibility(View.VISIBLE);
                }
                if (bookingClass.getCollected() == collected && bookingClass.getIsPackOrder() == 0) {
                    holder.parcel_title.setText(bookingClass.getCategory());
                    holder.tracknumber.setText(bookingClass.getTrack_number());
                    holder.quantity.setText(String.valueOf(bookingClass.getQuantity()));
                    holder.weight.setText(String.valueOf(bookingClass.getWeight()) + "KG");

                    String trackNumber = bookingClass.getTrack_number().toString();
                    DatabaseReference userReference = FirebaseDatabase.getInstance()
                            .getReference("Booking").child(username)
                            .child(trackNumber);
                    if (bookingClass.getIsChecked() == 1) {
                        holder.background.setBackgroundResource(R.drawable.order_selected);
                        holder.checkBox.setChecked(true);
                    } else {
                        holder.background.setBackgroundResource(R.drawable.order_rectangle);
                        holder.checkBox.setChecked(false);
                    }

                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            int newIsCheckedValue = isChecked ? 1 : 0;
                            userReference.child("isChecked").setValue(newIsCheckedValue);
                        }
                    });
                }
                else {
                    holder.background.setVisibility(View.GONE);
                }
            }
            else if(callFrom == "Edit_confirm_order"){
                List<String> matchingTrackNumbers = new ArrayList<>();
                sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                order_number = sharedPreferences.getString(Order_Number, null);
                db = FirebaseDatabase.getInstance();
                orderReference = db.getReference("Order").child(username);

                orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot trackSnapshot : snapshot.getChildren()) {
                            DataSnapshot orderNumberSnapshot = trackSnapshot.child("order_number");
                            if (orderNumberSnapshot.exists()) {
                                // Check if the order_number matches the specific target
                                String orderNumber = orderNumberSnapshot.getValue(String.class);
                                if (orderNumber.equals(order_number)) {
                                    // This track_number has a matching order_number
                                    String trackNumber = trackSnapshot.getKey(); // Get the track_number
                                    matchingTrackNumbers.add(trackNumber);

                                }
                            }
                        }

                        if(matchingTrackNumbers.contains(bookingClass.getTrack_number())) {
                            holder.background.setBackgroundResource(R.drawable.order_selected);
                            holder.parcel_title.setText(bookingClass.getCategory());
                            holder.tracknumber.setText(bookingClass.getTrack_number());
                            holder.quantity.setText(String.valueOf(bookingClass.getQuantity()));
                            holder.weight.setText(String.valueOf(bookingClass.getWeight()) + "KG");
                            holder.checkBox.setVisibility(View.GONE);
                        }
                        else{
                            holder.background.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any database error.
                    }
                });



            }
        }
        else{
            holder.background.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return bookingClassArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout background;
        TextView parcel_title, tracknumber, quantity, weight;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            background = itemView.findViewById(R.id.order_itemDesc);
            parcel_title = itemView.findViewById(R.id.parcel_title);
            tracknumber = itemView.findViewById(R.id.tracknumber);
            quantity = itemView.findViewById(R.id.quantity);
            weight = itemView.findViewById(R.id.weight);
            checkBox = itemView.findViewById(R.id.checkbox);

        }

    }
}
