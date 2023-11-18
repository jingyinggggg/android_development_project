package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder>{

    Context context;
    ArrayList<NotificationClass> notificationClassArrayList;
    ArrayList<SystemNotification> systemNotificationArrayList;
    private AppCompatActivity activity;
    String username;
    String callFrom;

    public AdapterNotification(Context context, ArrayList<NotificationClass> notificationClassArrayList,ArrayList<SystemNotification> systemNotificationArrayList,String username, AppCompatActivity activity, String callFrom){
        this.context = context;
        this.notificationClassArrayList = (notificationClassArrayList != null) ? notificationClassArrayList : new ArrayList<>();
        this.systemNotificationArrayList = (systemNotificationArrayList != null) ? systemNotificationArrayList : new ArrayList<>();
        this.username = username;
        this.activity = activity;
        this.callFrom = callFrom;
    }
    @NonNull
    @Override
    public AdapterNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_list,parent, false );
        return new AdapterNotification.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.ViewHolder holder, int position) {

        holder.notification_image.setImageResource(R.drawable.track); // Use a placeholder drawable
        holder.notification_title.setText("Loading...");
        holder.notification_content.setText("Please wait...");

        int resId;

        if (callFrom.equals("systemNotification")) {
            if (position < systemNotificationArrayList.size()){
                SystemNotification systemNotification = systemNotificationArrayList.get(position);

                String type = systemNotification.getType();
                String title = systemNotification.getTitle();
                String content = systemNotification.getContent();

                try {
                    resId = Integer.parseInt(systemNotification.getImage());
                } catch (NumberFormatException e) {
                    resId = R.drawable.track; // Default drawable if parsing fails
                }

                Drawable image = ContextCompat.getDrawable(context, resId);
                holder.notification_image.setImageDrawable(image);

                holder.notification_title.setText(title);
                holder.notification_content.setText(content);

                holder.itemView.setOnClickListener(view -> {
                    DatabaseReference systemNotificationRef = FirebaseDatabase.getInstance().getReference("SystemNotification");
                    systemNotificationRef.child(username).child(type).child("is_read").getRef().setValue(1);
                    // Remove the notification from the list
                    systemNotificationArrayList.remove(holder.getAdapterPosition());
                    // Notify the adapter of the item removal
                    notifyItemRemoved(holder.getAdapterPosition());


                });
            }

        }else {
            if (position < notificationClassArrayList.size()){
                NotificationClass notificationClass = notificationClassArrayList.get(position);

                try {
                    resId = Integer.parseInt(notificationClass.getImage());
                } catch (NumberFormatException e) {
                    resId = R.drawable.track; // Default drawable if parsing fails
                }

                Drawable image = ContextCompat.getDrawable(context, resId);
                holder.notification_image.setImageDrawable(image);

                processNotification(notificationClass, holder);
            }

        }


    }

    private void processNotification(NotificationClass notificationClass, AdapterNotification.ViewHolder holder){

        String type = notificationClass.getType();
        String title = notificationClass.getTitle();
        String content = notificationClass.getContent();

        DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference("Booking");
        DatabaseReference orderHistReference = FirebaseDatabase.getInstance().getReference("OrderHistory");
        DatabaseReference walletReference = FirebaseDatabase.getInstance().getReference("Wallet");

        if (title.equals("wallet")) {
            walletReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        double balance = snapshot.child("wallet_balance").getValue(double.class);
                        holder.notification_title.setText("Insufficient Balance");
                        holder.notification_content.setText("Insufficient balance in your wallet! Top up and enjoy our services now!" +
                                "\nYour current wallet balance is: RM" + balance );
                    } else {
                        holder.notification_content.setText("Wallet details not available.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    holder.notification_content.setText("Error loading wallet details.");
                }
            });
        }else if (title.equals("booking") || title.equals("order")){
            DatabaseReference ref = title.equals("booking") ? bookingReference : orderHistReference;

            if (ref.equals(bookingReference)){
                ref.child(username).child(notificationClass.getContent()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String category = snapshot.child("category").getValue(String.class);
                            String message;

                            if (title.equals("booking")) {
                                holder.notification_title.setText("Reminder for "+"( Track No " + notificationClass.getContent() +" )");
                                message ="Your parcel of " + category + " is collected at warehouse ! \nStart ship to Malaysia now !";
                            } else {
                                message = "Error!!";
                            }

                            holder.notification_content.setText(message);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.notification_content.setText("Error loading notification details.");
                    }
                });
            }
            else
            {

                ref.child(username).child(notificationClass.getContent()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String warehouse = snapshot.child("order_location").getValue(String.class);
                            String category = snapshot.child("category").getValue(String.class);
                            String message;

                            if (type.equals("order_packing")) {
                                holder.notification_title.setText("Reminder for "+"( Order No " + notificationClass.getContent() +" )");
                                message = "Your order of " + category + " is Requesting for approval at "+ warehouse +" Please wait for more!";
                            } else if (type.equals("order_delivering")) {
                                holder.notification_title.setText("Reminder for "+"( Order No " + notificationClass.getContent() +" )");
                                message = "Your order of " + category + " is Delivering from warehouse ! \nPlease Wait until it arrives !";
                            }else if(type.equals("order_delivered")) {
                                holder.notification_title.setText("Reminder for "+"( Order No " + notificationClass.getContent() +" )");
                                message = "Your order of " + category + " has been " + warehouse +" ! It has arrived !!";
                            }else {
                                message = "Error!!";
                            }

                            holder.notification_content.setText(message);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.notification_content.setText("Error loading notification details.");
                    }
                });

            }
        }else {
            holder.notification_title.setText(type);
            holder.notification_content.setText(content);
        }

        holder.itemView.setOnClickListener(view -> {
            if (title.equals("wallet")){
                Intent intent = new Intent(context, WalletPage.class).putExtra("from","homepage");
                context.startActivity(intent);
            }else{
                markAsRead(title,content);
                // Remove the notification from the list
                notificationClassArrayList.remove(holder.getAdapterPosition());
                // Notify the adapter of the item removal
                notifyItemRemoved(holder.getAdapterPosition());
            }

        });
    }


    @Override
    public int getItemCount() {
        if (callFrom.equals("systemNotification")) {
            return systemNotificationArrayList.size();
        } else {
            return notificationClassArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification_title, notification_content;
        ImageView notification_image;

        public ViewHolder(@NonNull View ItemView){

            super(ItemView);
            notification_image = itemView.findViewById(R.id.notification_image);
            notification_title = itemView.findViewById(R.id.notification_title);
            notification_content = itemView.findViewById(R.id.notification_content);


        }
    }

    public void markAsRead(String title,String content) {
        DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("Notification");
        notificationReference.child(username).child(title).child(content).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.child("is_read").getValue(Integer.class) == 0){
                    snapshot.child("is_read").getRef().setValue(1)
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    Log.e("Notification", "Failed to mark notification as read.");
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Notification", "Cancelled marking notification as read.", error.toException());
            }
        });

    }

}
