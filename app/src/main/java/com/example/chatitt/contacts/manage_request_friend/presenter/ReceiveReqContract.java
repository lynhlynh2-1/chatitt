package com.example.chatitt.contacts.manage_request_friend.presenter;

import com.example.chatitt.authentication.model.User;

public class ReceiveReqContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {
        void getReceiveReqSuccess(User newUser);
        void getReceiveReqFail();
        void getReceiveReqEmpty();
        void setAcceptSuccess(String status, int pos);

        void setAcceptFail();

        void onDelReqSuccess(int i);

        void onUserInfoChangeSuccess(int i);

        void onDelReqFailure();
    }
}
