package com.example.chatitt.profile.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatitt.R;
import com.example.chatitt.authentication.LoginActivity;
import com.example.chatitt.authentication.login.view.EmailInputActivity;
import com.example.chatitt.databinding.ActivityEmailInputBinding;
import com.example.chatitt.databinding.ActivityNewEmailVerificationBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class NewEmailVerificationActivity extends AppCompatActivity {
    private ActivityNewEmailVerificationBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewEmailVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(),this);
        preferenceManager = new PreferenceManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        checkEnableOK();
        setListener();
        mAuth.addAuthStateListener(mAuthListener);
//        final Handler handler = new Handler();
//        final int delay = 1000; // 1000 milliseconds == 1 second
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                System.out.println("myHandler: here!"); // Do your work here
//                handler.postDelayed(this, delay);
//            }
//        }, delay);
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

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                //  Intent intent = new Intent(getBaseContext(), MainActivity.class);
                //intent.putExtra("EXTRA_SESSION_ID", sessionId);
                //  startActivity(intent);
                // Log.d("LOG_Login", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                loading(true);
                Log.d("LOG_Login", "onAuthStateChanged:signed_out");

                preferenceManager.putString(Constants.KEY_EMAIL, binding.inputEmail.getText().toString().trim());
                db.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USED_ID))
                                .update(Constants.KEY_EMAIL, binding.inputEmail.getText().toString().trim())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    loading(false);
                                                    Helpers.showToast(getApplicationContext(), "Cập nhật email thành công!");
                                                    finish();
                                                }else {
                                                    loading(false);
                                                    Helpers.showToast(getApplicationContext(), "Lỗi mạng! Cập nhật email thất bại đến Firestore, dù đã update trên Auth!");
                                                }
                                            }
                                        });

            }
            // ...
        }
    };

//    final Handler handler = new Handler();
//    final int delay = 1000; // 1000 milliseconds == 1 second
//    Runnable runnable = () -> check();
//    private void check (){
//
//        mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                if(Objects.equals(mAuth.getCurrentUser().getEmail(), binding.inputEmail.getText().toString().trim())){
////                if(mAuth.getCurrentUser().){
//                    System.out.println("email address: "+ mAuth.getCurrentUser().getEmail() + " isEmailVerified = true");
//                    Helpers.showToast(getApplicationContext(), "Cập nhật email thành công!");
//                    finish();
//                }else {
//                    System.out.println("myHandler: run!");
//                    handler.postDelayed(runnable, delay);
//                }
//            }
//        });
//    }
    private void verifyBeforeUpdateEmail(String email){
        mAuth.getCurrentUser().verifyBeforeUpdateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loading(false);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            binding.status.setText("Email kèm link xác thực đã được gửi đến email của bạn, vui lòng kiểm tra hộp thư!");
                            binding.status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                            binding.status.setVisibility(View.VISIBLE);

                            binding.resendText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rippleDark));
                            binding.resendText.setVisibility(View.VISIBLE);
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

//                            handler.postDelayed(runnable, delay);
                        } else {
                            Log.d(TAG, "onError verifyBeforeUpdateEmail: "+ Objects.requireNonNull(task.getException()).getMessage());
                            Toast.makeText(NewEmailVerificationActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void setListener(){
        binding.buttonOK.setOnClickListener(v -> {
            onChangeEmail();
        } );
        binding.buttonOKAfterChange.setOnClickListener(view -> onBackPressed());
    }

    private void onChangeEmail() {
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

                        if(querySnapshot == null || querySnapshot.isEmpty()){
                            // Email hợp lệ
                            verifyBeforeUpdateEmail(email);
//                            if(mAuth.getCurrentUser() == null ) {
//                                onLoginAgainBeforeChangeEmail();
//                            }else {
//                                verifyBeforeUpdateEmail(email);
//                            }
                        }else {
                            // Email đã tồn tại
                            loading(false);
                            int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.error);
                            binding.inputEmail.setHintTextColor(colorEror);
                            binding.status.setText("Email đã được dùng để đăng ký một tài khoản khác. Hãy kiểm tra lại!");
                            binding.status.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // Xử lý lỗi truy vấn
                        binding.status.setText("Lỗi truy vấn!! Hãy thử lại");
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