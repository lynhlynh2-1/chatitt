package com.example.chatitt.authentication.login.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatitt.MainActivity;
import com.example.chatitt.R;
import com.example.chatitt.authentication.login.presenter.LogInContract;
import com.example.chatitt.authentication.login.presenter.LogInPresenter;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.databinding.FragmentLoginBinding;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

public class LoginFragment extends Fragment implements LogInContract.ViewInterface {

    private FragmentLoginBinding binding;

    private float v= 0;
    private Boolean isHidePassEdt = true;

    private LogInPresenter logInPresenter;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        // Get the root view from the binding
        View rootView = binding.getRoot();

        Helpers.setupUI(binding.LoginFragment, requireActivity());
        preferenceManager = new PreferenceManager(requireContext());

        logInPresenter = new LogInPresenter(this, preferenceManager, requireContext());
        showAnimationFromStart();
        checkEnableLogin();
        onEditTextStatusChange();
        setListener();
        return rootView;
    }

    private void setListener(){

        binding.login.setOnClickListener(view -> {
            User user = new User(binding.edtPhone.getText().toString().trim(),
                    binding.edtPass.getText().toString().trim());
            if (isValidLoginDetails(user)){
                binding.login.setVisibility(View.GONE);
                binding.loading.setVisibility(View.VISIBLE);
                binding.status.setVisibility(View.GONE);
                logInPresenter.login(user.getEmail(),user.getPassword());
            }
        });
        binding.imgShowPass.setOnClickListener(view ->{
            if(isHidePassEdt){
                binding.edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imgShowPass.setImageResource(R.drawable.red);
                isHidePassEdt = false;
            }else {
                binding.edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imgShowPass.setImageResource(R.drawable.ic_remove_red_eye);
                isHidePassEdt = true;
            }
        });
    }
    private boolean isValidLoginDetails(User user) {
        int colorEror = ContextCompat.getColor(requireActivity(), R.color.md_theme_light_error);
        int colorDefault = ContextCompat.getColor(requireActivity(), R.color.black);

        if(user.getEmail().isEmpty()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);

            binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPhone.setTextColor(colorEror);
            binding.status.setText("Hãy nhập Email!");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isValidEmail()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);

            binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPhone.setTextColor(colorEror);
            binding.status.setText("Định dạng email chưa đúng!");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isValidPass()){
            binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPhone.setTextColor(colorDefault);

            binding.password.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPass.setTextColor(colorEror);
            binding.status.setText("Password cần tối thiểu 6 ký tự");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }
        return true;


    }

    private void checkEnableLogin(){
        binding.edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.login.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.login.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void onEditTextStatusChange(){
        int colorFocus = ContextCompat.getColor(requireActivity(), R.color.md_theme_light_primary);
        int colorDefault = ContextCompat.getColor(requireActivity(), R.color.secondary_text);
        binding.edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.phone.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtPhone.setHintTextColor(colorFocus);
                }
                else {
                    binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtPhone.setHintTextColor(colorDefault);
                }
            }
        });
        binding.edtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.password.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtPass.setHintTextColor(colorFocus);
                }
                else {
                    binding.password.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtPass.setHintTextColor(colorDefault);
                }
            }
        });
    }
    private void showAnimationFromStart(){
        binding.password.setTranslationX(800);
        binding.password.setAlpha(v);
        binding.password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        binding.phone.setTranslationX(800);
        binding.phone.setAlpha(v);
        binding.phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        binding.forgotPass.setTranslationX(800);
        binding.forgotPass.setAlpha(v);
        binding.forgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        binding.login.setTranslationY(300);
        binding.login.setAlpha(v);
        binding.login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }

    @Override
    public void onLoginSuccess() {
        binding.login.setVisibility(View.VISIBLE);
        binding.loading.setVisibility(View.GONE);
        binding.status.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void onLoginWrongEmailOrPassword() {
        binding.login.setVisibility(View.VISIBLE);
        binding.loading.setVisibility(View.GONE);
        binding.status.setText("Email hoặc mật khẩu không đúng");
        binding.status.setVisibility(View.VISIBLE);
    }

}