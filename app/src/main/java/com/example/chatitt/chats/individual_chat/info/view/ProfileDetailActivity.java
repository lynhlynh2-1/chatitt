package com.example.chatitt.chats.individual_chat.info.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.individual_chat.info.presenter.Contract;
import com.example.chatitt.chats.individual_chat.info.presenter.ProfileDetailPresenter;
import com.example.chatitt.databinding.ActivityProfileDetailBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

public class ProfileDetailActivity extends AppCompatActivity implements Contract.ProfileDetailInterface{

    ActivityProfileDetailBinding binding;
    private PreferenceManager preferenceManager;
    private ProfileDetailPresenter presenter;
    private User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        presenter = new ProfileDetailPresenter(this);

        String userId = getIntent().getStringExtra("id");
        presenter.getInfo(userId);
        loading(true);
        setListener();

    }

    private void loading(boolean status){
        binding.progressBar.setVisibility(status ? View.VISIBLE: View.GONE);
        binding.scrollDetail.setVisibility(status ? View.GONE: View.VISIBLE);
    }
    private void setListener(){
        binding.imageBack.setOnClickListener(v->{onBackPressed();});
    }

    @Override
    public void getInfoDetailSuccess() {
        loading(false);
        userModel = presenter.getUser();

        binding.textName.setText(userModel.getName());

        binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
        if (userModel.getCoverImage() != null){
            binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getCoverImage()));
        }
        if (userModel.getBirthday() != null){
            binding.textBirthday.setText(userModel.getBirthday());
            binding.textBirthday.setVisibility(View.VISIBLE);
        }else {
            binding.textBirthday.setVisibility(View.GONE);
        }
        if (userModel.getGender() != null){
            binding.textGender.setText(userModel.getGender());
            binding.textGender.setVisibility(View.VISIBLE);
        }else {
            binding.textGender.setVisibility(View.GONE);
        }
        if (userModel.getCity() != null){
            String city = userModel.getCity()+" (" + userModel.getCountry()+")";
            binding.textCity.setText(city);
            binding.textAddressDetail.setText(userModel.getAddress());
        }else {
            binding.textCity.setText("Không rõ");
            binding.textAddressDetail.setText("Không rõ");
        }

        binding.textEmail.setText(userModel.getEmail());

        if (userModel.getEmail() != null){
            binding.textPhone.setText(userModel.getPhonenumber());
            binding.textPhone.setVisibility(View.VISIBLE);
        }else {
            binding.textPhone.setText("Không có");
        }

    }

    @Override
    public void getInfoDetailFail() {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Xảy ra lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
    }
}