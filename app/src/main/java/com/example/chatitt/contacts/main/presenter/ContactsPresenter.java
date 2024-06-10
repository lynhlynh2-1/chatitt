package com.example.chatitt.contacts.main.presenter;

import com.example.chatitt.authentication.model.User;

public class ContactsPresenter {
    private ContactsContract.ViewInterface viewInterface;
    private String token;

    User userModels;

    public User getUserModels() {
        return userModels;
    }

    public ContactsPresenter(ContactsContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public void searchUser(String keyword){

    }
}
