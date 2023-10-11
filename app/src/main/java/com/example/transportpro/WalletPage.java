package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.transportpro.databinding.ActivityWalletPageBinding;
import com.example.transportpro.databinding.HomepageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WalletPage extends AppCompatActivity {
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_page);




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

}