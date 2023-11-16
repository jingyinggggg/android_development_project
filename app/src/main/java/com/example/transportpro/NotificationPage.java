package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationPage extends AppCompatActivity {

    AdapterNotification adapterNotification;
    ArrayList<NotificationClass> notificationClassArrayList;
    DatabaseReference notificationReference;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";

    RecyclerView notificationRecycle;
    String username, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        userid = sharedPreferences.getString(KEY_ID,null);
        username = sharedPreferences.getString(KEY_USERNAME,null);

        notificationRecycle = findViewById(R.id.notification_list);

        notificationRecycle.setHasFixedSize(true);
        notificationRecycle.setLayoutManager(new LinearLayoutManager(this));

        notificationClassArrayList = new ArrayList<>();
        adapterNotification = new AdapterNotification(this, notificationClassArrayList,username,this);
        notificationRecycle.setAdapter(adapterNotification);

        notificationReference = FirebaseDatabase.getInstance().getReference("Notification");

        notificationReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot notificationSnapshot : categorySnapshot.getChildren()) {
                        int isRead = notificationSnapshot.child("is_read").getValue(int.class);
                        if (isRead == 0) {
                            NotificationClass notification = notificationSnapshot.getValue(NotificationClass.class);
                            if (notification != null) {
                                notificationClassArrayList.add(notification);
                            }
                        }
                    }
                }
                // Here you can notify an adapter if you are using a RecyclerView or ListView to display the notifications
                adapterNotification.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

    }

    public void backHomePage(View view) {

        Intent homepageIntent = new Intent(this, HomePage.class);
        startActivity(homepageIntent);
    }
}