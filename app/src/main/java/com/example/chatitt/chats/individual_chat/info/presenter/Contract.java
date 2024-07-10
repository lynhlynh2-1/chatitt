package com.example.chatitt.chats.individual_chat.info.presenter;

public class Contract {
    public interface FriendStatusInterface{

        void getStatusFriendSuccess();

        void getStatusFriendFail();

        void onActionFail();
    }
    public interface ProfileDetailInterface{
        void getInfoDetailSuccess();

        void getInfoDetailFail();
    }

    public interface ChatInfoInterface{

        void onDelMemEror();

        void onDelGroupSuccess();
    }
}
