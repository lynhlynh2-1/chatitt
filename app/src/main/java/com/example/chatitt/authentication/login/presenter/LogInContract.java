package com.example.chatitt.authentication.login.presenter;

public class LogInContract {
    public interface ViewInterface {
        // Interface này dành cho LoginActivity
        void onLoginSuccess();
        void onLoginWrongEmailOrPassword();

        void onConnectFail();
    }
}
