package com.example.chatitt.chats.individual_chat.create_new.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat.view.ChatActivity;
import com.example.chatitt.chats.individual_chat.create_new.presenter.CreateChatPrivateContract;
import com.example.chatitt.chats.individual_chat.create_new.presenter.CreateChatPrivatePresenter;
import com.example.chatitt.databinding.ActivityCreatePrivateChatBinding;
import com.example.chatitt.ultilities.Constants;
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
        binding.loading.setVisibility(View.VISIBLE);
        createChatPrivatePresenter.findChat(userModel);
    }

    @Override
    public void onFindChatSucces(String id) {
        binding.loading.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_COLLECTION_CHAT, createChatPrivatePresenter.getChat());
        intent.putExtra(Constants.KEY_RECEIVER_ID, id);
        intent.putExtra(Constants.KEY_RECEIVER_IMAGE, id);
        intent.putExtra(Constants.KEY_RECEIVER_NAME, id);
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
    public void onSearchUserError() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.textErrorMessage.setText("Tìm kiếm gặp lỗi");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_error);
        binding.textErrorMessage.setTextColor(colorEror);
    }

    @Override
    public void onSearchUserSuccess() {
        binding.swipeLayout.setRefreshing(false);

        List<User> list = createChatPrivatePresenter.getUserModelList();
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        if (list.size() != 0){
            adapter.reset(list);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Không tìm thấy dữ liệu!");
        }
    }

    @Override
    public void onGetListFriendError() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.textErrorMessage.setText("Tải danh sách bạn bè gặp lỗi");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_error);
        binding.textErrorMessage.setTextColor(colorEror);
    }

    @Override
    public void onGetListFriendSuccess() {
        userModelList = createChatPrivatePresenter.getUserModelList();
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        if (userModelList.size() != 0){
            adapter.reset(userModelList);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Danh sách bạn bè trống!");
        }
    }

    @Override
    public void onNoUser() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.textErrorMessage.setText("Không tìm thấy dữ liệu!");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_onSurfaceVariant);
        binding.textErrorMessage.setTextColor(colorEror);
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