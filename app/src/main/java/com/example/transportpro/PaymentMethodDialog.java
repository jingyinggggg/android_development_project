package com.example.transportpro;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PaymentMethodDialog extends DialogFragment {
    public RadioButton transportPro_wallet;
    public RadioButton tng_wallet;
    public RadioButton onlineBanking;
    public RadioButton debit_credit_card;
    public Button pay;
    private RadioButton selectedRadioButton;
    String payment_method;
    private TextView priceTextView;
    FirebaseDatabase db;
    DatabaseReference reference;
    Context context;

    SharedPreferences sharedPreferences;
    //sharedpreferences
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    private FirebaseAnalytics mFirebaseAnalytics;

    public PaymentMethodDialog(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_method_dialog, container, false);
        super.onCreate(savedInstanceState);

        transportPro_wallet = view.findViewById(R.id.transportPro_wallet);
        tng_wallet = view.findViewById(R.id.tng_wallet);
        onlineBanking = view.findViewById(R.id.onlineBanking);
        debit_credit_card = view.findViewById(R.id.debit_credit_card);

        pay = view.findViewById(R.id.payButton);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        String callfrom = getArguments().getString("callfrom");
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        // Update the TextView
        priceTextView = view.findViewById(R.id.price);
        TextView payment_method_title = view.findViewById(R.id.payment_method_title);
        TextView order_no = view.findViewById(R.id.order_no);




        if(callfrom.equals("Wallet")) {
            String priceText = getArguments().getString("priceText");

            if (priceText != null) {
                priceTextView.setText(priceText);

                payment_method_title.setText("Top Up");
                order_no.setText("");
            }

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedRadioButton != null) {

                        String substring = priceText.substring(3);

                        int user_id = Integer.parseInt(userid);
                        int amount = Integer.parseInt(substring);

                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference("Wallet");

                        reference.child(username).child("wallet_balance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Get the current balance
                                    int currentBalance = dataSnapshot.getValue(Integer.class);

                                    // Calculate the new balance
                                    double newBalance = currentBalance + amount;

                                    // Update the balance in the database
                                    reference.child(username).child("wallet_balance").setValue(newBalance).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                updateLowBalanceNotifications(username);

                                                ((PaymentCompleteListener) requireActivity()).onPaymentComplete();
                                            } else {
                                                // Handle the case where the update was not successful
                                                Toast.makeText(getActivity(), "Failed to update balance.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    WalletClass walletClass = new WalletClass(user_id, amount);
                                    reference.child(username).setValue(walletClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ((PaymentCompleteListener) requireActivity()).onPaymentComplete();
                                            } else {
                                                // Handle the case where creating the new entry was not successful
                                                Toast.makeText(getActivity(), "Failed to create a new entry.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                db = FirebaseDatabase.getInstance();
                                reference = db.getReference("Wallet_Activity");

                                Date date = new Date();
                                SimpleDateFormat p_number = new SimpleDateFormat("yyyyMMddHHmmss");

                                p_number.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                                String payment_number = userid + p_number.format(date);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                                String formattedDate = dateFormat.format(date);

                                String activity = "Top Up";

                                String p_text = "+ " + priceText;

                                WalletActivityClass walletActivityClass = new WalletActivityClass(user_id, payment_number, activity, formattedDate, p_text);

                                reference.child(username).child(payment_number).setValue(walletActivityClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        logWalletTopUp(username, payment_number, activity);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle the error here, if any
                                Log.e("Firebase", "Database error: " + databaseError.getMessage());
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "Please select a payment method.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (callfrom.equals("Payment")) {

            String order_number = getArguments().getString("ordernumber");
            double price = Double.parseDouble(getArguments().getString("payment_price"));

            String price_text = String.format( "%.2f" , price);
            order_no.setText(order_number);
            priceTextView.setText("RM " + String.valueOf(price_text));

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedRadioButton != null) {

                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference("Wallet");

                        reference.child(username).child("wallet_balance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // The "wallet_balance" exists for the user with the given userid
                                    double walletBalance = dataSnapshot.getValue(double.class);

                                    if (walletBalance < price) {
                                        Toast.makeText(getActivity(), "Balance is not enough, Please top up your balance", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        double aftercal = walletBalance - price;
                                        reference.child(username).child("wallet_balance").setValue(aftercal);

                                        DatabaseReference wallet_activity = db.getReference("Wallet_Activity");

                                        Date date = new Date();
                                        SimpleDateFormat p_number = new SimpleDateFormat("yyyyMMddHHmmss");

                                        p_number.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                                        String payment_number = userid + p_number.format(date);

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                                        String formattedDate = dateFormat.format(date);

                                        String activity = "Payment";

                                        int user_id = Integer.parseInt(userid);

                                        String price_txt = "- RM " + price;

                                        WalletActivityClass walletActivityClass = new WalletActivityClass(user_id, payment_number, activity, formattedDate, price_txt);

                                        wallet_activity.child(username).child(payment_number).setValue(walletActivityClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                DatabaseReference payment = db.getReference("Invoice");
                                                String invoice_number = "INV" + order_number;
                                                PaymentClass paymentClass = new PaymentClass(user_id, order_number, invoice_number, formattedDate, payment_method, price, "Successful");

                                                payment.child(username).child(invoice_number).setValue(paymentClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        DatabaseReference order_reference = db.getReference("OrderHistory");

                                                        order_reference.child(username).child(order_number).child("isPay").setValue(1);
                                                        logPayment(username, order_number, invoice_number);

                                                        Toast.makeText(getActivity(), order_number + "Paid Successful", Toast.LENGTH_SHORT).show();

                                                        Intent invoice = new Intent(context, InvoicePage.class);
                                                        invoice.putExtra("invoice", invoice_number);
                                                        invoice.putExtra("order", order_number);
                                                        invoice.putExtra("payment_method", payment_method);
                                                        invoice.putExtra("payment_status", "Successful");
                                                        invoice.putExtra("payment_amount", String.valueOf(price));
                                                        invoice.putExtra("date", formattedDate);


                                                        context.startActivity(invoice);
                                                    }
                                                });


                                            }
                                        });

                                    }


                                }
                                else{
                                    Toast.makeText(getActivity(), "Balance is not enough, Please top up your balance", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "Please select a payment method.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }



        // Set an OnClickListener for each radio button to track the selected radio button
        transportPro_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = transportPro_wallet;
                payment_method = "TransportPro Wallet";
            }
        });

        tng_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = tng_wallet;
                payment_method = "TNG Wallet";
            }
        });

        onlineBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = onlineBanking;
                payment_method = "Online Banking";
            }
        });

        debit_credit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioButton = debit_credit_card;
                payment_method = "Debit/Credit card";
            }
        });


        return view;
    }

    private void updateLowBalanceNotifications(String username) {
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Notification").child(username).child("wallet");

        // Check for low balance notifications and update or remove them
        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                    NotificationClass notification = notificationSnapshot.getValue(NotificationClass.class);
                    if (notification != null && notification.getType().equals("low balance") && notification.getIs_read() == 0) {
                        // Since the balance has been topped up, we can remove the notification
                        notificationSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("updateNotification", "Error updating notification", error.toException());
            }
        });
    }



    public interface PaymentCompleteListener {
        void onPaymentComplete();
    }
    public void logPayment(String username, String orderId, String invoice_number) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("order_id", orderId);
        bundle.putString("invoice_number", invoice_number);
        mFirebaseAnalytics.logEvent("Payment", bundle);
    }
    public void logWalletTopUp(String username, String payment_id, String activity) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("payment_id", payment_id);
        bundle.putString("activity", activity);
        mFirebaseAnalytics.logEvent("Top_up", bundle);
    }
}