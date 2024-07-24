package com.example.chatitt.contacts.main.presenter;

import com.example.chatitt.authentication.model.User;

public class ContactsContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {

        void onSearchUserError();

        void onSearchUserSuccess();

        void onUserUpdate(User user);
    }
}
