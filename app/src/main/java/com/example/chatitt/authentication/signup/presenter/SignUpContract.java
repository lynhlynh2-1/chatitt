package com.example.chatitt.authentication.signup.presenter;

public class SignUpContract {
    public interface ViewInterface {
        void onSignUpFail(String msg);

        void onSignUpSuccess();

    }
}
