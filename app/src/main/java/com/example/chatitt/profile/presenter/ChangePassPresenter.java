package com.example.chatitt.profile.presenter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePassPresenter {
    
    private final ChangePassContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private Context context;
    private FirebaseAuth mAuth;

    public ChangePassPresenter(ChangePassContract.ViewInterface viewInterface, PreferenceManager preferenceManager, Context context) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public void changePass(String cur_pass, String newPass) {
        mAuth.signInWithEmailAndPassword(preferenceManager.getString(Constants.KEY_EMAIL), cur_pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //login success
                                updatePasswordToFirestore(newPass);
                            }else {
                                //login fail
                                viewInterface.onCurrentPassWrong();
                            }
                        }
                    });
    }
    private void updatePasswordToFirestore(String new_pass){
        mAuth.getCurrentUser().updatePassword(new_pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xử lý khi thay đổi mật khẩu thành công
                            viewInterface.onChangePassSuccess();
                        } else {
                            // Xử lý khi thay đổi mật khẩu không thành công
                            viewInterface.onChangePassError();
                            Log.e(TAG, task.getResult().toString());

                        }
                    }
                });
    }
}
