package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UpdateWarehouseAdmin extends AppCompatActivity {

    private LinearLayout orderContainer;
    ArrayList<BookingClass> bookingClassArrayList;
    ArrayList<OrderClass> orderClassArrayList;
    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;

    int userId;
    String username;

    private DatabaseReference usersRef;
    private DatabaseReference orderHistRef;
    private DatabaseReference orderRef;
    private DatabaseReference bookingRef;

    RecyclerView updateWarehouseList;
    AdapterUpdateWarehouse adapterUpdateWarehouse;

    ImageButton backArrow;
    Button confirm_dialog;
    TextView cancel_dialog,customerId,customerName,ItemName,ItemNumber,order_status,TotalItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_warehouse);

        orderContainer = findViewById(R.id.orderContainer);

        Intent from_order = getIntent();
        if (from_order != null)
        {
            Bundle extras = from_order.getExtras();
            if (extras != null)
            {
                userId = extras.getInt("userId", 0);
                username = extras.getString("username");
            }
        }
        customerId = findViewById(R.id.customerId);
        customerName = findViewById(R.id.customerName);

        customerId.setText("Customer Id : " + userId);
        customerName.setText("Customer name : "+username);

        updateWarehouseList = findViewById(R.id.updateWarehouseList);

        updateWarehouseList.setHasFixedSize(true);
        updateWarehouseList.setLayoutManager(new LinearLayoutManager(this));

        bookingClassArrayList = new ArrayList<>();
        orderHistoryClassArrayList = new ArrayList<>();
        adapterUpdateWarehouse = new AdapterUpdateWarehouse(this, bookingClassArrayList,orderHistoryClassArrayList,username,this);
        updateWarehouseList.setAdapter(adapterUpdateWarehouse);

        usersRef = FirebaseDatabase.getInstance().getReference("User");
        orderHistRef = FirebaseDatabase.getInstance().getReference("OrderHistory");
        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        bookingRef = FirebaseDatabase.getInstance().getReference("Booking");


        bookingRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bookingDataSnapshot : snapshot.getChildren()) {
                    BookingClass booking = bookingDataSnapshot.getValue(BookingClass.class);
                    if (booking != null && booking.getCollected() == 0) {
                        bookingClassArrayList.add(booking);
                    }
                }
                adapterUpdateWarehouse.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderHistRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderHistSnapshot : snapshot.getChildren()){
                    OrderHistoryClass orderHistoryClass = orderHistSnapshot.getValue(OrderHistoryClass.class);
                    if (orderHistoryClass != null ){
                        addOrderView(orderHistoryClass);
                        orderHistoryClassArrayList.add(orderHistoryClass);
                    }
                }
                adapterUpdateWarehouse.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Find the TextView and Image Button in the header layout
        backArrow = findViewById(R.id.backArrow);
        /*Header Button function*/
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_warehouse(view);
            }
        });


    }

    private void addOrderView(OrderHistoryClass order) {
        // Inflate or create a view for the order
        View orderView = LayoutInflater.from(this).inflate(R.layout.order_item_layout, null, false);

        // Set the details of the order to the view
        TextView ItemName = orderView.findViewById(R.id.orderItems);
        TextView ItemNumber = orderView.findViewById(R.id.order_number);
        TextView order_status = orderView.findViewById(R.id.order_status);
        TextView TotalItems = orderView.findViewById(R.id.parcelQty);

            ItemNumber.setText("Order No : " + order.getOrder_number());
            ItemName.setText("Item Name : " + order.getCategory());
            order_status.setText("Order Status : " + order.getOrder_status());
            TotalItems.setText(String.valueOf(order.getParcel_quantity()));



        // Add the view to the container
        orderContainer.addView(orderView);
    }


    public void redirect_warehouse(View v){
        Intent warehouseIntent = new Intent(UpdateWarehouseAdmin.this, WarehouseAdmin.class);
        startActivity(warehouseIntent);
    }

    public void return_page(View v){
        Intent backIntent = new Intent(this, UpdateWarehouseAdmin.class);
        startActivity(backIntent);
    }
}