package com.softaloy.lossbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.softaloy.lossbook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());




        binding.viewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tablay.setupWithViewPager(binding.viewpager);

        //setSupportActionBar(binding.toolbar);
       // MainActivity.this.setTitle("My Profile");
       // getSupportActionBar().hide();

        //binding.tablay.getTabAt(0).setIcon(R.drawable.home_24);

    }
}