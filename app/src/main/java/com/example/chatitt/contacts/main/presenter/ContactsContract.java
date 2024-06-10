package com.example.chatitt.contacts.main.presenter;

public class ContactsContract {
    interface PresenterInterface {
        // Interface này dành cho SignUpPresenter
    }
    public interface ViewInterface {

        void onSearchUserError();

        void onSearchUserSuccess();
    }
}
