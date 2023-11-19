package com.example.transportpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Edit_confirm_fragment extends Fragment {

    FirebaseDatabase db;
    DatabaseReference reference;
    DatabaseReference orderReference;
    SharedPreferences sharedPreferences;
    AdapterSelectOrder adapterSelectOrder;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    private static final String Order_Number = "orderNumber";
    RecyclerView recyclerView;
    String currentUserId, username, order_number;
    ArrayList<BookingClass> bookingClassArrayList;
    ArrayList<String> matchingTrackNumbers; // Initialize this ArrayList
    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(KEY_USERNAME, null);
        order_number = sharedPreferences.getString(Order_Number, null);
        currentUserId = sharedPreferences.getString("userId", null);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("OrderHistory");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)) {
                    final String transport_type = snapshot.child(username).child(order_number).child("transport_type").getValue(String.class);
                    final int is_sensitive = snapshot.child(username).child(order_number).child("sensitive_item").getValue(int.class);

                    final String name = snapshot.child(username).child(order_number).child("receiver_name").getValue(String.class);
                    final String mobile = snapshot.child(username).child(order_number).child("receiver_contact").getValue(String.class);
                    final String email = snapshot.child(username).child(order_number).child("receiver_email").getValue(String.class);
                    final String state_text = snapshot.child(username).child(order_number).child("receiver_state").getValue(String.class);
                    final String postcode_text = snapshot.child(username).child(order_number).child("receiver_postcode").getValue(String.class);
                    final String add1_text = snapshot.child(username).child(order_number).child("receiver_add1").getValue(String.class);
                    final String add2_text = snapshot.child(username).child(order_number).child("receiver_add2").getValue(String.class);
                    final String add3_text = snapshot.child(username).child(order_number).child("receiver_add3").getValue(String.class);

                    TextView transport_method = view.findViewById(R.id.transport_method);
                    transport_method.setVisibility(View.VISIBLE);
                    if (transport_type.equals("Air")) {
                        // Handle the selected transport mode
                        transport_method.setText("Air");

                    } else if (transport_type.equals("Sea")) {
                        transport_method.setText("Sea");
                    }

                    TextView sensitive = view.findViewById(R.id.sensitive);
                    sensitive.setVisibility(View.VISIBLE);
                    if (is_sensitive == 0) {
                        sensitive.setText("No");
                    } else if (is_sensitive == 1) {
                        sensitive.setText("Yes");
                    }

                    TextView nameText = view.findViewById(R.id.confirm_name);
                    TextView mobileText = view.findViewById(R.id.confirm_mobile);
                    TextView emailText = view.findViewById(R.id.confirm_email);
                    TextView state = view.findViewById(R.id.state);
                    TextView postcode = view.findViewById(R.id.confirm_postcode);
                    TextView add1 = view.findViewById(R.id.confirm_add);
                    TextView add2 = view.findViewById(R.id.confirm_add1);
                    TextView add3 = view.findViewById(R.id.confirm_add2);

                    // Set the received data to TextViews
                    nameText.setText(name);
                    mobileText.setText(mobile);
                    emailText.setText(email);
                    state.setText(state_text);
                    postcode.setText(postcode_text);
                    add1.setText(add1_text);
                    add2.setText(add2_text);
                    add3.setText(add3_text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database error.
            }
        });

        recyclerView = view.findViewById(R.id.confirm_container);
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Booking").child(username);
        recyclerView.setHasFixedSize(true);

        if (bookingClassArrayList == null) {
            bookingClassArrayList = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize the adapterSelectOrder here
        adapterSelectOrder = new AdapterSelectOrder(getContext(), bookingClassArrayList, activity, "Edit_confirm_order");
        adapterSelectOrder.setCurrentUserId(currentUserId);
        recyclerView.setAdapter(adapterSelectOrder);

        // Assuming you have a DatabaseReference for your "Booking" table

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingClassArrayList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingClass booking = dataSnapshot.getValue(BookingClass.class);
                    if (booking != null && booking.getIsPackOrder() == 1) {
                        bookingClassArrayList.add(booking);
                    }
                }

                adapterSelectOrder.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Observe changes in the selected transport mode
        sharedViewModel.getTransportData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectedTransport) {
                TextView transport_method = view.findViewById(R.id.transport_method);
                transport_method.setVisibility(View.VISIBLE);
                // Handle the selected transport mode
                transport_method.setText(selectedTransport);
            }
        });

        sharedViewModel.getSensitiveItem().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                TextView sensitive = view.findViewById(R.id.sensitive);
                sensitive.setVisibility(View.VISIBLE);
                sensitive.setText(s);
            }
        });

        sharedViewModel.getReceiverData().observe(getViewLifecycleOwner(), new Observer<ReceiverData>() {
            @Override
            public void onChanged(ReceiverData receiverData) {
                LinearLayout receiver_detail_container = view.findViewById(R.id.receiver_detail_container);
                receiver_detail_container.setVisibility(View.VISIBLE);
                TextView name = view.findViewById(R.id.confirm_name);
                TextView mobile = view.findViewById(R.id.confirm_mobile);
                TextView email = view.findViewById(R.id.confirm_email);
                TextView state = view.findViewById(R.id.state);
                TextView postcode = view.findViewById(R.id.confirm_postcode);
                TextView add1 = view.findViewById(R.id.confirm_add);
                TextView add2 = view.findViewById(R.id.confirm_add1);
                TextView add3 = view.findViewById(R.id.confirm_add2);

                // Set the received data to TextViews
                name.setText(receiverData.getName());
                mobile.setText(receiverData.getMobile());
                email.setText(receiverData.getEmail());
                state.setText(receiverData.getState());
                postcode.setText(receiverData.getPostcode());
                add1.setText(receiverData.getAdd1());
                add2.setText(receiverData.getAdd2());
                add3.setText(receiverData.getAdd3());
            }
        });

        return view;
    }
}
