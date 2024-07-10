package com.example.chatitt.contacts.manage_request_friend.view;

import static com.example.chatitt.ultilities.Helpers.getBitmapFromEncodedString;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.contacts.manage_request_friend.presenter.ReceiveReqPresenter;
import com.example.chatitt.databinding.ItemReceiveReqFrieBinding;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.List;

public class ReceiveReqAdapter extends RecyclerView.Adapter<ReceiveReqAdapter.ViewHolder>{

    private List<User> userModelList;
    private ReceiveReqPresenter presenter;
    private PreferenceManager preferenceManager;

    public ReceiveReqAdapter(List<User> userModelList, ReceiveReqPresenter presenter, PreferenceManager preferenceManager) {
        this.userModelList = userModelList;
        this.presenter = presenter;
        this.preferenceManager = preferenceManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemReceiveReqFrieBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(userModelList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemReceiveReqFrieBinding binding;

        public ViewHolder(ItemReceiveReqFrieBinding itemReceiveReqFrieBinding) {
            super(itemReceiveReqFrieBinding.getRoot());
            binding = itemReceiveReqFrieBinding;
        }

        void setData(User userModel, int pos) {

            binding.imageProfile.setImageBitmap(getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getName());

            binding.remove.setOnClickListener(view -> {
                presenter.setDeclineFriend(userModel.getId(),"2", pos);

            });
            binding.accept.setOnClickListener(view -> {
                presenter.setAcceptFriend(userModel.getId(),"1", pos);

            });

        }
    }
}
