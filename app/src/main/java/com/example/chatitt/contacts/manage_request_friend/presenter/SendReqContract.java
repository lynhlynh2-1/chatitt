package com.example.chatitt.contacts.manage_request_friend.presenter;

import com.example.chatitt.authentication.model.User;

public class SendReqContract {
    interface PresenterInterface {

    }
    public interface ViewInterface {
        void getSendReqSuccess(User tempUser);
        void getSendReqFail();

        void delReqFail();

        void delReqSuccess(int pos);

        void onUserInfoChangeSuccess(int i);

        void onDelReqSuccess(int i);


        void onGetSendReqError();
    }
}
