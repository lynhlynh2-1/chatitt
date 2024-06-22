package com.example.chatitt.authentication.signup.presenter;

import static com.example.chatitt.ultilities.Helpers.showToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.MainActivity;
import com.example.chatitt.authentication.signup.view.SignUpFragment;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Objects;

public class SignUpPresenter {
    private Context mContext;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private SignUpContract.ViewInterface viewInterface;
    private PreferenceManager preferenceManager;

    public SignUpPresenter(Context mContext, SignUpContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.mContext = mContext;
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    public void signup(String avatar, String username,String email, String password){
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, username);
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_AVATAR, avatar);
        firebaseAuth.createUserWithEmailAndPassword(username,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        System.out.println("create success");
                        if(firebaseAuth.getCurrentUser() != null){
                            user.put(Constants.KEY_ID, firebaseAuth.getCurrentUser().getUid());
                        }
                        DocumentReference userInfor = db.collection(Constants.KEY_COLLECTION_USERS).document(firebaseAuth.getCurrentUser().getUid());
                        userInfor.set(user).addOnSuccessListener(documentReference -> {
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    preferenceManager.putString(Constants.KEY_USED_ID, firebaseAuth.getCurrentUser().getUid());
                                    preferenceManager.putString(Constants.KEY_NAME,username);
                                    preferenceManager.putString(Constants.KEY_AVATAR, avatar);
                                    preferenceManager.putString(Constants.KEY_EMAIL, email);
                                    viewInterface.onSignUpSuccess();
                                })
                                .addOnFailureListener(exception ->{
                                    viewInterface.onSignUpFail(exception.getMessage());
                                });

                    } else {
                        Log.d("SignUpFragment", "signup: "+ task.getResult().toString());
                        // If sign in fails, display a message to the user.
                        viewInterface.onSignUpFail("Đăng ký thất bại!");
                    }
                });
    }

}
