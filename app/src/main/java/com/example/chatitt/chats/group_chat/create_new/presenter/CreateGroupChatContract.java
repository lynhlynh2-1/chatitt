package com.example.chatitt.chats.group_chat.create_new.presenter;

public class CreateGroupChatContract {
    public interface ViewInterface{

        void onSearchUserError();

        void onSearchUserSuccess();

        void onGetListFriendSuccess();

        void onGetListFriendError();

        void onUserClicked();

        void onNoUser();
    }
}
