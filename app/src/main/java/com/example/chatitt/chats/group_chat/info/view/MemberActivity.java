package com.example.chatitt.chats.group_chat.info.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chatitt.MainActivity;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.group_chat.info.presenter.Contract;
import com.example.chatitt.chats.group_chat.info.presenter.MemActivityPresenter;
import com.example.chatitt.databinding.ActivityMemberBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class MemberActivity extends AppCompatActivity implements Contract.MemListViewInterface{

    ActivityMemberBinding binding;
    private PreferenceManager preferenceManager;
    private MemActivityPresenter presenter;
    private Chat chat;
    private boolean isAdmin;
    private List<User> userModelList;
    private MemberAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListener();

    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        ArrayList<String> listMemberAdded = intent != null ? intent.getStringArrayListExtra("listMemberAdded") : null;
                        Log.d("Add MEM:" , listMemberAdded.get(0));
                        presenter.addMember(chat.getId(), listMemberAdded);
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });
    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        presenter = new MemActivityPresenter(this, preferenceManager);


        userModelList = new ArrayList<>();
        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
        isAdmin = chat.getLeader().equals(preferenceManager.getString(Constants.KEY_USED_ID));
        if (!isAdmin){
            binding.addMem.setVisibility(View.INVISIBLE);
        }
        adapter = new MemberAdapter(userModelList, isAdmin, this, chat.getId(), chat.getLeader());
        binding.recyclerview.setAdapter(adapter);


        presenter.registerMemberListener(chat.getId());
        presenter.getMember(chat);
        binding.progressBar.setVisibility(View.VISIBLE);

    }


    private void setListener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Xử lý logic khi làm mới dữ liệu, ví dụ tải dữ liệu mới
                presenter.getMember(chat);
            }
        });
        binding.addMem.setOnClickListener(v -> {
            Intent it = new Intent(getApplicationContext(), AddMemActivity.class);
            ArrayList<String> list = new ArrayList<>();
            for (User u: userModelList) list.add(u.getId());
            it.putStringArrayListExtra("listMem", list);
            mStartForResult.launch(it);
        });
    }

    @Override
    public void deleteMember(String chatId, String id) {
        presenter.deleteMember(chatId, id);
        binding.recyclerview.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void leaveChat(String userId) {
        if (userId.equals(preferenceManager.getString(Constants.KEY_USED_ID))){
            runOnUiThread(new Runnable() {
                public void run() {
                    final Toast toast = Toast.makeText(getApplicationContext(), "Bạn vừa bị xóa khỏi một nhóm", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            MemberActivity.this.finish();
        }else{
            runOnUiThread(new Runnable() {
                public void run() {
                    final Toast toast = Toast.makeText(getApplicationContext(), "Một thành viên vừa rời khỏi một nhóm của bạn", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }

    }

    @Override
    public void onNewMemberAdded() {
        presenter.getMember(chat);
    }


    @Override
    public void onGetMemberError() {
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        binding.textErrorMessage.setText("Lỗi khi lấy dữ liệu, vui lòng thử lại!");
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetMemberSuccess() {
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        binding.textErrorMessage.setVisibility(View.GONE);
        userModelList = presenter.getUserModelList();
        adapter.notifyItemInserted(userModelList.size() - 1);
    }

    @Override
    public void onAddMemSuccess() {
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        userModelList = presenter.getUserModelList();
        adapter.notifyItemInserted(userModelList.size() - 1);
    }

    @Override
    public void onDelMemSuccess(int i) {

        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerview.setVisibility(View.VISIBLE);
        userModelList = presenter.getUserModelList();
        adapter.notifyItemRemoved(i);
    }

    @Override
    public void onAddMemEror() {
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(getApplicationContext(), "Thêm thành viên thất bại!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onDelMemEror() {
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(getApplicationContext(), "Xóa thành viên thất bại!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onMemInfoChangeSuccess(int i) {
        adapter.notifyItemChanged(i);
    }
}