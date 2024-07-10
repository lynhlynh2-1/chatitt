package com.example.chatitt.chats.individual_chat.info.view;

import android.os.Bundle;
import android.view.View;
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
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();
        preferenceManager = new PreferenceManager(this);
        presenter = new FriendStatusPresenter(this, preferenceManager);
        userID = getIntent().getStringExtra("id");
        presenter.getStatusFriend(userID);
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{onBackPressed();});
        binding.addFriend.setOnClickListener(v->{
            presenter.addFriend(userID);
        });
        binding.delFriend.setOnClickListener(v->{
            presenter.delFriend(userID);
        });
        binding.btnCancelReq.setOnClickListener(v->{
            presenter.cancelReq(userID);
        });
        binding.btnAccept.setOnClickListener(v->{
            presenter.accept(userID);
        });
        binding.btnDecline.setOnClickListener(v->{
            presenter.decline(userID);
        });
    }


    @Override
    public void getStatusFriendSuccess() {
        you = presenter.getYou();
        status = presenter.getStatus();
        binding.textMe.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.me.setImageBitmap(Helpers.getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_AVATAR)));

        binding.textYou.setText(you.getName());
        binding.you.setImageBitmap(Helpers.getBitmapFromEncodedString(you.getAvatar()));

        resetView();
        switch (status){
            case Constants.KEY_NOT_FRIEND:
                binding.status.setText("Người lạ");
                binding.addFriend.setVisibility(View.VISIBLE);
                break;
            case Constants.KEY_IS_FRIEND:
                binding.status.setText("Bạn bè");
                binding.delFriend.setVisibility(View.VISIBLE);
                break;
            case Constants.KEY_MY_REQ_FRIEND:
                binding.status.setText("Người lạ");
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.textMessage.setText("Bạn đã gửi lời mời kết bạn tới họ, hãy để lại tin nhắn trong lúc chờ đợi nhé!");
                binding.btnCancelReq.setVisibility(View.VISIBLE);
                break;
            case Constants.KEY_OTHER_REQ_FRIEND:
                binding.status.setText("Người lạ");
                binding.textMessage.setText("Người này đang chờ bạn phản hồi lời mời kết bạn của họ");
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.afterReceive.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void resetView(){
        binding.addFriend.setVisibility(View.GONE);
        binding.delFriend.setVisibility(View.GONE);
        binding.textMessage.setVisibility(View.GONE);
        binding.btnCancelReq.setVisibility(View.GONE);
        binding.afterReceive.setVisibility(View.GONE);
    }
    @Override
    public void getStatusFriendFail() {
        Toast.makeText(getApplicationContext(), "Xảy ra lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActionFail() {
        Toast.makeText(getApplicationContext(), "Thao tác thất bại. Vui lòng thử lại!!", Toast.LENGTH_SHORT).show();
    }
}