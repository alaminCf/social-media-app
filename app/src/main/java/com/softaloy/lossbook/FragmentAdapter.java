package com.softaloy.lossbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.softaloy.lossbook.Fragment.Add_Fragment;
import com.softaloy.lossbook.Fragment.Add_Post_Fragment;
import com.softaloy.lossbook.Fragment.Home_Fragment;
import com.softaloy.lossbook.Fragment.Notification_Fragment;
import com.softaloy.lossbook.Fragment.Profile_Fragment;
import com.softaloy.lossbook.Fragment.Search_Fragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }




    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0: return new Home_Fragment();
            case 1: return new Add_Post_Fragment();
            case 2: return new Profile_Fragment();
            case 3: return new Notification_Fragment();
            case 4: return new Search_Fragment();
            default: return new Home_Fragment();
        }

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if (position==0){
            title = "Home";

        }

        if (position==1){
            title = "Add";

        }
        if (position==2){
            title = "Profile";

        }
        if (position==3){
            title = "Notify";


        }
        if (position==4){
            title = "Search";

        }

        return title;
    }
}
