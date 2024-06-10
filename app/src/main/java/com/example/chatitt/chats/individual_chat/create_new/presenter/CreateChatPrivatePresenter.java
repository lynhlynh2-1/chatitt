package com.example.chatitt.chats.individual_chat.create_new.presenter;

import android.net.http.HttpException;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class CreateChatPrivatePresenter {
    private final CreateChatPrivateContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private List<User> userModelList;
    public CreateChatPrivatePresenter(CreateChatPrivateContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
    }

    public void searchUser (String keyword){

    }

    public void getListFriend (String user_id){

    }

    public void deleteFriend(String id) {

    }
}
