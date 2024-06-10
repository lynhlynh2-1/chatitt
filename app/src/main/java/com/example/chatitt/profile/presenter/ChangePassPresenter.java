package com.example.chatitt.profile.presenter;

import android.content.Context;

import com.example.chatitt.ultilities.PreferenceManager;

public class ChangePassPresenter {
    
    private final ChangePassContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private Context context;

    public ChangePassPresenter(ChangePassContract.ViewInterface viewInterface, PreferenceManager preferenceManager, Context context) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        this.context = context;
    }

    public void changePass(String currPass, String newPass) {

    }
}
