package com.example.chatitt.chats.individual_chat.create_new.presenter;

import com.example.chatitt.authentication.model.User;

import java.util.List;

public class CreateChatPrivateContract {
    public interface ViewInterface{

        void onUserClicked(User userModel);

        void onSearchUserError();

        void onSearchUserSuccess(List<User> list);

        void onGetListFriendError();

        void onGetListFriendSuccess();

        void onNoUser();

        void deleteFriend(String id);

        void onDelFriendError();

        void onDelFriendSuccess(int i);
        void onFindChatSucces(String id);
        void onFindChatError();
        void onChatNotExist(User user);

        void onActionFail();

        void getListFriendEmpty();

        void onGetFriendSuccess(User user);

        void onFriendInfoChangeSuccess(int i);

        void onNoChange();
    }
}
