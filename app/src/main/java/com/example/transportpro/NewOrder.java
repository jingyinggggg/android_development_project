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

public class NewOrder extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    NewOrderTabAdapter newOrderTabAdapter;
    ArrayList<BookingClass> bookingClassArrayList;
    AdapterSelectOrder adapterSelectOrder;
    Button nextButton;
    int currentTab = 0;
    ImageView back_btn;
    TextView total_weight, selectedOrder;
    DatabaseReference database;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    String userid, transport_type, sensitive_item, name, mobile, email, state, postcode, add1, add2 ,add3, order_number, formattedDate;
    double totalweight;
    int count;
    private FirebaseAnalytics mFirebaseAnalytics;
    List<String> category_name = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neworder);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tabLayout = findViewById(R.id.orderTab);
        viewPager2 = findViewById(R.id.view_pager);
        newOrderTabAdapter = new NewOrderTabAdapter(this);
        nextButton = findViewById(R.id.nextButton);
        viewPager2.setAdapter(newOrderTabAdapter);
        back_btn = findViewById(R.id.back_btn);

        total_weight = findViewById(R.id.total_weight);
        selectedOrder = findViewById(R.id.selectedOrder);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        int user_id = Integer.parseInt(userid);

        database = FirebaseDatabase.getInstance().getReference("Booking").child(username);
        bookingClassArrayList = new ArrayList<>();

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

        name = "";
        mobile = "";
        email = "";
        state = "";
        postcode = "";
        add1 = "";
        add2 = "";
        add3 = "";

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

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalWeight = 0;
                boolean anyItemChecked = false;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingClass bookingClass = dataSnapshot.getValue(BookingClass.class);
                    if (bookingClass != null) {
                        if (bookingClass.getIsChecked() == 1 && bookingClass.getIsPackOrder() == 0) {
                            totalWeight += bookingClass.getWeight();
                            anyItemChecked = true;
                        }
                    }
                }

                if (anyItemChecked) {
                    String weightTxt = String.format("%.2f",totalWeight);
                    total_weight.setText(weightTxt + " KG");
                } else {
                    total_weight.setText("0 KG");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewOrder.this, HomePage.class);
                startActivity(intent);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextButton.getText().toString().equals("Confirm")) {
                    // Check if transport_type is selected
                    if (transport_type == null) {
                        Toast.makeText(NewOrder.this, "Please select a transport type", Toast.LENGTH_SHORT).show();
                    }
                    // Check if sensitive_item is selected
                    else if (sensitive_item == null) {
                        Toast.makeText(NewOrder.this, "Please select whether the item is sensitive", Toast.LENGTH_SHORT).show();
                    }
                    // Check if receiver data is properly filled in
                    else if (name.equals("") || mobile.equals("")  || email.equals("")  || postcode.equals("")  || add1.equals("")  || add2.equals("")  || add3.equals("") ) {
                        Toast.makeText(NewOrder.this, "Please fill in all receiver data fields", Toast.LENGTH_SHORT).show();
                    }
                    else if(state == "  - - Select State - - "){
                        Toast.makeText(NewOrder.this, "Please select your state", Toast.LENGTH_SHORT).show();
                    }else {
                        // Query the Firebase Realtime Database to get the snapshot
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean anyItemChecked = false;
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    BookingClass bookingClass = dataSnapshot.getValue(BookingClass.class);
                                    if (bookingClass.getUserId() == user_id) {

                                        int isChecked = dataSnapshot.child("isChecked").getValue(Integer.class);
                                        int isPackOrder = dataSnapshot.child("isPackOrder").getValue(Integer.class);
                                        if (isChecked == 1 && isPackOrder == 0) {
                                            String tracknumber = dataSnapshot.child("track_number").getValue(String.class);
                                            String category = dataSnapshot.child("category").getValue(String.class);
                                            double weight = dataSnapshot.child("weight").getValue(Double.class);

                                            Date date = new Date();
                                            SimpleDateFormat p_number = new SimpleDateFormat("yyyyMMddHHmmss");
                                            p_number.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                                            formattedDate = dateFormat.format(date);

                                            order_number = p_number.format(date) + user_id;

                                            totalweight += weight;

                                            category_name.add(category);

                                            DatabaseReference orderdatabase = FirebaseDatabase.getInstance().getReference("Order").child(username).child(tracknumber);

                                            OrderClass orderClass = new OrderClass(user_id, order_number, formattedDate, tracknumber);
                                            orderdatabase.setValue(orderClass);

                                            anyItemChecked = true;
                                            dataSnapshot.child("isPackOrder").getRef().setValue(1);



                                            count++;
                                        }
                                    }
                                }

                                if (anyItemChecked) {
                                    DatabaseReference orderhistory = FirebaseDatabase.getInstance().getReference("OrderHistory").child(username);

                                    String order_status = "Packing";
                                    int isPay = 0;
                                    int isSensitive = "Yes".equals(sensitive_item) ? 1 : 0;
                                    String delimiter = ", ";

                                    String trackname = String.join(delimiter, category_name);
                                    String eastORwest = "";

                                    if(state == "Sabah" || state == "Sarawak") {
                                        eastORwest = "East";
                                    }
                                    else{
                                        eastORwest = "West";
                                    }
                                    double price = calculateShippingCost(transport_type,totalweight,eastORwest,isSensitive);
                                    DecimalFormat df = new DecimalFormat("#.##");

                                    String formattedPrice = df.format(price);
                                    String order_location = "China Warehouse";

                                    double roundedPrice = Double.parseDouble(formattedPrice);
                                    OrderHistoryClass orderHistoryClass = new OrderHistoryClass(user_id, order_number, order_location, trackname, transport_type, isSensitive, name,
                                            mobile, email, state, eastORwest, postcode, add1 , add2, add3, order_status, totalweight, formattedDate, count, roundedPrice, isPay);

                                    orderhistory.child(order_number).setValue(orderHistoryClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            logSubmitOrder(username, order_number);
                                            Toast.makeText(NewOrder.this, "Order Submitted", Toast.LENGTH_SHORT).show();
                                            Intent home = new Intent(NewOrder.this, HomePage.class);
                                            startActivity(home);
                                        }
                                    });


                                } else {
                                    Toast.makeText(NewOrder.this, "Please check at least one item to order", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle any database error
                            }
                        });
                    }
                }

                // The rest of your onClick logic for tab navigation
                if (currentTab < newOrderTabAdapter.getItemCount() - 1) {
                    currentTab++;
                    viewPager2.setCurrentItem(currentTab);
                    tabLayout.selectTab(tabLayout.getTabAt(currentTab));
                    if (currentTab == newOrderTabAdapter.getItemCount() - 1) {
                        nextButton.setText("Confirm");
                    }


                }
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                viewPager2.setCurrentItem(currentTab);
                if (currentTab == newOrderTabAdapter.getItemCount() - 1) {
                    nextButton.setText("Confirm");
                } else {
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
                    nextButton.setText("Confirm");
                } else {
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
    public void logSubmitOrder(String username, String orderId) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("order_id", orderId);
        mFirebaseAnalytics.logEvent("submit_order", bundle);
    }
}