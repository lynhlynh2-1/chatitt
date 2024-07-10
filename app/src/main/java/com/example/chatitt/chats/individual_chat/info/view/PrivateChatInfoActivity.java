package com.example.chatitt.chats.individual_chat.info.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.MainActivity;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.group_chat.info.view.GroupChatInfoActivity;
import com.example.chatitt.chats.individual_chat.info.presenter.Contract;
import com.example.chatitt.chats.individual_chat.info.presenter.PrivateChatInfoPresenter;
import com.example.chatitt.databinding.ActivityPrivateChatInfoBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

public class PrivateChatInfoActivity extends AppCompatActivity implements Contract.ChatInfoInterface{

    ActivityPrivateChatInfoBinding binding;
    private Chat chat;
    private String receiveId;
    private PreferenceManager preferenceManager;
    private PrivateChatInfoPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateChatInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListener();

    }
    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new PrivateChatInfoPresenter(this);
        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
        if (chat != null){
            binding.textName.setText(chat.getName());
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
            receiveId = chat.getLeader();
            if (receiveId.equals(preferenceManager.getString(Constants.KEY_USED_ID)))
                receiveId = chat.getMembers().get(0);
        }
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.btnDetailProfile.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), ProfileDetailActivity.class);
            if (receiveId == null)
                Log.d("PrivateChatInfoActivity  + btnDetailProfile", "null");
            it.putExtra("id", receiveId);
            startActivity(it);
        });

        binding.statusFriend.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), FriendStatusActivity.class);
            if (receiveId == null)
                Log.d("PrivateChatInfoActivity statusFriend", "null");
            it.putExtra("id", receiveId);
            startActivity(it);
        });

        binding.deleteChat.setOnClickListener(v->{
            presenter.deleteChat(chat.getId());
        });
    }

    @Override
    public void onDelMemEror() {
        Toast.makeText(this, "Thao tác thất bại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelGroupSuccess() {
        Toast.makeText(this, "Đoạn chat đã bị xóa", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        PrivateChatInfoActivity.this.finish();
    }
}