package com.example.chatitt.chats.individual_chat.create_new.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.individual_chat.create_new.presenter.CreateChatPrivateContract;
import com.example.chatitt.chats.individual_chat.create_new.presenter.CreateChatPrivatePresenter;
import com.example.chatitt.databinding.ActivityCreatePrivateChatBinding;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreatePrivateChatActivity extends AppCompatActivity implements CreateChatPrivateContract.ViewInterface {
    private ActivityCreatePrivateChatBinding binding;
    private PreferenceManager preferenceManager;
    private CreateChatPrivatePresenter createChatPrivatePresenter;
    private List<User> userModelList;
    private PrivateUserAdapter adapter;
    private boolean isFriendTab = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePrivateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);
        setupTitle();

        binding.shimmerEffect.setVisibility(View.VISIBLE);
        binding.shimmerEffect.startShimmerAnimation();
        binding.usersRecyclerView.setVisibility(View.GONE);

        preferenceManager = new PreferenceManager(this);
        createChatPrivatePresenter = new CreateChatPrivatePresenter(this, preferenceManager);

        userModelList = new ArrayList<>();
        adapter = new PrivateUserAdapter(userModelList,this, isFriendTab);

        binding.usersRecyclerView.setAdapter(adapter);

        createChatPrivatePresenter.getListFriend(null);

        setListener();
    }

    public void setupTitle(){
        binding.textView.setText(getIntent().getStringExtra("title"));
        if (Objects.equals(getIntent().getStringExtra("title"), "Danh sách bạn bè")) isFriendTab = true;
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    createChatPrivatePresenter.searchUser(binding.searchView.getQuery().toString());
                    binding.shimmerEffect.startShimmerAnimation();
                    binding.shimmerEffect.setVisibility(View.VISIBLE);
                    binding.usersRecyclerView.setVisibility(View.GONE);
                    binding.textErrorMessage.setVisibility(View.GONE);
                }else{
                    adapter.reset(userModelList);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(!TextUtils.isEmpty(newText.trim())){
                    List<User> userModels = Helpers.checkStringContain(newText, userModelList);
                    adapter.reset(userModels);
                }else{
                    adapter.reset(userModelList);
                }
                return false;
            }
        });
        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Xử lý logic khi làm mới dữ liệu, ví dụ tải dữ liệu mới
                createChatPrivatePresenter.getListFriend(null);
            }
        });
    }

    @Override
    public void onUserClicked(User userModel) {

    }

    @Override
    public void onSearchUserError() {

    }

    @Override
    public void onSearchUserSuccess() {

    }

    @Override
    public void onGetListFriendError() {

    }

    @Override
    public void onGetListFriendSuccess() {

    }

    @Override
    public void onNoUser() {

    }

    @Override
    public void deleteFriend(String id) {

    }

    @Override
    public void onDelFriendError() {

    }

    @Override
    public void onDelFriendSuccess() {

    }
}