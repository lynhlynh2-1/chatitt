package com.example.chatitt.contacts.send_request.presenter;

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

public class ProfileScanUserPresenter {

    private ProfileScanUserContract.ViewInterface viewInterface;
    private PreferenceManager preferenceManager;
    private User me, you;
    private String status;
    private Boolean isSend;
    private FirebaseFirestore db;
    private Chat chat;


    public ProfileScanUserPresenter(ProfileScanUserContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
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

    public Boolean getSend() {
        return isSend;
    }

    public void getStatusFriend(String receiver){
        db.collection(Constants.KEY_COLLECTION_USERS)
            .document(receiver)
                .addSnapshotListener((v,err)->{
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
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
//                                you = task.getResult().toObject(User.class);
//                                if (you == null)
//                                    return;
//                                if (you.getFriend_list() != null && you.getFriend_list().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
//                                    status = Constants.KEY_IS_FRIEND;
//                                }else if (you.getMy_friend_request() != null && you.getMy_friend_request().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
//                                    status = Constants.KEY_OTHER_REQ_FRIEND;
//                                }else if (you.getOther_request_friend() != null && you.getOther_request_friend().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
//                                    status = Constants.KEY_MY_REQ_FRIEND;
//                                }
//                                viewInterface.getStatusFriendSuccess();
//                            }else
//                        }
//                    });
    }

    public void setReqFriend(String userId){

        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayUnion(userId));
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayUnion(preferenceManager.getString(Constants.KEY_USED_ID)));
//        APIServices.apiServices.setRequestFriend("Bearer "+token, userId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<SetReqFriendRes>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(@NonNull SetReqFriendRes setReqFriendRes) {
//                        friend = setReqFriendRes.getData();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        viewInterface.setReqFriendFail();
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.setReqFriendSuccess();
//                    }
//                });
    }

    public void setAcceptFriend(String userId, String status){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.FRIEND_LIST, FieldValue.arrayUnion(userId));
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(Constants.FRIEND_LIST, FieldValue.arrayUnion(preferenceManager.getString(Constants.KEY_USED_ID)));
//
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()){
//                                    db.collection(Constants.KEY_COLLECTION_USERS)
//                                            .document(userId)
//                                            .update(Constants.FRIEND_LIST, FieldValue.arrayUnion(preferenceManager.getString(Constants.KEY_USED_ID)))
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()){
//
//                                                    }
//                                                }
//                                            });
//                                }
//                            }
//                        });


        //
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayRemove(userId));
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)));
//        APIServices.apiServices.setAccept("Bearer "+token, userId,status)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<SetReqFriendRes>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(@NonNull SetReqFriendRes setReqFriendRes) {
//                        friend = setReqFriendRes.getData();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        viewInterface.setAcceptFail();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.setAcceptSuccess(status);
//                    }
//                });
    }

    public void delReq(String userId){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayRemove(userId));
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)));

//        APIServices.apiServices.delRequest("Bearer "+ token, userId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<SetReqFriendRes>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(@NonNull SetReqFriendRes setReqFriendRes) {
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        viewInterface.delReqFail();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.delReqSuccess();
//                    }
//                });
    }

    private void checkForConversionRemotely(String senderId, String receiverId, User user){
        ArrayList<String> leader = new ArrayList<>();
        leader.add(senderId);
        leader.add(receiverId);

        ArrayList<String> mem1 = new ArrayList<>();
        mem1.add(senderId);
        ArrayList<String> mem2 = new ArrayList<>();
        mem2.add(receiverId);

        ArrayList<ArrayList<String>> mem = new ArrayList<>();
        mem.add(mem1);
        mem.add(mem2);

        db.collection(Constants.KEY_COLLECTION_CHAT)
                .whereIn(Constants.KEY_LEADER, leader)
                .whereIn(Constants.KEY_MEMBERS, mem)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() ){
                        if (task.getResult() == null || task.getResult().getDocuments().isEmpty()){
                            viewInterface.onChatNotExist(user);
                        }else {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            chat = documentSnapshot.toObject(Chat.class);
                            viewInterface.onFindChatSucces(user.getId());
                        }

                    }else {
                        viewInterface.onFindChatError();
                    }
                });
    }


    public void findChat(User user) {
        checkForConversionRemotely(preferenceManager.getString(Constants.KEY_USED_ID), user.getId(), user);
    }

    public Chat getChat() {
        return this.chat;
    }
}
