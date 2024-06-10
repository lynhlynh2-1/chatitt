package com.example.chatitt.chats.individual_chat.create_new.presenter;

import com.example.chatitt.authentication.model.User;

public class CreateChatPrivateContract {
    public interface ViewInterface{

        void onUserClicked(User userModel);

        void onSearchUserError();

        void onSearchUserSuccess();

        void onGetListFriendError();

        void onGetListFriendSuccess();

        void onNoUser();

        void deleteFriend(String id);

        void onDelFriendError();

        void onDelFriendSuccess();
    }
}
