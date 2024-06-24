package com.example.chatitt.contacts.send_request.presenter;

public class ProfileScanUserContract {
    public interface PresenterInterface {


    }
    public interface ViewInterface {
        void getStatusFriendSuccess();

        void getStatusFriendFail();

        void setReqFriendSuccess();

        void setReqFriendFail();

        void setAcceptSuccess(String status);

        void setAcceptFail();

        void delReqFail();

        void delReqSuccess();
    }
}
