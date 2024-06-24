package com.example.chatitt.chats.group_chat.create_new.presenter;

import com.example.chatitt.authentication.model.User;

import java.util.List;

public class CreateGroupChatContract {
    public interface ViewInterface{

        void onSearchUserError();

        void onSearchUserSuccess(List<User> usersFind);

        void onGetListFriendSuccess();

        void onGetListFriendError();

        void onUserClicked();

        void onNoUser();
    }
}
