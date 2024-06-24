package com.example.chatitt.contacts.manage_request_friend.presenter;

public class SendReqContract {
    interface PresenterInterface {

    }
    public interface ViewInterface {
        void getSendReqSuccess();
        void getSendReqFail();

        void delReqFail();

        void delReqSuccess(int pos);
    }
}
