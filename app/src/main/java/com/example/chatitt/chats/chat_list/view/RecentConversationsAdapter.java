package com.example.chatitt.chats.chat_list.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.chats.chat_list.presenter.ChatListContract;
import com.example.chatitt.databinding.ItemContainerRecentConversionBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversiongViewHolder>{
    private List<Chat> chatList;
    private final ChatListContract.ViewInterface conversionListener;
    private PreferenceManager preferenceManager;
    public RecentConversationsAdapter(List<Chat> chatList, ChatListContract.ViewInterface conversionListener, PreferenceManager preferenceManager) {
        this.chatList = chatList;
        this.conversionListener = conversionListener;
        this.preferenceManager = preferenceManager;
    }

    @NonNull
    @Override
    public RecentConversationsAdapter.ConversiongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversiongViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationsAdapter.ConversiongViewHolder holder, int position) {
        holder.setData(chatList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void reset(List<Chat> conversations) {
        chatList = conversations;
        notifyDataSetChanged();
    }

    class ConversiongViewHolder extends RecyclerView.ViewHolder {

        ItemContainerRecentConversionBinding binding;

        public ConversiongViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(Chat chat) {
            if (Objects.equals(chat.getOnline(), "1")){
                binding.imageStatus.setColorFilter(Color.rgb(0,255,0));
            }else {
                binding.imageStatus.setColorFilter(Color.rgb(255,165,0));
            }
            String content = "";
            String sender = chat.getSenderName();
            if (Objects.equals(sender, preferenceManager.getString(Constants.KEY_NAME))) sender = "Bạn";
            switch (chat.getType_msg()){
                case Constants.KEY_TYPE_TEXT:
                    content = sender + ": " + chat.getLastMessage();
                    break;
                case Constants.KEY_TYPE_IMAGE:
                    content = sender + ": Hình ảnh";
                    break;
                case Constants.KEY_TYPE_VOICE:
                    content = sender + ": Ghi âm";
                    break;
            }

            if (chat.getAvatar() != null){
                binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
            }
            if (chat.getName() != null){
                binding.textName.setText(chat.getName());
            }

            binding.textRecentMessage.setText(content);
            binding.textTime.setText(Helpers.formatTime(chat.getTimestamp(),false));

            binding.getRoot().setOnClickListener(view -> {
                conversionListener.onConversionClicked(chat);
            });
        }
    }

}
