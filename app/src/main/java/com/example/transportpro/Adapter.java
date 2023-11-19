package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Adapter extends FragmentStateAdapter {


    public Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return  new RequestingParcel();
            case 2:
                return  new CollectedParcel();
            case 0:
            default:
                return  new AllParcel();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
