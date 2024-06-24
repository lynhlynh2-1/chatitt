package com.example.chatitt.chats.group_chat.info.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.group_chat.info.presenter.AddMemPresenter;
import com.example.chatitt.chats.group_chat.info.presenter.Contract;
import com.example.chatitt.databinding.ActivityAddMemBinding;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddMemActivity extends AppCompatActivity implements Contract.AddMemViewInterface {


    private ActivityAddMemBinding binding;
    private PreferenceManager preferenceManager;
    private AddMemPresenter addMemPresenter;
    private List<User> chosenList;
    private List<User> userModelList;
    ArrayList<String> listMember;
    private AddMemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);


        binding.shimmerEffect.setVisibility(View.VISIBLE);
        binding.shimmerEffect.startShimmerAnimation();
        binding.usersRecyclerView.setVisibility(View.GONE);


        preferenceManager = new PreferenceManager(this);
        addMemPresenter = new AddMemPresenter(this, preferenceManager);
        listMember = getIntent().getStringArrayListExtra("listMem");

        addMemPresenter.getListFriend(null);

        chosenList = new ArrayList<>();
        userModelList = new ArrayList<>();
//        for (int i = 0; i < 20; i++){
//            UserModel u = new UserModel(i+"","name" + i);
//            userModelList.add(u);
//        }
        adapter = new AddMemAdapter(userModelList,this);

        binding.usersRecyclerView.setAdapter(adapter);


        setListener();

    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.btnAdd.setOnClickListener(v->{
            chosenList = adapter.getChosenList();

            ArrayList<String> list = new ArrayList<>();
            for (User u : chosenList){
                list.add(u.getId());
            }
            Intent data = new Intent();
            data.putStringArrayListExtra("listMemberAdded", list);
            setResult(RESULT_OK, data);
            finish();
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    addMemPresenter.searchUser(binding.searchView.getQuery().toString());
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
                binding.shimmerEffect.startShimmerAnimation();
                binding.shimmerEffect.setVisibility(View.VISIBLE);
                addMemPresenter.getListFriend(null);
            }
        });
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
    public void onSearchUserSuccess(List<User> usersFind) {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        if (usersFind.size() != 0){
            List<User> chosen_list = adapter.getChosenList();
            int cnt = chosen_list.size();
            for (User u: usersFind) {
                for (User c: chosen_list) {
                    if (Objects.equals(u.getPhonenumber(), c.getPhonenumber())){
                        u.setChecked(true);
                        cnt --;
                        break;
                    }
                }
                if (cnt <= 0){
                    break;
                }
            }
            usersFind.removeIf(user -> listMember.contains(user.getId()));
            adapter.reset(usersFind);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoUser() {
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.textErrorMessage.setText("Không tìm thấy dữ liệu!");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_onSurfaceVariant);
        binding.textErrorMessage.setTextColor(colorEror);
    }

    @Override
    public void onGetListFriendSuccess() {

        userModelList = addMemPresenter.getUserModelList();
        userModelList.removeIf(user -> listMember.contains(user.getId()));
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        if (userModelList.size() != 0){
            adapter.reset(userModelList);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Danh sách bạn bè trống hoặc tất cả bạn bè đã là thành viên nhóm!");
        }
    }

    @Override
    public void onGetListFriendError() {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        binding.swipeLayout.setRefreshing(false);
        binding.textErrorMessage.setText("Tải danh sách bạn bè gặp lỗi");
        int colorEror = ContextCompat.getColor(getApplicationContext(), R.color.md_theme_light_error);
        binding.textErrorMessage.setTextColor(colorEror);
    }

    @Override
    public void onUserClicked() {
        binding.btnAdd.setEnabled(adapter.getChosenList().size() > 0);
    }

    void removeMemberList(List<User> userList){

    }


}