package com.example.transportpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserViewHolder> {

    private List<User> userList;
    private String adminUsername;
    private Context context;

    public AdapterUser(Context context, List<User> userList, String adminUsername) {
        this.context = context;
        this.userList = userList;
        this.adminUsername = adminUsername;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        if (user.username.equals(adminUsername)) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }

        holder.usernameTextView.setText(user.username);
        holder.emailTextView.setText(user.email);
        holder.contactTextView.setText(user.contact);

        if (user.isAdminAcc == 1) {
            holder.adminStatusTextView.setVisibility(View.VISIBLE);
        } else {
            holder.adminStatusTextView.setVisibility(View.GONE);
        }

        if (user.isDeletedAcc == 1) {
            holder.deactivatedStatusTextView.setVisibility(View.VISIBLE);
        } else {
            holder.deactivatedStatusTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailsAdmin.class);
            intent.putExtra("username", user.username);
            intent.putExtra("email", user.email);
            intent.putExtra("contact", user.contact);
            intent.putExtra("fullname", user.fullname);
            intent.putExtra("isAdminAcc", user.isAdminAcc);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (int) userList.stream().filter(user -> !user.username.equals(adminUsername)).count();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, emailTextView, contactTextView;
        TextView adminStatusTextView, deactivatedStatusTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
            adminStatusTextView = itemView.findViewById(R.id.adminStatusTextView);
            deactivatedStatusTextView = itemView.findViewById(R.id.deactivatedStatusTextView);
        }
    }
}

