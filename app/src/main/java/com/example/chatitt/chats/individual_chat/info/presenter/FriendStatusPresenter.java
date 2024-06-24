package com.example.chatitt.chats.individual_chat.info.presenter;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendStatusPresenter {

    private Contract.FriendStatusInterface friendStatusInterface;
    private User me, you;
    private String status;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    public FriendStatusPresenter(Contract.FriendStatusInterface friendStatusInterface, PreferenceManager preferenceManager) {
        this.friendStatusInterface = friendStatusInterface;
        db = FirebaseFirestore.getInstance();
        this.preferenceManager = preferenceManager;
        status = Constants.KEY_NOT_FRIEND;
    }


    public User getMe() {
        return me;
    }

    public User getYou() {
        return you;
    }

    public String getStatus() {
        return status;
    }

    public void getStatusFriend(String receiver){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(receiver)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
                            you = task.getResult().toObject(User.class);
                            if (you == null)
                                return;
                            if (you.getFriend_list() != null && you.getFriend_list().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
                                status = Constants.KEY_IS_FRIEND;
                            }
                            friendStatusInterface.getStatusFriendSuccess();
                        }else
                            friendStatusInterface.getStatusFriendFail();
                    }
                });
    }
}
