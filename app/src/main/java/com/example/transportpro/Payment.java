package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.transportpro.databinding.ActivityPaymentBinding;
import com.example.transportpro.databinding.HomepageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class Payment extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);



        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottombar);

        bottomNavigationView.setSelectedItemId(R.id.bar_payment);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bar_home){
                Intent intent = new Intent(Payment.this,HomePage.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_wallet) {
                Intent intent = new Intent(Payment.this,WalletPage.class).putExtra("from","payment");
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_profile) {
                Intent intent = new Intent(Payment.this,AccountPage.class).putExtra("from","payment");
                startActivity(intent);
            }
            return true;
        });
    }

    public void  backPreviousActivity(View view){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottombar);

        if(getIntent().getStringExtra("from").equals("homepage")){
            bottomNavigationView.setSelectedItemId(R.id.bar_home);
        }
        else if(getIntent().getStringExtra("from").equals("wallet")){
            bottomNavigationView.setSelectedItemId(R.id.bar_wallet);
        }
        else if(getIntent().getStringExtra("from").equals("profile")){
            bottomNavigationView.setSelectedItemId(R.id.bar_profile);
        }

        finish();
    }
}