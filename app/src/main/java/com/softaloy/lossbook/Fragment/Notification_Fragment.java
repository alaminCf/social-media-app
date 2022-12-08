package com.softaloy.lossbook.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.softaloy.lossbook.Adapter.ViewPager_Adapter;
import com.softaloy.lossbook.R;


public class Notification_Fragment extends Fragment {


    public Notification_Fragment() {
        // Required empty public constructor
    }


    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_, container, false);

        viewPager = view.findViewById(R.id.viewpager_noti);
        viewPager.setAdapter(new ViewPager_Adapter(getFragmentManager()));
        tabLayout = view.findViewById(R.id.tabLay);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}