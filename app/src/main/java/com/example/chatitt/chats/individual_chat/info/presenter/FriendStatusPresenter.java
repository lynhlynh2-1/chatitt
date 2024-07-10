package com.example.chatitt.chats.individual_chat.info.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class FriendStatusPresenter {

    private Contract.FriendStatusInterface viewInterface;
    private User me, you;
    private String status;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    public FriendStatusPresenter(Contract.FriendStatusInterface friendStatusInterface, PreferenceManager preferenceManager) {
        this.viewInterface = friendStatusInterface;
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
                .addSnapshotListener((v, err) -> {
                    if (err != null) {
                        Log.w(TAG, "Listen failed.", err);
                        viewInterface.getStatusFriendFail();
                        return;
                    }
                    if (v != null && v.exists()){
                        you = v.toObject(User.class);
                        if (you == null)
                            return;
                        if (you.getFriend_list() != null && you.getFriend_list().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
                            status = Constants.KEY_IS_FRIEND;
                        }else if (you.getMy_friend_request() != null && you.getMy_friend_request().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
                            status = Constants.KEY_OTHER_REQ_FRIEND;
                        }else if (you.getOther_request_friend() != null && you.getOther_request_friend().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
                            status = Constants.KEY_MY_REQ_FRIEND;
                        }else
                            status = Constants.KEY_NOT_FRIEND;

                        viewInterface.getStatusFriendSuccess();
                    }
                });
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
//                            you = task.getResult().toObject(User.class);
//                            if (you == null)
//                                return;
//                            if (you.getFriend_list() != null && you.getFriend_list().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
//                                status = Constants.KEY_IS_FRIEND;
//                            }
//                            viewInterface.getStatusFriendSuccess();
//                        }else
//                            viewInterface.getStatusFriendFail();
//                    }
//                });
    }

    public void delFriend(String userId) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.FRIEND_LIST, FieldValue.arrayRemove(userId))
                .addOnSuccessListener(unused -> db.collection(Constants.KEY_COLLECTION_USERS)
                        .document(userId)
                        .update(Constants.FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)))
                        .addOnFailureListener(e -> {
                            viewInterface.onActionFail();
                            db.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                    .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayUnion(userId));
                        }))
                .addOnFailureListener(e -> viewInterface.onActionFail());
    }

    public void addFriend(String userId) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayUnion(userId))
                .addOnSuccessListener(unused -> db.collection(Constants.KEY_COLLECTION_USERS)
                        .document(userId)
                        .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayUnion(preferenceManager.getString(Constants.KEY_USED_ID)))
                        .addOnFailureListener(e -> {
                            viewInterface.onActionFail();
                            db.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                    .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayRemove(userId));
                        }))
                .addOnFailureListener(e -> viewInterface.onActionFail());
    }

    public void cancelReq(String userId) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayRemove(userId))
                .addOnSuccessListener(unused -> db.collection(Constants.KEY_COLLECTION_USERS)
                        .document(userId)
                        .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)))
                        .addOnFailureListener(e -> {
                            viewInterface.onActionFail();
                            db.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                    .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayUnion(userId));
                        }))
                .addOnFailureListener(e -> viewInterface.onActionFail());

    }

    public void accept(String userId) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.FRIEND_LIST, FieldValue.arrayUnion(userId))
                .addOnSuccessListener(unused -> db.collection(Constants.KEY_COLLECTION_USERS)
                        .document(userId)
                        .update(Constants.FRIEND_LIST, FieldValue.arrayUnion(preferenceManager.getString(Constants.KEY_USED_ID)))
                        .addOnSuccessListener(unused1 -> db.collection(Constants.KEY_COLLECTION_USERS)
                                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayRemove(userId))
                                .addOnSuccessListener(unused11 -> db.collection(Constants.KEY_COLLECTION_USERS)
                                        .document(userId)
                                        .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)))
                                        .addOnFailureListener(e -> {
                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                                    .update(Constants.FRIEND_LIST, FieldValue.arrayRemove(userId));
                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                    .document(userId)
                                                    .update(Constants.FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)));
                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                                    .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayUnion(userId));
                                            viewInterface.onActionFail();
                                        }))
                                .addOnFailureListener(e -> {
                                    viewInterface.onActionFail();
                                    db.collection(Constants.KEY_COLLECTION_USERS)
                                            .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                            .update(Constants.FRIEND_LIST, FieldValue.arrayRemove(userId));
                                    db.collection(Constants.KEY_COLLECTION_USERS)
                                            .document(userId)
                                            .update(Constants.FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)));
                                }))
                        .addOnFailureListener(e -> db.collection(Constants.KEY_COLLECTION_USERS)
                                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                .update(Constants.FRIEND_LIST, FieldValue.arrayRemove(userId))))
                .addOnFailureListener(e -> viewInterface.onActionFail());
    }

    public void decline(String userId) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayRemove(userId))
                .addOnSuccessListener(unused -> db.collection(Constants.KEY_COLLECTION_USERS)
                        .document(userId)
                        .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)))
                        .addOnFailureListener(e -> {
                            viewInterface.onActionFail();
                            db.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                                    .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayUnion(userId));
                        }))
                .addOnFailureListener(e -> viewInterface.onActionFail());
    }
}
