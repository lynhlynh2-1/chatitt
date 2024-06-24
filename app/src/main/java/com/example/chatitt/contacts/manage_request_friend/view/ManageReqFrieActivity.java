package com.example.chatitt.contacts.manage_request_friend.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.databinding.ActivityManageReqFrieBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ManageReqFrieActivity extends AppCompatActivity {
    private ActivityManageReqFrieBinding binding;
    private TabLayoutMediator tabLayoutMediator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageReqFrieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tabLayoutMediator = new TabLayoutMediator(binding.tabLayout,binding.viewPager,(tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Lời mời");
                    break;
                case 1:
                    tab.setText("Đã gửi");
                    break;
            }
        });

        PagerAdapter pagerAdapter = new PagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        tabLayoutMediator.attach();

        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }


}