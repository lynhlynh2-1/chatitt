package com.example.chatitt.chats.individual_chat.info.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.individual_chat.info.presenter.Contract;
import com.example.chatitt.databinding.ActivityPrivateChatInfoBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

public class PrivateChatInfoActivity extends AppCompatActivity {

    ActivityPrivateChatInfoBinding binding;
    private Chat chat;
    private String receiveId;
    private PreferenceManager preferenceManager;
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
    }

}