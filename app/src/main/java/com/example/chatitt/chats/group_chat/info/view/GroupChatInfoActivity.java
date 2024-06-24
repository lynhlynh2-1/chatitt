package com.example.chatitt.chats.group_chat.info.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.MainActivity;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.group_chat.info.presenter.ChangeGroupInfoPresenter;
import com.example.chatitt.chats.group_chat.info.presenter.Contract;
import com.example.chatitt.databinding.ActivityGroupChatInfoBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.Objects;

public class GroupChatInfoActivity extends AppCompatActivity implements Contract.ChangeGroupInfoViewInterface {

    ActivityGroupChatInfoBinding binding;
    private PreferenceManager preferenceManager;
    private ChangeGroupInfoPresenter presenter;
    private Chat chat;
    private String chatId;
    private String avatar;
    private String name;
    private boolean isAdmin;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        String newName = intent != null ? intent.getStringExtra("name") : null;
                        String newAvatar = intent != null ? intent.getStringExtra("avatar") : null;

                        presenter.updateChatInfo(chatId, newName, newAvatar);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListener();

    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new ChangeGroupInfoPresenter(this, preferenceManager);

        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
        if (chat != null){
            binding.textName.setText(chat.getName());
            name = chat.getName();
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
            avatar = chat.getAvatar();
            chatId = chat.getId();
            isAdmin = Objects.equals(preferenceManager.getString(Constants.KEY_USED_ID), chat.getLeader());
            presenter.getInfoRealtime(chatId);
        }
        if (isAdmin) binding.btnLeaveRoom.setVisibility(View.GONE);
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            Intent data = new Intent();
            name = binding.textName.getText().toString().trim();
            if (!name.isEmpty())
                data.putExtra("name", name);
            if (avatar != null)
                data.putExtra("avatar", avatar);
            setResult(RESULT_OK, data);
            finish();
        });
        binding.btnManageMember.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), MemberActivity.class);
            it.putExtra(Constants.KEY_COLLECTION_CHAT, chat);
            startActivity(it);
        });
        binding.btnChangeInfo.setOnClickListener(v->{
            Intent it = new Intent(getApplicationContext(), ChangeInfoGroupActivity.class);
            it.putExtra("chatId", chatId);
            it.putExtra("name", name);
            it.putExtra("avatar", avatar);
            mStartForResult.launch(it);
        });
        binding.btnLeaveRoom.setOnClickListener(v->{
            presenter.deleteMember(chatId,preferenceManager.getString(Constants.KEY_USED_ID));
        });
    }

    @Override
    public void onUpdateSuccess() {
        binding.progressBar.setVisibility(View.GONE);

        Toast.makeText(this, "Thay đổi thông tin thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChatInfoChanged(String name, String avatar) {
        chat = presenter.getChat();
        if (name != null){
            binding.textName.setText(name);
        }
        if (avatar != null){
            binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(avatar));
        }
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateEror() {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Thay đổi thông tin thất bại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateChatRealtime(String chatId, String newName, String newAvatar) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Toast toast = Toast.makeText(getApplicationContext(), "Thông tin nhóm vừa được thay đổi!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        if (this.chatId.equals(chatId)){
            if (newName != null){
                if (chat != null)
                    chat.setName(newName);
                binding.textName.setText(newName);
            }
            if (newAvatar != null){
                if (chat != null)
                    chat.setAvatar(newAvatar);
                binding.shapeableImageView.setImageBitmap(Helpers.getBitmapFromEncodedString(newAvatar));
            }
        }

    }

    @Override
    public void onDelMemEror() {
        Toast.makeText(this, "Thao tác thất bại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelMemSuccess() {
        Toast.makeText(this, "Bạn đã rời khỏi cuộc trò chuyện!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        GroupChatInfoActivity.this.finish();
    }


}