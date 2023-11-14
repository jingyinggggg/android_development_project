package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordSecurityPage extends AppCompatActivity {
    public ImageButton setting;
    private Button confirm;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference reference;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_security_page);

// Back to setting page
        setting = (ImageButton) findViewById(R.id.backArrow);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backSettingPage();
            }
        });

// Cancel change password
        TextView cancel = findViewById(R.id.cancel_button);
        String text = "Cancel";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PasswordSecurityPage.this, "Cancel change password.", Toast.LENGTH_SHORT).show();
                backSettingPage();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan1, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        cancel.setText(ss);
        cancel.setMovementMethod(LinkMovementMethod.getInstance());

        // Add TextWatcher to the current and new password fields for real-time validation
        EditText currentPassword = findViewById(R.id.currentPassword);
        EditText newPassword = findViewById(R.id.newPassword);
        EditText reenterNewPassword = findViewById(R.id.reenterNewPassword);
        TextView passwordError = findViewById(R.id.password_error);
        TextView reenterNewPasswordError = findViewById(R.id.reenter_password_error);

        currentPassword.addTextChangedListener(new PasswordTextWatcher(currentPassword, newPassword, passwordError, reenterNewPassword, reenterNewPasswordError));
        newPassword.addTextChangedListener(new PasswordTextWatcher(currentPassword, newPassword, passwordError, reenterNewPassword, reenterNewPasswordError));
        reenterNewPassword.addTextChangedListener(new PasswordTextWatcher(currentPassword, newPassword, passwordError, reenterNewPassword, reenterNewPasswordError));

        confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPasswordText = currentPassword.getText().toString();
                String newPasswordText = newPassword.getText().toString();
                String reenterNewPasswordText = reenterNewPassword.getText().toString();

                if(!currentPasswordText.isEmpty() && !newPasswordText.isEmpty() && !reenterNewPasswordText.isEmpty() && newPasswordText != reenterNewPasswordText){
                    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                    String username = sharedPreferences.getString(KEY_USERNAME,null);

                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("User");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(username)){
                                DatabaseReference userReference = reference.child(username);
                                String storedPassword = snapshot.child(username).child("password").getValue(String.class);

                                if(currentPasswordText.equals(storedPassword)){
                                    if (passwordError.getVisibility() == View.GONE && reenterNewPasswordError.getVisibility() == View.GONE){
                                        userReference.child("password").setValue(newPasswordText);
                                        showConfirmDialog(view);
                                    }
                                    else{
                                        Toast.makeText(PasswordSecurityPage.this, "Something is wrong, Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(PasswordSecurityPage.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(PasswordSecurityPage.this, "Something is wrong, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    public void showConfirmDialog(View v){
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.confirm_dialog, null);

        // Find views in the custom layout
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);

        // Customize the dialog content (e.g., set image and message)
        dialogMessage.setText("You have successfully changed your password.");

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

    public void backSettingPage(){
        Intent settingPage = new Intent(this, Setting.class);
        startActivity(settingPage);
    }
    private class PasswordTextWatcher implements TextWatcher {
        private EditText currentPassword;
        private EditText newPassword;
        private TextView passwordError;
        private EditText reenterNewPassword;
        private TextView reenterNewPasswordError;

        public PasswordTextWatcher(EditText currentPassword, EditText newPassword, TextView passwordError, EditText reenterNewPassword, TextView reenterNewPasswordError) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
            this.passwordError = passwordError;
            this.reenterNewPassword = reenterNewPassword;
            this.reenterNewPasswordError = reenterNewPasswordError;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }


        @Override
        public void afterTextChanged(Editable editable) {
            String currentPasswordText = currentPassword.getText().toString();
            String newPasswordText = newPassword.getText().toString();
            String reenterNewPasswordText = reenterNewPassword.getText().toString();

            boolean isCurrentNewMatch = currentPasswordText.equals(newPasswordText);
            boolean isNewReenterNotMatch = !newPasswordText.equals(reenterNewPasswordText);

            if (isCurrentNewMatch) {
                passwordError.setVisibility(View.VISIBLE);
            } else {
                passwordError.setVisibility(View.GONE);
                reenterNewPasswordError.setVisibility(View.GONE);
            }

            if(isNewReenterNotMatch){
                reenterNewPasswordError.setVisibility(View.VISIBLE);
            }else{
                reenterNewPasswordError.setVisibility(View.GONE);
            }
        }
    }

}



