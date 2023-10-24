package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteAccountPage extends AppCompatActivity {
    public Button delete;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference reference;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_page);

        TextView cancel = findViewById(R.id.cancel_button);
        String text = "Cancel";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeleteAccountPage.this, "canceled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan1, 0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        cancel.setText(ss);
        cancel.setMovementMethod(LinkMovementMethod.getInstance());

        // Confirm delete
        delete = (Button) findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                reference = db.getReference("User");

                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                String username = sharedPreferences.getString(KEY_USERNAME,null);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(username)){
                            DatabaseReference userReference = reference.child(username);
                            int delete = 1;
                            userReference.child("isDeletedAcc").setValue(delete);

                            showDeleteAccountDialog();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent loginpage = new Intent(DeleteAccountPage. this, LoginPage.class);
                                    startActivity(loginpage);
                                }
                            }, 2200);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    public void backSettingPage(View view) {
        Intent settingPage = new Intent(this, Setting.class);
        startActivity(settingPage);
    }

    public void showDeleteAccountDialog(){
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_account_dialog, null);

        // Find views in the custom layout
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);

        // Customize the dialog content (e.g., set image and message)
        dialogMessage.setText("You have successfully deleted your account !");

        // Create the custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
