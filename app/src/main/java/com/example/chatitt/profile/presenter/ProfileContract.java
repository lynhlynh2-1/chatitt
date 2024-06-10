package com.example.chatitt.profile.presenter;

public class ProfileContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {
        // Interface này dành cho SignUpActivity

        void onEditProfileSuccess(String type);
        void onEditProfileError();

        void onGetProfileSuccess();
        void onGetProfileError();
    }
}
