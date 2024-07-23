package com.example.chatitt.chats.chat.presenter;

import com.example.chatitt.chats.chat_list.model.Chat;

import java.util.Map;

public class ChatContract {
    public interface ViewInterface{
        void onChatNotExist();
        void onUpdateInforSuccess(String name, String avatar);
        void onUpdateOnlineStatus(Boolean status);
        void onGetMessagesSuccess();
        void onGetMessagesError();
        void onFindChatError();
        void onSendError(int pos);
        void onSendSuccess(String content, String typeMess, int pos);
        void onCreateAndSendSuccess(String content, String typeMess, int pos);
        void onShowImageClick(String content);
        void onInfoChatChanged(Chat chat);
        void updateMessList(int count);

        void onUpdateFcmOrInchat(Map<String, String> fcm, Map<String, Object> inchat);
    }
}
