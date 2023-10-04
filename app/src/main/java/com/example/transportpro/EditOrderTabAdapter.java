package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class EditOrderTabAdapter extends FragmentStateAdapter {
    public EditOrderTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Edit_transport_fragment();
            case 1:
                return new Edit_receiver_fragment();
            case 2:
                return new Edit_confirm_fragment();
            default:
                return new Edit_transport_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
