package com.example.chatitt.chats.chat.view;

import static com.example.chatitt.ultilities.Helpers.getBitmapFromEncodedString;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.chats.chat.presenter.ChatContract;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.databinding.ItemContainerReceivedMessageBinding;
import com.example.chatitt.databinding.ItemContainerSentMessageBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> chatMessages;
    private final String senderId;
    private final String type;
    private ChatContract.ViewInterface viewInterface;

    public ChatAdapter(List<Message> chatMessages, String senderId, String type, ChatContract.ViewInterface viewInterface) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
        this.type = type;
        this.viewInterface = viewInterface;
    }
    public void reset(List<Message> messageList) {
        chatMessages = messageList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position), position);

        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSenderId().equals(senderId)){
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public void addData() {
        notifyItemRangeInserted(chatMessages.size(),chatMessages.size());

    }

    public void resetData(List<Message> messageList) {
        chatMessages = messageList;
    }

    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(Message chatMessage, int position) {
            String content = chatMessage.getContent();

//            if (Objects.equals(chatMessage.getType_msg(), Constants.KEY_TYPE_TEXT)){
//                binding.textMessage.setText(content);
//            }else {
//                binding.textMessage.setVisibility(View.GONE);
//                binding.imgChat.setVisibility(View.VISIBLE);
////                binding.imgChat.setImageBitmap(getBitmapFromEncodedString(content));
//                Picasso.get().load(Uri.parse(content)).into(binding.imgChat);
//                binding.imgChat.setOnClickListener(view -> {
//                    viewInterface.onShowImageClick(content);
//                });
//            }

            switch (chatMessage.getType_msg()){
                case Constants.KEY_TYPE_TEXT:
                    binding.textMessage.setText(content);
                    break;
                case Constants.KEY_TYPE_IMAGE:
                    binding.textMessage.setVisibility(View.GONE);
                    binding.imgChat.setVisibility(View.VISIBLE);
//                    binding.imgChat.setImageBitmap(getBitmapFromEncodedString(content));
                    Picasso.get().load(Uri.parse(content)).into(binding.imgChat);
                    binding.imgChat.setOnClickListener(view -> {
                        viewInterface.onShowImageClick(content);
                    });
                    break;
                case Constants.KEY_TYPE_VOICE:
                    binding.textMessage.setVisibility(View.GONE);
                    binding.voicePlayerView.setVisibility(View.VISIBLE);
                    binding.voicePlayerView.setAudio(content);
                    break;
            }
            if (Objects.equals(chatMessage.getIsSending(), "2")){
                binding.textTime.setVisibility(View.VISIBLE);
                binding.textTime.setText("Đã gửi");
                binding.textTime.setText(Helpers.formatTime(chatMessage.getTimestamp(),true));
                binding.textTime.setVisibility(View.GONE);
            }else if (Objects.equals(chatMessage.getIsSending(), "1")){
                binding.textTime.setVisibility(View.VISIBLE);
                binding.textTime.setText("Đang gửi");
            }else if (Objects.equals(chatMessage.getIsSending(), "0")){
                binding.textTime.setVisibility(View.VISIBLE);
                binding.textTime.setText("Gửi thất bại");
            }else {
                binding.textTime.setText(Helpers.formatTime(chatMessage.getTimestamp(),true));
                binding.textTime.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v->{
                if(binding.textTime.getVisibility() == View.VISIBLE){
                    binding.textTime.setVisibility(View.GONE);
                } else {
                    binding.textTime.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMessageBinding binding;

        public ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }


        void setData(Message chatMessage) {

            String content;

            content = chatMessage.getContent();
            binding.textName.setText(chatMessage.getSenderName());
            binding.textName.setVisibility(View.VISIBLE);

            switch (chatMessage.getType_msg()){
                case Constants.KEY_TYPE_TEXT:
                    binding.textMessage.setText(content);
                    break;
                case Constants.KEY_TYPE_IMAGE:
                    binding.textMessage.setVisibility(View.GONE);
                    binding.imgChat.setVisibility(View.VISIBLE);
//                    binding.imgChat.setImageBitmap(getBitmapFromEncodedString(content));
                    Picasso.get().load(Uri.parse(content)).into(binding.imgChat);
                    binding.imgChat.setOnClickListener(view -> {
                        viewInterface.onShowImageClick(content);
                    });
                    break;
                case Constants.KEY_TYPE_VOICE:
                    binding.textMessage.setVisibility(View.GONE);
                    binding.voicePlayerView.setVisibility(View.VISIBLE);
                    binding.voicePlayerView.setAudio(content);
                    break;
            }

            binding.imageProfile.setImageBitmap(getBitmapFromEncodedString(chatMessage.getSenderImage()));
            if (Objects.equals(chatMessage.getIsSending(), "2")){
                binding.textTime.setVisibility(View.VISIBLE);
                binding.textTime.setText("Đã gửi");
                binding.textTime.setText(Helpers.formatTime(chatMessage.getTimestamp(),true));
                binding.textTime.setVisibility(View.GONE);
            }else if (Objects.equals(chatMessage.getIsSending(), "1")){
                binding.textTime.setVisibility(View.VISIBLE);
                binding.textTime.setText("Đang gửi");
            }else if (Objects.equals(chatMessage.getIsSending(), "0")){
                binding.textTime.setVisibility(View.VISIBLE);
                binding.textTime.setText("Gửi thất bại");
            }else {
                binding.textTime.setText(Helpers.formatTime(chatMessage.getTimestamp(),true));
                binding.textTime.setVisibility(View.GONE);
            }
            binding.getRoot().setOnClickListener(v->{
                if(binding.textTime.getVisibility() == View.VISIBLE){
                    binding.textTime.setVisibility(View.GONE);
                } else {
                    binding.textTime.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
