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
import com.example.chatitt.contacts.manage_request_friend.presenter.ReceiveReqContract;
import com.example.chatitt.contacts.manage_request_friend.presenter.ReceiveReqPresenter;
import com.example.chatitt.databinding.FragmentReceiveReqBinding;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReceiveReqFragment extends Fragment implements ReceiveReqContract.ViewInterface {
    private FragmentReceiveReqBinding binding;
    private PreferenceManager preferenceManager;
    private ReceiveReqPresenter presenter;
    private ReceiveReqAdapter adapter;
    private List<User> userModelList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceiveReqBinding.inflate(inflater, container, false);

        // Get the root view from the binding
        View rootView = binding.getRoot();
        preferenceManager = new PreferenceManager(requireContext());
        presenter = new ReceiveReqPresenter(this, preferenceManager);
        userModelList = new ArrayList<>();
        adapter = new ReceiveReqAdapter(userModelList, presenter, preferenceManager);
        binding.recyclerview.setAdapter(adapter);

        presenter.getReceiveReq();

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getReceiveReq();
            }
        });
        return rootView;
    }

    private void setListener(){

    }

    @Override
    public void getReceiveReqSuccess(User newUser) {
        binding.swipeLayout.setRefreshing(false);
        userModelList.add(newUser);
        adapter.notifyItemInserted(userModelList.size() - 1);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getReceiveReqFail() {
        binding.swipeLayout.setRefreshing(false);

        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Lấy dữ liệu fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAcceptSuccess(String status, int pos) {
        userModelList.remove(pos);
        presenter.getReceiveReq();
        Activity activity =getActivity();
        if (activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Objects.equals(status, "1")){
                        Toast.makeText(activity, "Bạn đồng ý kết bạn, hai người trở thành bạn bè của nhau!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, "Bạn từ chối kết bạn!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void setAcceptFail() {
        Activity activity =getActivity();
        if (activity != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Thao tác thất bại!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}