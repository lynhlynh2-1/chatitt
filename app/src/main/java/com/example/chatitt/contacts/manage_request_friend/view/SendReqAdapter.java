package com.example.chatitt.contacts.manage_request_friend.view;

import static com.example.chatitt.ultilities.Helpers.getBitmapFromEncodedString;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.contacts.manage_request_friend.presenter.SendReqPresenter;
import com.example.chatitt.databinding.ItemSendReqFrieBinding;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.List;

public class SendReqAdapter extends RecyclerView.Adapter<SendReqAdapter.ViewHolder> {

    private List<User> userModelList;
    private SendReqPresenter presenter;
    private PreferenceManager preferenceManager;

    public SendReqAdapter(List<User> userModelList, SendReqPresenter presenter, PreferenceManager preferenceManager) {
        this.userModelList = userModelList;
        this.presenter = presenter;
        this.preferenceManager = preferenceManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSendReqFrieBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
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

        ItemSendReqFrieBinding binding;

        public ViewHolder(ItemSendReqFrieBinding itemSendReqFrieBinding) {
            super(itemSendReqFrieBinding.getRoot());
            binding = itemSendReqFrieBinding;
        }

        void setData(User userModel, int pos) {

            binding.imageProfile.setImageBitmap(getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getName());


            binding.remove.setOnClickListener(view -> {
                presenter.delReq(userModel.getId(), pos);
            });
        }
    }
}
