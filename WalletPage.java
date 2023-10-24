package com.example.transportpro;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.transportpro.databinding.ActivityWalletPageBinding;
import com.example.transportpro.databinding.HomepageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class WalletPage extends AppCompatActivity implements PaymentMethodDialog.PaymentCompleteListener {
    Context context;
    RecyclerView recyclerView;
    AdapterWallet adapterWallet;
    ArrayList<WalletActivityClass> walletActivityClassArrayList;
    ImageButton back;
    FirebaseDatabase db;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    private AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_page);

        recyclerView = findViewById(R.id.recent_activity);
        recyclerView.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Wallet");

        reference.child(username).child("wallet_balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The "wallet_balance" exists for the user with the given userid
                    double walletBalance = dataSnapshot.getValue(double.class);
                    TextView wallet_balance = findViewById(R.id.wallet_balance);

                    String balance = String.format("%.2f", walletBalance);
                    wallet_balance.setText("RM " + balance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottombar);

        bottomNavigationView.setSelectedItemId(R.id.bar_wallet);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bar_home){
                Intent intent = new Intent(WalletPage.this,HomePage.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_payment) {
                Intent intent = new Intent(WalletPage.this,Payment.class).putExtra("from","wallet");
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_profile) {
                Intent intent = new Intent(WalletPage.this,AccountPage.class).putExtra("from","wallet");
                startActivity(intent);
            }
            return true;
        });

        //Recent Activity

            reference = FirebaseDatabase.getInstance().getReference("Wallet_Activity");

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);

        walletActivityClassArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterWallet = new AdapterWallet(this, walletActivityClassArrayList, activity);
        adapterWallet.setCurrentUserId(userid); // Set the currentUserId in the adapter
        recyclerView.setAdapter(adapterWallet);

        reference.child(username)
                .orderByKey()
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                walletActivityClassArrayList.clear();
                ArrayList<WalletActivityClass> reversed = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    WalletActivityClass walletActivityClass = dataSnapshot.getValue(WalletActivityClass.class);
                    reversed.add(walletActivityClass);
                }

                Collections.reverse(reversed);
                walletActivityClassArrayList.addAll(reversed);
                adapterWallet.notifyDataSetChanged();

                reference = db.getReference("Wallet");

                reference.child(username).child("wallet_balance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The "wallet_balance" exists for the user with the given userid
                            double walletBalance = dataSnapshot.getValue(double.class);
                            TextView wallet_balance = findViewById(R.id.wallet_balance);

                            String balance = String.format("%.2f", walletBalance);
                            wallet_balance.setText("RM " + balance);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void backPreviousActivity(View view){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottombar);

        if(getIntent().getStringExtra("from").equals("homepage")){
            bottomNavigationView.setSelectedItemId(R.id.bar_home);
        }
        else if(getIntent().getStringExtra("from").equals("payment")){
            bottomNavigationView.setSelectedItemId(R.id.bar_payment);
        }
        else if(getIntent().getStringExtra("from").equals("profile")){
            bottomNavigationView.setSelectedItemId(R.id.bar_profile);
        }

        finish();
    }

    public void showSelectPaymentDialog(View view){PaymentMethodDialog dialogFragment = new PaymentMethodDialog(context);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        args.putString("callfrom","Wallet");
        if (view.getId() == R.id.five){
            args.putString("priceText", "RM 5");
        }
        else if (view.getId() == R.id.ten) {
            args.putString("priceText", "RM 10");
        }
        else if (view.getId() == R.id.twenty) {
            args.putString("priceText", "RM 20");
        }
        else if (view.getId() == R.id.fifty) {
            args.putString("priceText", "RM 50");
        }
        else if (view.getId() == R.id.hundred) {
            args.putString("priceText", "RM 100");
        }
        else if (view.getId() == R.id.two_hundred) {
            args.putString("priceText", "RM 200");
        }
        dialogFragment.setArguments(args);

        dialogFragment.show(ft, "PaymentMethodDialogFragment");
    }
    public void onPaymentComplete() {
        // Refresh the dialog or perform any other actions here
        // For example, dismiss the dialog
        dismissDialogFragment();

    }

    private void dismissDialogFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PaymentMethodDialog dialogFragment = (PaymentMethodDialog) fragmentManager.findFragmentByTag("PaymentMethodDialogFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

}