package com.example.chatitt.authentication.login.presenter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.chatitt.MainActivity;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInPresenter {
    private LogInContract.ViewInterface viewInterface;
    private PreferenceManager preferenceManager;
    private Context context;
    private FirebaseAuth firebaseAuth;

    public LogInPresenter(LogInContract.ViewInterface viewInterface, PreferenceManager preferenceManager, Context context) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task1) {
                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .whereEqualTo(Constants.KEY_EMAIL, email)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0 ){
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                        preferenceManager.putString(Constants.KEY_USED_ID, documentSnapshot.getId());
                                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                                        preferenceManager.putString(Constants.KEY_AVATAR, documentSnapshot.getString(Constants.KEY_AVATAR));
                                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));

                                        if (documentSnapshot.getString(Constants.KEY_PHONE) != null){
                                            preferenceManager.putString(Constants.KEY_PHONE, documentSnapshot.getString(Constants.KEY_PHONE));
                                        }
                                        if (documentSnapshot.getString(Constants.KEY_ADDRESS_CITY) != null){
                                            preferenceManager.putString(Constants.KEY_ADDRESS_CITY, documentSnapshot.getString(Constants.KEY_ADDRESS_CITY));
                                        }
                                        if (documentSnapshot.getString(Constants.KEY_ADDRESS_COUNTRY) != null){
                                            preferenceManager.putString(Constants.KEY_ADDRESS_COUNTRY, documentSnapshot.getString(Constants.KEY_ADDRESS_COUNTRY));
                                        }
                                        if (documentSnapshot.getString(Constants.KEY_ADDRESS_DETAIL) != null){
                                            preferenceManager.putString(Constants.KEY_ADDRESS_DETAIL, documentSnapshot.getString(Constants.KEY_ADDRESS_DETAIL));
                                        }
                                        viewInterface.onLoginSuccess();
                                    } else {
                                        viewInterface.onLoginWrongEmailOrPassword();
                                    }
                                });
                    }
                });
    }
}
