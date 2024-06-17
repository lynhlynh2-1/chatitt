package com.example.chatitt.authentication.login.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.chatitt.R;
import com.example.chatitt.databinding.ActivityEmailInputBinding;
import com.example.chatitt.databinding.ActivityOtpAuthenBinding;
import com.example.chatitt.profile.view.ChangePasswordActivity;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class OtpAuthenActivity extends AppCompatActivity {

    private ActivityOtpAuthenBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authen);
        binding = ActivityOtpAuthenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(),this);
        preferenceManager = new PreferenceManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = getIntent().getStringExtra(Constants.KEY_EMAIL);
        checkEnableOK();
        setListener();

    }

    private void checkEnableOK(){
        binding.inputOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.buttonOK.setEnabled(!binding.inputOTP.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setListener(){
        binding.buttonOK.setOnClickListener(view -> {
            loading(true);
            String otp = binding.inputOTP.getText().toString().trim();

            db.collection(Constants.KEY_COLLECTION_USERS).whereEqualTo(Constants.KEY_EMAIL, email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            loading(false);

                            if(querySnapshot != null && !querySnapshot.isEmpty()){
                                // Email hợp lệ
                                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                                intent.putExtra(Constants.KEY_EMAIL, email);
                                startActivity(intent);
                            }else {
                                // Email chưa tồn tại
                                int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.error);
                                binding.inputOTP.setHintTextColor(colorEror);
                                binding.status.setText("Email chưa được đăng ký. Hãy kiểm tra lại!");
                                binding.status.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // Xử lý lỗi truy vấn
                            binding.status.setText("Lỗi!!");
                            binding.status.setVisibility(View.VISIBLE);
                        }
                    });
        });
    }

    public void loading(boolean b) {
        if (b){
            binding.status.setVisibility(View.INVISIBLE);
        }
        binding.progressBar.setVisibility(b? View.VISIBLE : View.GONE);
        binding.buttonOK.setVisibility(b? View.GONE : View.VISIBLE);
    }
}