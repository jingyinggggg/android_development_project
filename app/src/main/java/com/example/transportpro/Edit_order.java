package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Edit_order extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    EditOrderTabAdapter newOrderTabAdapter;
    Button nextButton, updateButton, deleteButton;
    int currentTab = 0;
    ImageView back_btn;
    FirebaseDatabase db;
    DatabaseReference reference, order_reference, booking_reference;
    LinearLayout edit_btn_container;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_ID = "userId";
    private static final String Order_Number = "orderNumber";

    String category_name, userid, transport_type, sensitive_item, name, mobile, email, state, postcode, add1, add2 ,add3, order_number, formattedDate, order_status;
    double totalweight, price;
    int parcel_quantity;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order);
        setContentView(R.layout.edit_order);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tabLayout = findViewById(R.id.orderTab);
        viewPager2 = findViewById(R.id.view_pager);
        newOrderTabAdapter = new EditOrderTabAdapter(this);
        nextButton = findViewById(R.id.nextButton);
        viewPager2.setAdapter(newOrderTabAdapter);
        back_btn = findViewById(R.id.back_btn);

        edit_btn_container = findViewById(R.id.edit_btn_container);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);


        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);
        String order_number = sharedPreferences.getString(Order_Number,null);
        int user_id = Integer.parseInt(userid);
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("OrderHistory");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(username)){
                    transport_type = snapshot.child(username).child(order_number).child("transport_type").getValue(String.class);
                    sensitive_item = String.valueOf(snapshot.child(username).child(order_number).child("sensitive_item").getValue(int.class));
                    name = snapshot.child(username).child(order_number).child("receiver_name").getValue(String.class);
                    mobile = snapshot.child(username).child(order_number).child("receiver_contact").getValue(String.class);
                    email = snapshot.child(username).child(order_number).child("receiver_email").getValue(String.class);
                    state = snapshot.child(username).child(order_number).child("receiver_state").getValue(String.class);
                    postcode = snapshot.child(username).child(order_number).child("receiver_postcode").getValue(String.class);
                    add1 = snapshot.child(username).child(order_number).child("receiver_add1").getValue(String.class);
                    add2 = snapshot.child(username).child(order_number).child("receiver_add2").getValue(String.class);
                    add3 = snapshot.child(username).child(order_number).child("receiver_add3").getValue(String.class);
                    totalweight = snapshot.child(username).child(order_number).child("weight").getValue(double.class);
                    category_name = snapshot.child(username).child(order_number).child("category").getValue(String.class);
                    parcel_quantity = snapshot.child(username).child(order_number).child("parcel_quantity").getValue(int.class);
                    order_status = snapshot.child(username).child(order_number).child("order_status").getValue(String.class);

                    if(!order_status.equals("Packing")){
                        updateButton.setVisibility(View.GONE);
                        deleteButton.setVisibility(View.GONE);
                    }


                    TextView weight = findViewById(R.id.weight);

                    String format_weight = String.format("%.2f",totalweight);
                    totalweight = Double.parseDouble(format_weight);
                    weight.setText(format_weight + " KG");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SharedViewModel sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        // Observe changes in the selected transport mode
        sharedViewModel.getTransportData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String selectedTransport) {;
                transport_type = selectedTransport;
            }
        });

        sharedViewModel.getSensitiveItem().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sensitive_item = s;
            }
        });

        sharedViewModel.getReceiverData().observe(this, new Observer<ReceiverData>() {
            @Override
            public void onChanged(ReceiverData receiverData) {

                name = receiverData.getName();
                mobile = receiverData.getMobile();
                email = receiverData.getEmail();
                state = receiverData.getState();
                postcode = receiverData.getPostcode();
                add1 = receiverData.getAdd1();
                add2 = receiverData.getAdd2();
                add3 = receiverData.getAdd3();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Edit_order.this, Orderhistory.class);
                startActivity(intent);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTab < newOrderTabAdapter.getItemCount() - 1) {
                    currentTab++;
                    viewPager2.setCurrentItem(currentTab);
                    tabLayout.selectTab(tabLayout.getTabAt(currentTab));
                    if (currentTab == newOrderTabAdapter.getItemCount() - 1) {
                        nextButton.setVisibility(View.GONE);
                        edit_btn_container.setVisibility(View.VISIBLE);
                    }
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    edit_btn_container.setVisibility(View.GONE);
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || postcode.isEmpty() || add1.isEmpty() || add2.isEmpty() || add3.isEmpty()) {
                    Toast.makeText(Edit_order.this, "Please fill in all receiver data fields", Toast.LENGTH_SHORT).show();
                } else if (state == "  - - Select State - - ") {
                    Toast.makeText(Edit_order.this, "Please select your state", Toast.LENGTH_SHORT).show();
                }
                // Check if transport_type is selected
                else if (transport_type == null) {
                    Toast.makeText(Edit_order.this, "Please select a transport type", Toast.LENGTH_SHORT).show();
                }
                // Check if sensitive_item is selected
                else if (sensitive_item == null) {
                    Toast.makeText(Edit_order.this, "Please select whether the item is sensitive", Toast.LENGTH_SHORT).show();
                } else {

                    DatabaseReference orderhistory = FirebaseDatabase.getInstance().getReference("OrderHistory").child(username);

                    String order_status = "Packing";
                    int isPay = 0;
                    int isSensitive = "Yes".equals(sensitive_item) ? 1 : 0;
                    String eastORwest = "";

                    if (state == "Sabah" || state == "Sarawak") {
                        eastORwest = "East";
                    } else {
                        eastORwest = "West";
                    }
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    formattedDate = dateFormat.format(date);

                    double price = calculateShippingCost(transport_type, totalweight, eastORwest, isSensitive);
                    DecimalFormat df = new DecimalFormat("#.##");

                    String formattedPrice = df.format(price);
                    String order_location = "China Warehouse";

                    double roundedPrice = Double.parseDouble(formattedPrice);
                    OrderHistoryClass orderHistoryClass = new OrderHistoryClass(user_id, order_number,order_location, category_name, transport_type, isSensitive, name,
                            mobile, email, state, eastORwest, postcode, add1, add2, add3, order_status, totalweight, formattedDate, parcel_quantity, roundedPrice, isPay);

                    orderhistory.child(order_number).setValue(orderHistoryClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            logEditOrder(username, order_number,"update");
                            Toast.makeText(Edit_order.this, "Edit order " + order_number + " successful", Toast.LENGTH_SHORT).show();
                            Intent Orderhistory = new Intent(Edit_order.this, Orderhistory.class);
                            startActivity(Orderhistory);
                        }
                    });

                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(username).child(order_number).removeValue();

                order_reference = db.getReference("Order").child(username);
                booking_reference = db.getReference("Booking").child(username);

                order_reference.addListenerForSingleValueEvent(new ValueEventListener() {
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

                                    order_reference.child(trackNumber).removeValue();
                                    booking_reference.child(trackNumber).child("isChecked").setValue(0);
                                    booking_reference.child(trackNumber).child("isPackOrder").setValue(0);

                                }
                            }
                        }
                        logEditOrder(username, order_number,"delete");
                        Toast.makeText(Edit_order.this, "Delete order " + order_number + " successful", Toast.LENGTH_SHORT).show();
                        Intent Orderhistory = new Intent(Edit_order.this, Orderhistory.class);
                        startActivity(Orderhistory);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any database error.
                    }
                });
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                viewPager2.setCurrentItem(currentTab);
                if (currentTab == newOrderTabAdapter.getItemCount() - 1) {
                    nextButton.setVisibility(View.GONE);
                    edit_btn_container.setVisibility(View.VISIBLE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    edit_btn_container.setVisibility(View.GONE);
                    nextButton.setText("Next");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Update the selected tab when the user scrolls
                currentTab = position;
                tabLayout.selectTab(tabLayout.getTabAt(position));

                // Update the button text when reaching the last tab
                if (position == newOrderTabAdapter.getItemCount() - 1) {
                    nextButton.setVisibility(View.GONE);
                    edit_btn_container.setVisibility(View.VISIBLE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    edit_btn_container.setVisibility(View.GONE);
                    nextButton.setText("Next");
                }
            }
        });
    }
    private double calculateShippingCost(String transport, double weight, String area, int isSensitive){

        double finalCost = 0;

        //Calculation format is based on the price list that provided in the app
        if(transport != null && weight > 0 && area != null){
            if(transport.equals("Air")){
                if(area.equals("West")){
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 20;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 18) + 100;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 15) + 100 + 90;
                    }
                } else{
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 23;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 21) + 115;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 18) + 115 + 105;
                    }
                }
            } else{
                if(area.equals("West")){
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 12;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 10) + 60;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 7) + 60 + 50;
                    }
                } else{
                    if(weight > 0 && weight <= 5){
                        finalCost = weight * 14;
                    } else if (weight > 5 && weight <= 10) {
                        finalCost = ((weight - 5) * 12) + 70;
                    } else if (weight > 10) {
                        finalCost = ((weight - 10) * 9) + 70 + 60;
                    }
                }
            }
            if(isSensitive == 1){
                finalCost += 25;
            }
        }else{
            finalCost = 0;
        }

        return finalCost;

    }
    public void logEditOrder(String username, String orderId, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("order_id", orderId);
        mFirebaseAnalytics.logEvent("edit_order", bundle);
        bundle.putString("Action", action);
        if(action == "update"){
            mFirebaseAnalytics.logEvent("edit_order", bundle);
        }
        else if(action == "delete"){
            mFirebaseAnalytics.logEvent("delete_order", bundle);
        }
    }
}