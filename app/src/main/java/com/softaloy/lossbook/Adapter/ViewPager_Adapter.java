package com.softaloy.lossbook.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.softaloy.lossbook.Fragment.Notification2_Fragment;
import com.softaloy.lossbook.Fragment.Request_Fragment;

public class ViewPager_Adapter extends FragmentPagerAdapter {
    public ViewPager_Adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new Notification2_Fragment();
            case 1: return new Request_Fragment();
            default: return new Notification2_Fragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;

        if (position==0){
            title = "Notification";
        }else if (position == 1) {
            title ="Request";
        }

        return title;
    }
}
