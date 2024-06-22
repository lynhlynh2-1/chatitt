package com.example.chatitt.chats.group_chat.create_new.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.group_chat.create_new.presenter.CreateGroupChatContract;
import com.example.chatitt.chats.group_chat.create_new.presenter.CreateGroupChatPresenter;
import com.example.chatitt.databinding.ActivityCreateGroupChatBinding;
import com.example.chatitt.R;
import com.example.chatitt.chats.chat.view.ChatActivity;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CreateGroupChatActivity extends AppCompatActivity implements CreateGroupChatContract.ViewInterface {


    private ActivityCreateGroupChatBinding binding;
    private PreferenceManager preferenceManager;
    private CreateGroupChatPresenter createGroupChatPresenter;

    private List<User> chosenList;
    private List<User> userModelList;

    private GroupUserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);


        binding.shimmerEffect.setVisibility(View.VISIBLE);
        binding.shimmerEffect.startShimmerAnimation();
        binding.usersRecyclerView.setVisibility(View.GONE);


        preferenceManager = new PreferenceManager(this);
        createGroupChatPresenter = new CreateGroupChatPresenter(this, preferenceManager);

        createGroupChatPresenter.getListFriend(null);

        chosenList = new ArrayList<>();
        userModelList = new ArrayList<>();
//        for (int i = 0; i < 20; i++){
//            UserModel u = new UserModel(i+"","name" + i);
//            userModelList.add(u);
//        }
        adapter = new GroupUserAdapter(userModelList,this);

        binding.usersRecyclerView.setAdapter(adapter);


        setListener();

    }

    private void setListener(){
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });
        binding.btnCreateGroup.setOnClickListener(v->{

            chosenList = adapter.getChosenList();

//            Log.d("HP","Start chose");
//            for (UserModel userModel: chosenList) {
//                Log.d("HP",userModel.getUsername());
//            }
//            Log.d("HP","End chose");

            Intent intent = new Intent(this,ChatActivity.class);
            List<String> list = new ArrayList<>();
            for (User u : chosenList){
                list.add(u.getId());
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("create_group_chat", new ArrayList<>(list));
            intent.putExtras(bundle);
            intent.putExtra(Constants.KEY_NAME, binding.edtCreateGroup.getText().toString().trim());
            startActivity(intent);
            finish();
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    createGroupChatPresenter.searchUser(binding.searchView.getQuery().toString());
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
//                    List<Boolean> isCheckeList = new ArrayList<Boolean>(Arrays.asList(new Boolean[userModels.size()]));
//                    Collections.fill(isCheckeList, false);
//                    int cnt = 0;
//                    for (int i = 0 ; i < userModels.size(); i++){
//                        UserModel u = userModels.get(i);
//                        if (chosenList.contains(u)){
//                            cnt ++;
//                            isCheckeList.set(i,true);
//                        }
//                        if (cnt >= chosenList.size()) break;
//                    }
                    adapter.reset(userModels);
                }else{
                    adapter.reset(userModelList);

                }
                return false;
            }
        });


        binding.edtCreateGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = !TextUtils.isEmpty(s);
                binding.btnCreateGroup.setEnabled(hasText && adapter.getChosenList().size() > 1);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Xử lý logic khi làm mới dữ liệu, ví dụ tải dữ liệu mới
                binding.shimmerEffect.startShimmerAnimation();
                binding.shimmerEffect.setVisibility(View.VISIBLE);
                createGroupChatPresenter.getListFriend(null);
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
    public void onSearchUserSuccess() {
        List<User> list = createGroupChatPresenter.getUserModelList();
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        if (list.size() != 0){
            List<User> chosen_list = adapter.getChosenList();
            int cnt = chosen_list.size();
            for (User u: list) {
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

            adapter.reset(list);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onGetListFriendSuccess() {

        userModelList = createGroupChatPresenter.getUserModelList();
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        if (userModelList.size() != 0){
            adapter.reset(userModelList);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.textErrorMessage.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setText("Danh sách bạn bè trống!");
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
        if (adapter.getChosenList().size() > 1){
            binding.btnCreateGroup.setEnabled(!binding.edtCreateGroup.getText().toString().trim().isEmpty());
        }else
            binding.btnCreateGroup.setEnabled(false);

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
}