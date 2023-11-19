package com.example.transportpro;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class delete_dialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete confirmation");
        builder.setMessage("Your account has been deleted.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backWelcomePage();
            }
        });

        return builder.create();
    }

    /* Main Activity class need to change to welcome page */
    public void backWelcomePage(){
        Intent settingPage = new Intent(getContext(), Setting.class);
        startActivity(settingPage);
    }

}

