package com.example.transportpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

public class Edit_order extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    EditOrderTabAdapter newOrderTabAdapter;
    Button nextButton;
    int currentTab = 0;
    ImageView back_btn;
    LinearLayout edit_btn_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order);
        tabLayout = findViewById(R.id.orderTab);
        viewPager2 = findViewById(R.id.view_pager);
        newOrderTabAdapter = new EditOrderTabAdapter(this);
        nextButton = findViewById(R.id.nextButton);
        viewPager2.setAdapter(newOrderTabAdapter);
        back_btn = findViewById(R.id.back_btn);

        edit_btn_container = findViewById(R.id.edit_btn_container);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Edit_order.this,MainActivity.class);
                startActivity(intent);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTab < newOrderTabAdapter.getItemCount() - 1) {
                    currentTab++;
                    viewPager2.setCurrentItem(currentTab);
                    tabLayout.selectTab(tabLayout.getTabAt(currentTab));
                    if (currentTab == newOrderTabAdapter.getItemCount() - 1) {
                        nextButton.setVisibility(View.GONE);
                        edit_btn_container.setVisibility(View.VISIBLE);
                    }
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    edit_btn_container.setVisibility(View.GONE);
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                viewPager2.setCurrentItem(currentTab);
                if (currentTab == newOrderTabAdapter.getItemCount() - 1) {
                    nextButton.setVisibility(View.GONE);
                    edit_btn_container.setVisibility(View.VISIBLE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    edit_btn_container.setVisibility(View.GONE);
                    nextButton.setText("Next");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Update the selected tab when the user scrolls
                currentTab = position;
                tabLayout.selectTab(tabLayout.getTabAt(position));

                // Update the button text when reaching the last tab
                if (position == newOrderTabAdapter.getItemCount() - 1) {
                    nextButton.setVisibility(View.GONE);
                    edit_btn_container.setVisibility(View.VISIBLE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    edit_btn_container.setVisibility(View.GONE);
                    nextButton.setText("Next");
                }
            }
        });


    }
}