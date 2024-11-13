package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderRequestAdmin extends AppCompatActivity {

    // Binding views
    private TextView customer_name,customer_Id, customer_phoneNo, customer_email, customer_postCode, customer_add;
    private TextView order_trans, total_items,total_weight;
    private Button accept_order, decline_order;
    ImageButton header_button;

    RecyclerView order_requestRecycle;
    AdapterOrderRequest adapterOrderRequest;

    ArrayList<BookingClass> bookingClassArrayList;
    ArrayList<OrderClass> orderClassArrayList;

    private DatabaseReference usersReference;
    private DatabaseReference orderHistReference;
    private DatabaseReference orderReference;
    private DatabaseReference bookingReference;

    private String username;
    private String orderNo;
    int userId;
    int parcel_qty;
    double tWeight;
    String fullName, contact, email, postCode, add1, add2, add3, transport_type,category,track_number;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        header_button = findViewById(R.id.backArrow);

        bindViews();


        Intent from_order = getIntent();
        if (from_order != null)
        {
            Bundle extras = from_order.getExtras();
            if (extras != null)
            {
                userId = extras.getInt("userId", 0);
                orderNo = extras.getString("order_number");
                parcel_qty = extras.getInt("parcel_qty");
            }
        }

        order_requestRecycle = findViewById(R.id.order_request_list);

        order_requestRecycle.setHasFixedSize(true);
        order_requestRecycle.setLayoutManager(new LinearLayoutManager(this));

        bookingClassArrayList = new ArrayList<>();
        adapterOrderRequest = new AdapterOrderRequest(this, bookingClassArrayList,this);
        order_requestRecycle.setAdapter(adapterOrderRequest);

        usersReference = FirebaseDatabase.getInstance().getReference("User");
        bookingReference = FirebaseDatabase.getInstance().getReference("Booking");
        orderReference = FirebaseDatabase.getInstance().getReference("Order");
        orderHistReference= FirebaseDatabase.getInstance().getReference("OrderHistory");

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.userId == userId) {
                            // Update UI with user details
                            username = user.getUsername();
                            updateCustomerDetailsUI(user);

                            getOrderHistoryAndOrder(username, orderNo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });




        /*Header Button Function*/
        header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_order(view);
            }
        });

        accept_order = findViewById(R.id.accept_order);
        decline_order = findViewById(R.id.decline_order);

        /*accept order button function */
        accept_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //accept order function here

                orderHistReference.child(username).child(orderNo).child("order_status").setValue("Delivering");

                orderHistReference.child(username).child(orderNo).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot orderHistorySnapshot) {
                        if (orderHistorySnapshot.exists()) {
                            OrderHistoryClass orderHist = orderHistorySnapshot.getValue(OrderHistoryClass.class);
                            String notification_type;
                            if (orderHist != null) {
                                if (orderHist.getOrder_status().equals("Delivering")){
                                    notification_type = "order_delivering";
                                    logUpdateOrder(username, "accept");
                                    setNotification(orderHist,username,notification_type);
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here
                    }
                });


                redirect_order(view);
            }
        });

        /*decline order button function*/
        decline_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create an EditText for the user to enter description
                final EditText input = new EditText(OrderRequestAdmin.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderRequestAdmin.this);
                builder.setView(input); // Add the EditText to the dialog
                builder.setTitle("Enter Description (10 words max)");
                builder.setMessage("Please enter the reason for declining the order: ");

                // Set up the buttons
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = input.getText().toString().trim(); // Get user input
                        // Proceed with the rest of the logic only if description is not empty
                        String[] words = description.split("\\s+");
                        if (description.isEmpty() || words.length >= 10) {
                            Toast.makeText(OrderRequestAdmin.this, "Please enter a valid description with the length of 10 words.", Toast.LENGTH_SHORT).show();
                            return;// Stop further execution
                        } else {
                            declineOrder(description,orderNo,username,userId); // Call the decline order function
                            redirect_order(view);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void declineOrder(String description, String orderNo, String username, int userId) {

        if (username != null) {
            String title = "order";
            String type = description;
            String content = orderNo;
            String imageResId = String.valueOf(R.drawable.decline_order);

            int is_read = 0;
            DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("Notification").child(username).child(title).child(content);

            NotificationClass notificationClass = new NotificationClass(userId, imageResId, title, type, content, is_read);
            notificationReference.setValue(notificationClass);
        }

        ArrayList<String> track_number_list = new ArrayList<>();

        orderReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logUpdateOrder(username, "decline");
                track_number_list.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()){
                    String currentOrderNo = orderSnapshot.child("order_number").getValue(String.class);
                    if (currentOrderNo.equals(orderNo)){
                        String trackNo = orderSnapshot.child("track_number").getValue(String.class);
                        track_number_list.add(trackNo);
                        for (String currentTrackNo : track_number_list){
                            bookingReference.child(username).child(currentTrackNo)
                                    .child("isPackOrder")
                                    .getRef()
                                    .setValue(0);
                            orderReference.child(username).child(currentTrackNo).getRef().removeValue();
                        }
                        orderHistReference.child(username).child(orderNo).getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderRequestAdmin.this, "Error for decline order", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateCustomerDetailsUI(User user) {
        /*Customer Details*/
        customer_Id.setText("Customer ID : " + user.getUserId());
        customer_name.setText(user.getFullname());
        customer_phoneNo.setText(user.getContact());
        customer_email.setText(user.getEmail());
    }

    private void setNotification(OrderHistoryClass orderHist, String username,String notification_type){

        if (username!=null) {
            String title = "order";
            String type = notification_type;
            String content = orderHist.getOrder_number();
            String imageResId = String.valueOf(R.drawable.order_ship);

            if (orderHist.getTransport_type().equals("sea")) {
                imageResId = String.valueOf(R.drawable.order_ship);
            } else if (orderHist.getTransport_type().equals("air")) {
                imageResId = String.valueOf(R.drawable.order_air);
            }

            int is_read = 0;

            DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("Notification").child(username).child(title).child(content);

            NotificationClass notificationClass = new NotificationClass(orderHist.getUserId(), imageResId, title, type, content, is_read);
            notificationReference.setValue(notificationClass);
        }

    }

    private void getOrderHistoryAndOrder(String username, String orderNo) {
        orderHistReference.child(username).child(orderNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot orderHistorySnapshot) {
                if (orderHistorySnapshot.exists()) {
                    OrderHistoryClass orderHist = orderHistorySnapshot.getValue(OrderHistoryClass.class);
                    String notification_type;
                    if (orderHist != null) {
                        updateOrderHistoryUI(orderHist);
                    }
                    getOrder(username,orderNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });

    }

    private void updateOrderHistoryUI(OrderHistoryClass orderHistory) {

        customer_postCode.setText(orderHistory.getReceiver_postcode());
        customer_add.setText(orderHistory.getReceiver_add1()+" "+orderHistory.getReceiver_add2()+" "+orderHistory.getReceiver_add3());

        /*Order Transport Type*/
        order_trans.setText(orderHistory.getTransport_type());

        /*Total Track Items*/
        total_items.setText(String.valueOf(orderHistory.getParcel_quantity()));

        if (orderHistory != null && orderHistory.getWeight() > 0) {
            total_weight.setText(String.format("%.2f kg", orderHistory.getWeight()));
        } else {
            total_weight.setText("N/A");
        }

    }

    private void getOrder(String username, String orderNo){
        orderReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String currOrderNo = dataSnapshot.child("order_number").getValue(String.class);
                    if ((currOrderNo).equals(orderNo)){
                        OrderClass order = dataSnapshot.getValue(OrderClass.class);
                        track_number = order.getTrack_number();
                        getBookingsForOrder(username, track_number);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getBookingsForOrder(String username, String trackNumber) {
        bookingReference.child(username).child(trackNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                if (bookingSnapshot.exists()) {
                    BookingClass booking = bookingSnapshot.getValue(BookingClass.class);
                    if (booking != null) {
                        bookingClassArrayList.add(booking);
                    }
                    adapterOrderRequest.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    private void bindViews() {

        /*Customer Details*/
        customer_Id = findViewById(R.id.customerId );
        customer_name = findViewById(R.id.customer_name);
        customer_phoneNo  = findViewById(R.id.customer_phoneNo );
        customer_email  = findViewById(R.id.customer_email );
        customer_postCode  = findViewById(R.id.customer_postCode );
        customer_add  = findViewById(R.id.customer_add );

        /*Order Transport Type*/
        order_trans = findViewById(R.id.order_trans );

        /*Total Track Items*/
        total_items  = findViewById(R.id.total_items );


        total_weight = findViewById(R.id.total_weight );

        customer_Id.setText("Customer ID : Loading..");
        customer_name.setText("Loading Customer Details..");
        customer_phoneNo.setText("Loading..");
        customer_email.setText("Loading..");

        customer_postCode.setText("Loading Addresses..");
        customer_add.setText("Loading..");

        /*Total Track Items*/
        total_weight.setText("N/A");
    }

    public void redirect_order(View v){
        Intent orderIntent = new Intent(OrderRequestAdmin.this, OrderAdmin.class);
        startActivity(orderIntent);
    }

    public void logUpdateOrder(String username, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        if (action == "accept"){
            mFirebaseAnalytics.logEvent("accept_order", bundle);
        }
        else if (action == "decline"){
            mFirebaseAnalytics.logEvent("decline_order", bundle);
        }
    }
}