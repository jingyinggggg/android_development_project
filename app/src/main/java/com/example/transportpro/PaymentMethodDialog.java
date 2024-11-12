package com.example.transportpro;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
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
    // SharedPreferences
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";

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

        priceTextView = view.findViewById(R.id.price);
        TextView payment_method_title = view.findViewById(R.id.payment_method_title);
        TextView order_no = view.findViewById(R.id.order_no);

        String callfrom = getArguments().getString("callfrom");
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString(KEY_ID, null);
        String username = sharedPreferences.getString(KEY_USERNAME, null);

        if (callfrom.equals("Wallet")) {
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
                        String amountText = priceTextView.getText().toString();
                        String substring = amountText.substring(3);

                        int user_id = Integer.parseInt(userid);
                        int amount = Integer.parseInt(substring);

                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference("Wallet");

                        reference.child(username).child("wallet_balance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    int currentBalance = dataSnapshot.getValue(Integer.class);
                                    double newBalance = currentBalance + amount;

                                    try {
                                        String encryptedBalance = EncryptionUtil.encrypt(String.valueOf(newBalance));
                                        reference.child(username).child("wallet_balance").setValue(encryptedBalance)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            updateLowBalanceNotifications(username);
                                                            ((PaymentCompleteListener) requireActivity()).onPaymentComplete();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Failed to update balance.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } catch (Exception e) {
                                        Log.e("EncryptionError", "Failed to encrypt balance", e);
                                    }
                                } else {
                                    WalletClass walletClass = new WalletClass(user_id, amount);
                                    reference.child(username).setValue(walletClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ((PaymentCompleteListener) requireActivity()).onPaymentComplete();
                                            } else {
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
                                String p_text = "+ " + amountText;

                                WalletActivityClass walletActivityClass = new WalletActivityClass(user_id, payment_number, activity, formattedDate, p_text);

                                reference.child(username).child(payment_number).setValue(walletActivityClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("Firebase", "Database error: " + databaseError.getMessage());
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "Please select a payment method.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (callfrom.equals("Payment")) {
            String order_number = getArguments().getString("ordernumber");
            double price = Double.parseDouble(getArguments().getString("payment_price"));

            String price_text = String.format("%.2f", price);
            order_no.setText(order_number);
            priceTextView.setText("RM " + price_text);

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedRadioButton != null) {
                        String amountText = priceTextView.getText().toString();
                        String substring = amountText.substring(3);
                        int user_id = Integer.parseInt(userid);
                        int amount = Integer.parseInt(substring);

                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference("Wallet");

                        reference.child(username).child("wallet_balance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    int currentBalance = dataSnapshot.getValue(Integer.class);
                                    double newBalance = currentBalance + amount;

                                    try {
                                        String encryptedBalance = EncryptionUtil.encrypt(String.valueOf(newBalance));
                                        Log.d("Encryption", "Encrypted balance: " + encryptedBalance);
                                        reference.child(username).child("wallet_balance").setValue(encryptedBalance)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            updateLowBalanceNotifications(username);
                                                            ((PaymentCompleteListener) requireActivity()).onPaymentComplete();
                                                        } else {
                                                            Log.e("Firebase", "Failed to update balance.", task.getException());
                                                        }
                                                    }
                                                });
                                    } catch (Exception e) {
                                        Log.e("EncryptionError", "Failed to encrypt balance", e);
                                    }


                                } else {
                                    WalletClass walletClass = new WalletClass(user_id, amount);
                                    reference.child(username).setValue(walletClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ((PaymentCompleteListener) requireActivity()).onPaymentComplete();
                                            } else {
                                                Toast.makeText(getActivity(), "Failed to create a new entry.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("Firebase", "Database error: " + databaseError.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Please select a payment method.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

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
                payment_method = "Debit / Credit Card";
            }
        });

        return view;
    }

    private void updateLowBalanceNotifications(String username) {
        reference.child(username).child("low_balance").setValue(false);
    }

    public interface PaymentCompleteListener {
        void onPaymentComplete();
    }
}
