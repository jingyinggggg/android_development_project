package com.example.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class DeleteAccountPage extends AppCompatActivity {
    public Button delete;

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
                backSettingPage();
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

                showDeleteAccountDialog();
            }
        });

    }

    public void backSettingPage() {
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
                        backSettingPage();
                    }
                });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
