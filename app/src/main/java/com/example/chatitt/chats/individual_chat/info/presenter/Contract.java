package com.example.chatitt.chats.individual_chat.info.presenter;

public class Contract {
    public interface FriendStatusInterface{

        void getStatusFriendSuccess();

        void getStatusFriendFail();
    }
    public interface ProfileDetailInterface{
        void getInfoDetailSuccess();

        void getInfoDetailFail();
    }
}
