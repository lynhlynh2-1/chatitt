package com.example.chatitt.chats.individual_chat.info.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.individual_chat.info.presenter.Contract;
import com.example.chatitt.chats.individual_chat.info.presenter.FriendStatusPresenter;
import com.example.chatitt.databinding.ActivityFriendStatusBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.Objects;

public class FriendStatusActivity extends AppCompatActivity implements Contract.FriendStatusInterface {

    ActivityFriendStatusBinding binding;
    private PreferenceManager preferenceManager;
    private FriendStatusPresenter presenter;
    private User me, you;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();
        preferenceManager = new PreferenceManager(this);
        presenter = new FriendStatusPresenter(this, preferenceManager);
        String id = getIntent().getStringExtra("id");
        presenter.getStatusFriend(id);
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{onBackPressed();});
    }


    @Override
    public void getStatusFriendSuccess() {
        you = presenter.getYou();
        status = presenter.getStatus();
        binding.textMe.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.me.setImageBitmap(Helpers.getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_AVATAR)));

        binding.textYou.setText(you.getName());
        binding.you.setImageBitmap(Helpers.getBitmapFromEncodedString(you.getAvatar()));

        if (Objects.equals(status, Constants.KEY_IS_FRIEND)){
            binding.status.setText("Bạn bè");
            binding.sendReqFriend.setText("Xóa bạn");
        }else {
            binding.status.setText("Người lạ");
            binding.sendReqFriend.setText("Gửi kết bạn");
        }
    }

    @Override
    public void getStatusFriendFail() {
        Toast.makeText(getApplicationContext(), "Xảy ra lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
    }

}