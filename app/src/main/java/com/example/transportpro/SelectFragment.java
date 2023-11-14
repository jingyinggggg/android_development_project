package com.example.transportpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BookingClass> bookingClassArrayList;
    AdapterSelectOrder adapterSelectOrder;
    DatabaseReference database;
    CheckBox checkBox;
    LinearLayout linearLayout;
    SharedViewModel sharedViewModel;
    String currentUserId,username;
    private AppCompatActivity activity;
    TextView counter, selectAll;
    private List<BookingClass> selectedItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("localstorage", Context.MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("userId", null);
        username = sharedPreferences.getString("userName", null);
        int user_id = Integer.parseInt(currentUserId);

        database = FirebaseDatabase.getInstance().getReference("Booking").child(username);
        resetUncheckedItems();
        // Get the current user ID from SharedPreferences



        recyclerView = view.findViewById(R.id.recycle_order);
        recyclerView.setHasFixedSize(true);

        bookingClassArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterSelectOrder = new AdapterSelectOrder(getContext(), bookingClassArrayList, activity, "SelectOrder");
        adapterSelectOrder.setCurrentUserId(currentUserId); // Set the currentUserId in the adapter
        recyclerView.setAdapter(adapterSelectOrder);

        counter = view.findViewById(R.id.counter);

        selectAll = view.findViewById(R.id.selectAll);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                int size = 0;
                bookingClassArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingClass bookingClass = dataSnapshot.getValue(BookingClass.class);
                    if (bookingClass.getCollected() == 1 && bookingClass.getIsPackOrder() == 0) {
                        bookingClassArrayList.add(bookingClass);
                        selectAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(int i =0; i<bookingClassArrayList.size(); i ++){
                                    DatabaseReference userReference = FirebaseDatabase.getInstance()
                                            .getReference("Booking").child(username)
                                            .child(bookingClassArrayList.get(i).getTrack_number());
                                    userReference.child("isChecked").setValue(1);


                                }
                                recyclerView.setAdapter(adapterSelectOrder);
                            }
                        });



                        if (bookingClass.getIsChecked() == 1) {
                            count++;
                        }

                    }
                }
                size = bookingClassArrayList.size();
                String str_count = String.valueOf(count);
                counter.setText("Selected "+ str_count + " of " + size +" item");
                adapterSelectOrder.notifyDataSetChanged();

                if(bookingClassArrayList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
    private void resetUncheckedItems() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingClass bookingClass = dataSnapshot.getValue(BookingClass.class);
                    if (bookingClass.getIsChecked() == 1 && bookingClass.getIsPackOrder() == 0) {
                        DatabaseReference userReference = dataSnapshot.getRef().child("isChecked");
                        userReference.setValue(0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database error
            }
        });
    }

}
