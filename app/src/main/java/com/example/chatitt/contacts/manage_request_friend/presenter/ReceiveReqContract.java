package com.example.chatitt.contacts.manage_request_friend.presenter;

public class ReceiveReqContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {
        void getReceiveReqSuccess();
        void getReceiveReqFail();

        void setAcceptSuccess(String status, int pos);

        void setAcceptFail();
    }
}
