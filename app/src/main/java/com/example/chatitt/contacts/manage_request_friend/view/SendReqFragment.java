package com.example.chatitt.contacts.manage_request_friend.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.contacts.manage_request_friend.presenter.SendReqContract;
import com.example.chatitt.contacts.manage_request_friend.presenter.SendReqPresenter;
import com.example.chatitt.databinding.FragmentSendReqBinding;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class SendReqFragment extends Fragment implements SendReqContract.ViewInterface {

    private FragmentSendReqBinding binding;
    private PreferenceManager preferenceManager;
    private SendReqPresenter presenter;
    private SendReqAdapter adapter;
    private List<User> userModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSendReqBinding.inflate(inflater, container, false);

        // Get the root view from the binding
        View rootView = binding.getRoot();
        preferenceManager = new PreferenceManager(requireContext());
        presenter = new SendReqPresenter(this, preferenceManager);
        userModelList = new ArrayList<>();
        adapter = new SendReqAdapter(userModelList, presenter, preferenceManager);
        binding.recyclerview.setAdapter(adapter);
//        User user = new User();
//        user.setAvatar("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");
//        user.setName("Han");
//        userModelList.add(user);


        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getSendReq();
            }
        });
        presenter.getSendReq();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void getSendReqSuccess(User tempUser) {
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        binding.textErrorMessage.setVisibility(View.GONE);
        userModelList.add(tempUser);
        adapter.notifyItemInserted(userModelList.size() - 1);

    }

    @Override
    public void getSendReqFail() {
        binding.swipeLayout.setRefreshing(false);
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Lấy dữ liệu fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delReqFail() {
        Activity activity = getActivity();
        if (activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(activity, "Thao tác thất bại!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void delReqSuccess(int pos) {
        presenter.getSendReq();
        userModelList.remove(pos);
        Activity activity =getActivity();
        if (activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(activity, "Xóa lời mời kết bạn thành công!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onUserInfoChangeSuccess(int i) {
        binding.progressBar.setVisibility(View.GONE);
        adapter.notifyItemChanged(i);
    }

    @Override
    public void onDelReqSuccess(int i) {
        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerview.setVisibility(View.VISIBLE);
        userModelList.remove(i);
        adapter.notifyItemRemoved(i);
    }

    @Override
    public void onGetSendReqError() {
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        binding.swipeLayout.setRefreshing(false);
        binding.textErrorMessage.setText("Lỗi khi lấy dữ liệu, vui lòng thử lại!");
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
}