package com.example.chatitt.chats.chat_list.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.presenter.ChatListContract;
import com.example.chatitt.databinding.ItemContainer0FastBinding;
import com.example.chatitt.databinding.ItemContainerFastBinding;
import com.example.chatitt.ultilities.Helpers;

import java.util.List;
import java.util.Objects;

public class RecentUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int VIEW_TYPE_1 = 1;
    public static final int VIEW_TYPE_2 = 2;
    private final List<User> userModelList;
    private final ChatListContract.ViewInterface viewInterface;

    public RecentUserAdapter(List<User> userModelList, ChatListContract.ViewInterface viewInterface) {
        this.userModelList = userModelList;
        this.viewInterface = viewInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_1) {
            return new CreateNewChatViewHolder(
                    ItemContainer0FastBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }else {
            return new RecentUserViewHolder(
                    ItemContainerFastBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_2){
            ((RecentUserViewHolder) holder).setData(userModelList.get(position - 1));
        }else {
            ((CreateNewChatViewHolder) holder).setData();

        }
    }

    @Override
    public int getItemCount() {
        return userModelList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return VIEW_TYPE_1;
        }else {
            return VIEW_TYPE_2;
        }
    }

    class CreateNewChatViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainer0FastBinding binding;
        public CreateNewChatViewHolder( ItemContainer0FastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(){
            binding.getRoot().setOnClickListener(v->{
                viewInterface.onAddNewChatClick();
            });
        }
    }

    class RecentUserViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerFastBinding binding;
        public RecentUserViewHolder( ItemContainerFastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(User userModel){
            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.imageStatus.setImageResource(Objects.equals(userModel.getOnline(), "1") ?  R.drawable.background_online : R.drawable.background_offline);
            binding.getRoot().setOnClickListener(v->{
                viewInterface.onRecentUserChatClick(userModel);
            });
        }
    }
}
