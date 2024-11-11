package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterPayment extends RecyclerView.Adapter<AdapterPayment.MyViewHolder> {
    Context context;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    ArrayList<OrderHistoryClass> orderHistoryClassArrayList;
    String callfrom;
    FragmentManager fragmentManager;

    Bundle args = new Bundle();

    public AdapterPayment(Context context, ArrayList<OrderHistoryClass> orderHistoryClassArrayList, String callfrom, FragmentManager fragmentManager){
        this.context = context;
        this.orderHistoryClassArrayList = orderHistoryClassArrayList;
        this.callfrom = callfrom;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public AdapterPayment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.payment_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPayment.MyViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        OrderHistoryClass orderHistoryClass = orderHistoryClassArrayList.get(position);
        if(orderHistoryClass.getTransport_type().equals("Air")){
            holder.cargo_image.setImageResource(R.drawable.aircraft);
        }
        else if (orderHistoryClass.getTransport_type().equals("Sea")) {
            holder.cargo_image.setImageResource(R.drawable.ship);
        }

        if(callfrom.equals("All") || (callfrom.equals("Paid") && orderHistoryClass.getIsPay() == 1) || (callfrom.equals("Unpaid") && orderHistoryClass.getIsPay() == 0)){
            holder.orderNo.setText("Order No: " + orderHistoryClass.getOrder_number());
            holder.dateOrder.setText(orderHistoryClass.getDate());

            holder.priceOrder.setText("RM " + String.format( "%.2f", orderHistoryClass.getPrice()));

            if(orderHistoryClass.isPay == 0){
                holder.buttonClick.setText("Pay");
                holder.buttonClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        args.putString("ordernumber",orderHistoryClass.getOrder_number());
                        args.putString("payment_price",String.valueOf(orderHistoryClass.getPrice()));
                        showSelectPaymentDialog();
                    }
                });
            }
            else if(orderHistoryClass.isPay == 1){
                holder.buttonClick.setText("Invoice");
                holder.buttonClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Invoice").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot invoiceSnapshot : snapshot.getChildren()) {
                                    String invoiceNumber = invoiceSnapshot.getKey();

                                    if (invoiceSnapshot.hasChild("order_number")) {
                                        String orderNumberFromInvoice = invoiceSnapshot.child("order_number").getValue(String.class);

                                        // Assuming orderHistoryClass.getOrder_number() returns the order number to validate against
                                        String orderNumberToValidate = orderHistoryClass.getOrder_number();

                                        if (orderNumberToValidate.equals(orderNumberFromInvoice)) {
                                            Intent invoice = new Intent(context, InvoicePage.class);
                                            invoice.putExtra("invoice", invoiceSnapshot.child("invoice_number").getValue(String.class));
                                            invoice.putExtra("order", invoiceSnapshot.child("order_number").getValue(String.class));
                                            invoice.putExtra("payment_method", invoiceSnapshot.child("payment_method").getValue(String.class));
                                            invoice.putExtra("payment_status", invoiceSnapshot.child("payment_status").getValue(String.class));
                                            invoice.putExtra("payment_amount", String.valueOf(invoiceSnapshot.child("payment_amount").getValue(Double.class)));
                                            invoice.putExtra("date", invoiceSnapshot.child("date").getValue(String.class));


                                            context.startActivity(invoice);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle any errors that occur during the read operation
                            }
                        });
                    }

                });
            }
        }
        else{
            holder.payment_container.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return orderHistoryClassArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout payment_container;
        ImageView cargo_image;
        TextView orderNo, dateOrder, priceOrder;
        Button buttonClick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            payment_container = itemView.findViewById(R.id.payment_container);
            cargo_image = itemView.findViewById(R.id.cargo_image);
            orderNo = itemView.findViewById(R.id.orderNo);
            dateOrder = itemView.findViewById(R.id.dateOrder);
            priceOrder = itemView.findViewById(R.id.priceOrder);
            buttonClick = itemView.findViewById(R.id.buttonClick);
        }
    }
    public void showSelectPaymentDialog(){
        args.putString("callfrom","Payment");

        PaymentMethodDialog dialogFragment = new PaymentMethodDialog(context);
        dialogFragment.setArguments(args);

        dialogFragment.show(fragmentManager, "PaymentMethodDialogFragment");
    }
}
