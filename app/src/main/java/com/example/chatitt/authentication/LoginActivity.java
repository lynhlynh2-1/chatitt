package com.example.chatitt.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatitt.MainActivity;
import com.example.chatitt.R;
import com.example.chatitt.databinding.ActivityLoginBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private TabLayoutMediator tabLayoutMediator;
    private float v= 0;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//            if (preferenceManager.getString(Constants.KEY_PRIVATE_KEY) != null){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
//            }else {
//                Intent intent = new Intent(getApplicationContext(), TransferDataActivity.class);
//                startActivity(intent);
//                finish();
//            }
        }
        tabLayoutMediator = new TabLayoutMediator(binding.tabLayout,binding.viewPager,(tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("ĐĂNG NHẬP");
                    break;
                case 1:
                    tab.setText("ĐĂNG KÝ");
                    break;
            }
        });


        LoginViewPagerAdapter loginViewPagerAdapter = new LoginViewPagerAdapter(this);
        binding.viewPager.setAdapter(loginViewPagerAdapter);

        binding.tabLayout.setTranslationY(300);
        binding.tabLayout.setAlpha(v);
        binding.tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        tabLayoutMediator.attach();
        Helpers.setupUI(binding.loginActivity, this);

    }
}