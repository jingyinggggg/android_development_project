package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class NewOrderTabAdapter extends FragmentStateAdapter {
    public NewOrderTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SelectFragment();
            case 1:
                return new TransportFragment();
            case 2:
                return new ReceiverFragment();
            case 3:
                return new ConfirmFragment();
            default:
                return new SelectFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
