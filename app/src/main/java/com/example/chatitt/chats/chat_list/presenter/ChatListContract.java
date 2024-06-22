package com.example.chatitt.chats.chat_list.presenter;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;

public class ChatListContract {
    public interface ViewInterface {
        void onConversionClicked(Chat chat);

        void onAddNewChatClick();

        void onRecentUserChatClick(User userModel);

        void onGetMessagedSuccess();

        void onGetMessagedError();

        void onNewChatCreate();

        void receiveNewMsgRealtime();

        void getMessaged(String userId);

        void isAddedChat();

        void updateChatRealtime(int count);

        void onNewMemberAdded();
    }
}
