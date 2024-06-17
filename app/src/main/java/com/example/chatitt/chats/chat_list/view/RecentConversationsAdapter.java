package com.example.chatitt.chats.chat_list.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.presenter.ChatListContract;
import com.example.chatitt.databinding.ItemContainerRecentConversionBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversiongViewHolder>{
    private List<Chat> chatMessages;
    private final ChatListContract.ViewInterface conversionListener;
    private PreferenceManager preferenceManager;
    public RecentConversationsAdapter(List<Chat> chatMessages, ChatListContract.ViewInterface conversionListener, PreferenceManager preferenceManager) {
        this.chatMessages = chatMessages;
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
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void reset(List<Chat> conversations) {
        chatMessages = conversations;
        notifyDataSetChanged();
    }

    class ConversiongViewHolder extends RecyclerView.ViewHolder {

        ItemContainerRecentConversionBinding binding;

        public ConversiongViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(Chat chatMessage) {
            if (Objects.equals(chatMessage.getOnline(), "1")){
                binding.imageStatus.setColorFilter(Color.rgb(0,255,0));
            }else {
                binding.imageStatus.setColorFilter(Color.rgb(255,165,0));
            }

            SecretKey secretKey;

            String content;
            String sender = chatMessage.getLastMessage().getUser().getName();

            if (Objects.equals(sender, preferenceManager.getString(Constants.KEY_NAME))) sender = "Bạn";
            if (Objects.equals(chatMessage.getLastMessage().getType(), Constants.KEY_TYPE_IMAGE)){

                content = sender + ": Hình ảnh";

            }else {
                content = sender + ": " + chatMessage.getLastMessage().getContent();
            }

            binding.imageProfile.setImageBitmap(getConversionImage(chatMessage.getAvatar()));
            binding.textName.setText(chatMessage.getName());
            binding.textRecentMessage.setText(content);
            binding.textTime.setText(Helpers.formatTime(chatMessage.getLastMessage().getUpdatedAt(),false));

            binding.getRoot().setOnClickListener(view -> {
                conversionListener.onConversionClicked(chatMessage);
            });
        }
    }

    private Bitmap getConversionImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }
}
