package com.example.transportpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpPage extends AppCompatActivity {

    Button signUp;

    //Dialog Variables
    Dialog showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(R.layout.sign_up_successful, 1);
//                showAlertDialog(R.layout.sign_up_fail, 2);
            }
        });

        //Password Validation
        //Get reference to the TextInputLayout and TextInputEditText for password
        TextInputLayout layoutPassword = findViewById(R.id.textInputLayoutPassword);
        TextInputEditText eTextPassword = findViewById(R.id.passwordInput);
        eTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if(password.length() >= 8){
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(password);
                    boolean isPwdContainSpeChar = matcher.find();
                    layoutPassword.setHelperText("");
                    if(isPwdContainSpeChar){
                        layoutPassword.setError("");
                    }else{
                        layoutPassword.setError("Weak Password. Please include minimum 1 special character");
                    }

                }else{
                    layoutPassword.setHelperText("***Password must be minimum 8 character");
                    layoutPassword.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Validate Re-enter password
        //Get reference to the TextInputLayout and TextInputEditText for reenter password
        TextInputLayout textInputLayoutReenterPassword = findViewById(R.id.textInputLayoutRepeatPassword);
        TextInputEditText reenterPasswordInput = findViewById(R.id.reenterPasswordInput);

        reenterPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = eTextPassword.getText().toString();
                String reenterPassword = reenterPasswordInput.getText().toString();

                //If password does not match reenter password, error message displayed
                if(password.equals(reenterPassword)){
                    textInputLayoutReenterPassword.setError(null);
                } else{
                    textInputLayoutReenterPassword.setError("Password do not match");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    public void backLoginPage(View view){
        Intent loginPage = new Intent(this, LoginPage.class);
        startActivity(loginPage);
    }

    private void showAlertDialog(int myLayout, int type){
        showDialog = new Dialog(SignUpPage.this);
        View layoutView = getLayoutInflater().inflate(myLayout, null);

        showDialog.setContentView(layoutView);
        showDialog.setCancelable(false);
        showDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        //Sign up successful
        if(type == 1){
            showDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_button_success));

            Button done = showDialog.findViewById(R.id.done);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backLoginPage(v);
                }
            });
        }
        //Sign up fail
        else{
            showDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_button_fail));
            Button signUpFail = layoutView.findViewById(R.id.tryAgainButton);
            //Click on try again button
            signUpFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog.dismiss();
                }
            });

            Button backHome = layoutView.findViewById(R.id.backHome);
            backHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backLoginPage(v);
                }
            });
        }

        showDialog.show();

    }
}