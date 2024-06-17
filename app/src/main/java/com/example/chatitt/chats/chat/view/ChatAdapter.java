package com.example.chatitt.chats.chat.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.chats.chat.presenter.ChatContract;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;

import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

public class ChatAdapter {
//    public static final int VIEW_TYPE_SENT = 1;
//    public static final int VIEW_TYPE_RECEIVED = 2;
//
//    private List<Message> chatMessages;
//    private final String senderId;
//    private final String type;
//    private SecretKey secretKey;
//    private ChatContract.ViewInterface viewInterface;
//
//    public ChatAdapter(List<Message> chatMessages, String senderId, String type, SecretKey secretKey, ChatContract.ViewInterface viewInterface) {
//        this.chatMessages = chatMessages;
//        this.senderId = senderId;
//        this.type = type;
//        this.secretKey = secretKey;
//        this.viewInterface = viewInterface;
//    }
//    public void reset(List<Message> messageList) {
//        chatMessages = messageList;
//        notifyDataSetChanged();
//    }
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_SENT) {
//            return new SentMessageViewHolder(
//                    ItemContainerSentMessageBinding.inflate(
//                            LayoutInflater.from(parent.getContext()),
//                            parent,
//                            false
//                    )
//            );
//        }else {
//            return new ReceivedMessageViewHolder(
//                    ItemContainerReceivedMessageBinding.inflate(
//                            LayoutInflater.from(parent.getContext()),
//                            parent,
//                            false
//                    )
//            );
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (getItemViewType(position) == VIEW_TYPE_SENT){
//            ((SentMessageViewHolder) holder).setData(chatMessages.get(position), position);
//
//        }else {
//            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return chatMessages.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (chatMessages.get(position).getUser().getId().equals(senderId)){
//            return VIEW_TYPE_SENT;
//        }else {
//            return VIEW_TYPE_RECEIVED;
//        }
//    }
//
//    public void addData() {
//        notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
//
//    }
//
//    public void resetData(List<Message> messageList) {
//        chatMessages = messageList;
//    }
//
//    class SentMessageViewHolder extends RecyclerView.ViewHolder {
//        private final ItemContainerSentMessageBinding binding;
//
//        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
//            super(itemContainerSentMessageBinding.getRoot());
//            binding = itemContainerSentMessageBinding;
//        }
//
//        void setData(Message chatMessage, int position) {
//            String content;
//
//            if (Objects.equals(type, Constants.KEY_GROUP_CHAT)){
//                content = chatMessage.getContent();
//            }else {
//                content = ECCc.decryptString(secretKey, chatMessage.getContent());
//            }
//
//            if (Objects.equals(chatMessage.getType(), Constants.KEY_TYPE_TEXT)){
//                binding.textMessage.setText(content);
//            }else {
//                binding.textMessage.setVisibility(View.GONE);
//                binding.imgChat.setVisibility(View.VISIBLE);
//                binding.imgChat.setImageBitmap(Helpers.getBitmapFromEncodedString(content));
//                binding.imgChat.setOnClickListener(view -> {
//                    viewInterface.onShowImageClick(content);
//                });
//
//            }
//
//            if (Objects.equals(chatMessage.isSending(), "2")){
//                binding.textTime.setVisibility(View.VISIBLE);
//                binding.textTime.setText("Đã gửi");
//                binding.textTime.setText(Helpers.formatTime(chatMessage.getUpdatedAt(),true));
//                binding.textTime.setVisibility(View.GONE);
//            }else if (Objects.equals(chatMessage.isSending(), "1")){
//                binding.textTime.setVisibility(View.VISIBLE);
//                binding.textTime.setText("Đang gửi");
//            }else if (Objects.equals(chatMessage.isSending(), "0")){
//                binding.textTime.setVisibility(View.VISIBLE);
//                binding.textTime.setText("Gửi thất bại");
//            }else {
//                binding.textTime.setText(Helpers.formatTime(chatMessage.getUpdatedAt(),true));
//                binding.textTime.setVisibility(View.GONE);
//            }
//
//            binding.getRoot().setOnClickListener(v->{
//                if(binding.textTime.getVisibility() == View.VISIBLE){
//                    binding.textTime.setVisibility(View.GONE);
//                } else {
//                    binding.textTime.setVisibility(View.VISIBLE);
//                }
//            });
//
//        }
//    }
//
//    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
//        private final ItemContainerReceivedMessageBinding binding;
//
//        public ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
//            super(itemContainerReceivedMessageBinding.getRoot());
//            binding = itemContainerReceivedMessageBinding;
//        }
//
//
//        void setData(Message chatMessage) {
//
//            String content;
//
//            if (Objects.equals(type, Constants.KEY_GROUP_CHAT)){
//                content = chatMessage.getContent();
//                binding.textName.setText(chatMessage.getUser().getUsername());
//                binding.textName.setVisibility(View.VISIBLE);
//            }else {
//                content = ECCc.decryptString(secretKey, chatMessage.getContent());
//            }
//
//            if (Objects.equals(chatMessage.getType(), Constants.KEY_TYPE_TEXT)){
//                binding.textMessage.setText(content);
//            }else {
//                binding.textMessage.setVisibility(View.GONE);
//                binding.imgChat.setVisibility(View.VISIBLE);
//                binding.imgChat.setImageBitmap(Helpers.getBitmapFromEncodedString(content));
//                binding.imgChat.setOnClickListener(view -> {
//                    viewInterface.onShowImageClick(content);
//                });
//            }
//
//            binding.imageProfile.setImageBitmap(getBitmapFromEncodedString(chatMessage.getUser().getAvatar()));
//            if (Objects.equals(chatMessage.isSending(), "2")){
//                binding.textTime.setVisibility(View.VISIBLE);
//                binding.textTime.setText("Đã gửi");
//                binding.textTime.setText(Helpers.formatTime(chatMessage.getUpdatedAt(),true));
//                binding.textTime.setVisibility(View.GONE);
//            }else if (Objects.equals(chatMessage.isSending(), "1")){
//                binding.textTime.setVisibility(View.VISIBLE);
//                binding.textTime.setText("Đang gửi");
//            }else if (Objects.equals(chatMessage.isSending(), "0")){
//                binding.textTime.setVisibility(View.VISIBLE);
//                binding.textTime.setText("Gửi thất bại");
//            }else {
//                binding.textTime.setText(Helpers.formatTime(chatMessage.getUpdatedAt(),true));
//                binding.textTime.setVisibility(View.GONE);
//            }
//            binding.getRoot().setOnClickListener(v->{
//                if(binding.textTime.getVisibility() == View.VISIBLE){
//                    binding.textTime.setVisibility(View.GONE);
//                } else {
//                    binding.textTime.setVisibility(View.VISIBLE);
//                }
//            });
//        }
//    }
}
