package com.example.chatitt.profile.view;

import static com.example.chatitt.ultilities.Helpers.showToast;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.chatitt.R;
import com.example.chatitt.databinding.ActivityChangePasswordBinding;
import com.example.chatitt.profile.presenter.ChangePassContract;
import com.example.chatitt.profile.presenter.ChangePassPresenter;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

public class ChangePasswordActivity extends AppCompatActivity implements ChangePassContract.ViewInterface {


    ActivityChangePasswordBinding binding;
    private PreferenceManager preferenceManager;
    private String currPass = null;
    private String newPass = null;
    private ChangePassPresenter changePassPresenter;
    private Boolean isHidePassEdt = true;

    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(),this);

        preferenceManager = new PreferenceManager(getApplicationContext());
        changePassPresenter = new ChangePassPresenter(this, preferenceManager, getApplicationContext());


        if (getIntent().getStringExtra(Constants.KEY_EMAIL)!= null){
            email = getIntent().getStringExtra(Constants.KEY_EMAIL);
        }else email = preferenceManager.getString(Constants.KEY_EMAIL);

        getCurrPass();
        setListener();
        onEditTextStatusChange();
    }
    private void onEditTextStatusChange(){
        int colorFocus = ContextCompat.getColor(getApplicationContext(), R.color.primary_text);
        int colorDefault = ContextCompat.getColor(getApplicationContext(), R.color.secondary_text);
        binding.inputCurrentPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_good);
                    binding.inputCurrentPass.setHintTextColor(colorFocus);
                }
                else {
                    binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input);
                    binding.inputCurrentPass.setHintTextColor(colorDefault);
//                    if (binding.inputCurrentPass.getText().toString().isEmpty()) {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_wrong);
//                    } else {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_good);
//                    }
                }
            }
        });
        binding.inputNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_good);
                    binding.inputNewPassword.setHintTextColor(colorFocus);
                }
                else {
                    binding.inputNewPassword.setBackgroundResource(R.drawable.background_input);
                    binding.inputNewPassword.setHintTextColor(colorDefault);
                }
            }
        });
        binding.inputConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_good);
                    binding.inputConfirmPassword.setHintTextColor(colorFocus);
                }
                else {
                    binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input);
                    binding.inputConfirmPassword.setHintTextColor(colorDefault);
                }
            }
        });
    }
    private void getCurrPass(){
        System.out.println("curren password");
        currPass = preferenceManager.getString(Constants.KEY_PASSWORD);
        System.out.println(currPass);
    }
    private Boolean isValidPassDetails(){
        int colorEror = ContextCompat.getColor(this, R.color.error);
        if(binding.inputCurrentPass.getText().toString().trim().isEmpty()){
            binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputCurrentPass.setHintTextColor(colorEror);
            showToast(getApplicationContext(),"Hãy nhập mật khẩu hiện tại");
            return false;
        }else if(binding.inputNewPassword.getText().toString().trim().isEmpty()){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            showToast(getApplicationContext(),"Hãy nhập mật khẩu mới");
            return false;
        }else if(binding.inputConfirmPassword.getText().toString().isEmpty()){
            binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputConfirmPassword.setHintTextColor(colorEror);
            showToast(getApplicationContext(),"Hãy nhập lại mật khẩu mới");
            return false;
        }else if(!binding.inputNewPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputConfirmPassword.setHintTextColor(colorEror);
            showToast(getApplicationContext(),"Xác nhận mật khẩu mới chưa không đồng nhất, hãy kiểm tra lại");
            return false;
        }else if (binding.inputCurrentPass.getText().toString().equals(binding.inputNewPassword.getText().toString())){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            showToast(getApplicationContext(),"Mật khẩu mới không được giống với mật khẩu cũ");
            return false;
        }else if(binding.inputNewPassword.getText().toString().length() < 6){
            binding.inputNewPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputNewPassword.setHintTextColor(colorEror);
            binding.inputConfirmPassword.setBackgroundResource(R.drawable.background_input_wrong);
            binding.inputConfirmPassword.setHintTextColor(colorEror);
            showToast(getApplicationContext(),"Mật khẩu cần có tối thiểu 6 kí tự");
            return false;
        }
        else{
            return true;
        }

    }
    public void loading(boolean b) {
        if (b){
            binding.status.setVisibility(View.INVISIBLE);
        }
        binding.progressBar.setVisibility(b? View.VISIBLE : View.GONE);
        binding.buttonChangePass.setVisibility(b? View.GONE : View.VISIBLE);

    }
    private void setListener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.buttonChangePass.setOnClickListener(view -> {
            if (isValidPassDetails()){
                binding.buttonChangePass.setVisibility(View.GONE);
                loading(true);
                String newPass = binding.inputNewPassword.getText().toString().trim();
                String currPass = binding.inputCurrentPass.getText().toString().trim();
                changePassPresenter.changePass(currPass,newPass);
            }
        });
        binding.inputCurrentPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.inputCurrentPass.getRight() - binding.inputCurrentPass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(isHidePassEdt){
                            binding.inputCurrentPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.inputCurrentPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red, 0);
                            isHidePassEdt = false;
                        }else {
                            binding.inputCurrentPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.inputCurrentPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_red_eye, 0);
                            isHidePassEdt = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        binding.inputNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.inputNewPassword.getRight() - binding.inputNewPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(isHidePassEdt){
                            binding.inputNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.inputNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red, 0);
                            isHidePassEdt = false;
                        }else {
                            binding.inputNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.inputNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_red_eye, 0);
                            isHidePassEdt = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        binding.inputConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.inputConfirmPassword.getRight() - binding.inputConfirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(isHidePassEdt){
                            binding.inputConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.inputConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red, 0);
                            isHidePassEdt = false;
                        }else {
                            binding.inputConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.inputConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_red_eye, 0);
                            isHidePassEdt = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onChangePassSuccess() {
        loading(false);
        showToast(getApplicationContext(),"Thay đổi mật khẩu thành công.");
        onBackPressed();
    }

    @Override
    public void onCurrentPassWrong() {
        loading(false);
        binding.inputNewPassword.getText().clear();
        binding.inputConfirmPassword.getText().clear();
        binding.inputCurrentPass.getText().clear();
        binding.status.setVisibility(View.VISIBLE);
        binding.status.setText("Mật khẩu hiện tại không đúng!");
    }

    @Override
    public void onChangePassError() {
        loading(false);
        binding.inputNewPassword.getText().clear();
        binding.inputConfirmPassword.getText().clear();
        binding.inputCurrentPass.getText().clear();
        binding.status.setVisibility(View.VISIBLE);
        binding.status.setText("Thay đổi mật khẩu thất bại! Hãy thử lại.");
    }
}