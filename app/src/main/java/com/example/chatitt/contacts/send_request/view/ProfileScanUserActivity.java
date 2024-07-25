package com.example.chatitt.contacts.send_request.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat.view.ChatActivity;
import com.example.chatitt.contacts.send_request.presenter.ProfileScanUserContract;
import com.example.chatitt.contacts.send_request.presenter.ProfileScanUserPresenter;
import com.example.chatitt.databinding.ActivityProfileScanUserBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.Objects;

public class ProfileScanUserActivity extends AppCompatActivity implements ProfileScanUserContract.ViewInterface {

    ActivityProfileScanUserBinding binding;
    ProfileScanUserPresenter presenter;
    PreferenceManager preferenceManager;
    User you;
    private String userId, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileScanUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListener();
        if (userId != null)
            presenter.getStatusFriend(userId);
        binding.shimmerEffect.startShimmerAnimation();
    }

    private void init(){
        userId = getIntent().getStringExtra("userId");
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new ProfileScanUserPresenter(this, preferenceManager);

    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v-> onBackPressed());
        binding.addFriend.setOnClickListener(v->{
            presenter.setReqFriend(you.getId());
        });

        binding.btnAccept.setOnClickListener(v->{
            presenter.setAcceptFriend(you.getId(),"1");
        });

        binding.btnDecline.setOnClickListener(v->{
            presenter.setAcceptFriend(you.getId(),"2");
        });

        binding.btnCancel.setOnClickListener(v->{
            presenter.delReq(you.getId());
        });
        binding.btnChat.setOnClickListener(v->{
            binding.loading.setVisibility(View.VISIBLE);
            presenter.findChat(you);
        });

        binding.btnChat1.setOnClickListener(v->{
            binding.loading.setVisibility(View.VISIBLE);
            presenter.findChat(you);
        });

    }
    @Override
    public void onFindChatSucces(String id) {
        binding.loading.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_COLLECTION_CHAT, presenter.getChat());
        intent.putExtra(Constants.KEY_RECEIVER_ID, id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFindChatError() {
        binding.loading.setVisibility(View.VISIBLE);
        Helpers.showToast(getApplicationContext(),"Xảy ra lỗi, vui lòng kiểm tra kết nối mạng và thử lại!!");
    }

    @Override
    public void onChatNotExist(User user) {
        binding.loading.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }

    @Override
    public void getStatusFriendSuccess() {

        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.imageProfile.setVisibility(View.VISIBLE);
        binding.cardView2.setVisibility(View.VISIBLE);

        binding.before.setVisibility(View.GONE);
        binding.afterSend.setVisibility(View.GONE);
        binding.afterReceive.setVisibility(View.GONE);
        binding.textMessage.setVisibility(View.GONE);

        you = presenter.getYou();
        if (you.getCoverImage() == null) {
            binding.coverImg.setImageResource(R.drawable.cover_img_placeholder);
        } else
            binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(you.getCoverImage()));

        binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(you.getAvatar()));
        binding.textName.setText(you.getName());
        if (Objects.equals(presenter.getStatus(), Constants.KEY_NOT_FRIEND)) {
            binding.before.setVisibility(View.VISIBLE);
            binding.addFriend.setVisibility(View.VISIBLE);
        } else if (Objects.equals(presenter.getStatus(), Constants.KEY_IS_FRIEND)) {
            binding.before.setVisibility(View.VISIBLE);
            binding.addFriend.setVisibility(View.GONE);
        } else if (Objects.equals(presenter.getStatus(), Constants.KEY_MY_REQ_FRIEND)) {
            binding.textMessage.setVisibility(View.VISIBLE);
            binding.afterSend.setVisibility(View.VISIBLE);
        } else if (Objects.equals(presenter.getStatus(), Constants.KEY_OTHER_REQ_FRIEND)) {
            binding.afterReceive.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getStatusFriendFail() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void setReqFriendSuccess() {
        binding.before.setVisibility(View.GONE);
        binding.textMessage.setVisibility(View.VISIBLE);
        binding.afterSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void setReqFriendFail() {
        Toast.makeText(this, "Gửi kết bạn thất bại, hãy thử lại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAcceptSuccess(String status) {
        binding.afterReceive.setVisibility(View.GONE);
        binding.before.setVisibility(View.VISIBLE);
        if (Objects.equals(status, "1"))
            binding.addFriend.setVisibility(View.GONE);
        else
            binding.addFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAcceptFail() {
        Toast.makeText(this, "Thao tác thất bại, hãy thử lại!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void delReqFail() {
        Toast.makeText(this, "Hủy yêu cầu thất bại, hãy thử lại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delReqSuccess() {
        binding.textMessage.setVisibility(View.GONE);
        binding.afterSend.setVisibility(View.GONE);
        binding.before.setVisibility(View.VISIBLE);

    }
}