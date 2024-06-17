package com.example.chatitt.authentication.login.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatitt.R;
import com.example.chatitt.authentication.login.presenter.LogInPresenter;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.databinding.ActivityChangePasswordBinding;
import com.example.chatitt.databinding.ActivityEmailInputBinding;
import com.example.chatitt.databinding.FragmentLoginBinding;
import com.example.chatitt.profile.view.ChangePasswordActivity;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class EmailInputActivity extends AppCompatActivity {

    private ActivityEmailInputBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmailInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(),this);
        preferenceManager = new PreferenceManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        checkEnableOK();
        setListener();
    }
    private void checkEnableOK(){
        binding.inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.buttonOK.setEnabled(!binding.inputEmail.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setListener(){
        binding.buttonOK.setOnClickListener(v -> onChangePass() );
        binding.buttonOKAfterChange.setOnClickListener(view -> onBackPressed());
    }

    private void onChangePass() {
        loading(true);
        String email = binding.inputEmail.getText().toString().trim();
        if (!Helpers.isValidEmail(email)){
            binding.status.setText("Email không đúng định dạng, vui lòng kiểm tra lại!!");
            binding.status.setVisibility(View.VISIBLE);
            loading(false);
            return;
        }
        db.collection(Constants.KEY_COLLECTION_USERS).whereEqualTo(Constants.KEY_EMAIL, email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if(querySnapshot != null && !querySnapshot.isEmpty()){
                            // Email hợp lệ
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                loading(false);
                                                binding.status.setText("Email đổi mật khẩu đã được gửi đến email của bạn, vui lòng kiểm tra hộp thư!");
                                                binding.status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                                                binding.status.setVisibility(View.VISIBLE);

                                                binding.resendText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rippleDark));
                                                binding.resendText.setVisibility(View.VISIBLE);
                                                binding.buttonOKAfterChange.setVisibility(View.VISIBLE);
                                                binding.buttonOK.setEnabled(false);
                                                binding.buttonOK.setText("Gửi lại");

                                                new CountDownTimer(60000, 1000) {
                                                    public void onTick(long millisUntilFinished) {
                                                        binding.resendText.setText("Gửi lại sau " + millisUntilFinished / 1000 + " giây");
                                                    }

                                                    public void onFinish() {
                                                        binding.buttonOK.setEnabled(!binding.inputEmail.getText().toString().trim().isEmpty());
                                                        binding.resendText.setVisibility(View.INVISIBLE);
                                                    }
                                                }.start();

                                            } else {
                                                Toast.makeText(EmailInputActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else {
                            // Email chưa tồn tại
                            loading(false);
                            int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.error);
                            binding.inputEmail.setHintTextColor(colorEror);
                            binding.status.setText("Email chưa được đăng ký. Hãy kiểm tra lại!");
                            binding.status.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // Xử lý lỗi truy vấn
                        binding.status.setText("Lỗi!!");
                        binding.status.setVisibility(View.VISIBLE);
                    }
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