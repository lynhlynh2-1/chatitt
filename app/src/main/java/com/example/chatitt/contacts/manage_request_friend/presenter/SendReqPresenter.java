package com.example.chatitt.contacts.manage_request_friend.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class SendReqPresenter {
    private SendReqContract.ViewInterface viewInterface;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db;
    private User user;
    private List<User> userModelList;
    public List<User> getUserModelList() {
        return userModelList;
    }

    public SendReqPresenter(SendReqContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
        userModelList = new ArrayList<>();
    }
    public void getSendReq(){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .addSnapshotListener((v,err)->{
                    if (err != null) {
                        Log.w(TAG, "Listen failed.", err);
                        viewInterface.onGetSendReqError();
                        return;
                    }
                    if (v != null && v.exists()){
                        User tempUser = v.toObject(User.class);
                        if (tempUser.getMy_friend_request() == null || tempUser.getMy_friend_request().size() == 0){
                            viewInterface.getSendReqEmpty();
                            return;
                        }

                        if (tempUser.getMy_friend_request().size() > userModelList.size()){
                            //Add My Req
                            int oldSize = userModelList.size();
                            int newSize = tempUser.getMy_friend_request().size();
                            List<String> userIDList = new ArrayList<>();
                            for (int i = oldSize; i < newSize; i++) {
                                String userId = tempUser.getMy_friend_request().get(i);
                                userIDList.add(userId);
                            }
                            getUserInforAndListener(userIDList);
                        }else if (tempUser.getMy_friend_request().size() + 1 < userModelList.size()){
                            //Del My Req
                            int i = 0;
                            int j = 0;
                            for (User user : userModelList){
                                if (j == 0) {
                                    j++;
                                    continue;
                                }
                                if (i == tempUser.getMy_friend_request().size()){
                                    i ++;
                                    break;
                                }
                                if (user.getId().equals(tempUser.getMy_friend_request().get(i))){
                                    i++;
                                }else {
                                    break;
                                }
                            }
                            userModelList.remove(i);
                            viewInterface.onDelReqSuccess(i);
                        }
                    }
                });
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
//                            user = task.getResult().toObject(User.class);
//                            if (user == null)
//                                return;
//                            if (user.getMy_friend_request() != null && !user.getMy_friend_request().isEmpty()) {
//                                for (String friend : user.getMy_friend_request()) {
//                                    db.collection(Constants.KEY_COLLECTION_USERS)
//                                            .document(friend)
//                                            .get()
//                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
//                                                        User tempUser = task.getResult().toObject(User.class);
//                                                        userModelList.add(tempUser);
//                                                        viewInterface.getSendReqSuccess(tempUser);
//                                                    } else
//                                                        viewInterface.getSendReqFail();
//                                                }
//                                            });
//                                }
//                            }
//                        }else
//                            viewInterface.getSendReqFail();
//                    }
//                });
//        APIServices.apiServices.getSendRequestFriend("Bearer " +token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ListReqRes>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ListReqRes listReqRes) {
//                        userModelList = listReqRes.getData();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        viewInterface.getSendReqFail();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.getSendReqSuccess();
//                    }
//                });
    }

    private void getUserInforAndListener(List<String> userIDList){

        for(String userId : userIDList){
            db.collection(Constants.KEY_COLLECTION_USERS)
                    .document(userId)
                    .addSnapshotListener((value,err)->{
                        if (err != null) {
                            Log.w(TAG, "Listen failed.", err);
                            return;
                        }
                        if (value != null && value.exists()){
                            User user = value.toObject(User.class);
                            int i = 0;
                            for (User userr : userModelList){
                                if (userr.getId().equals(user.getId())){
                                    break;
                                }
                                i ++;
                            }
                            if(i >= userModelList.size()){
                                userModelList.add(user);
                                viewInterface.getSendReqSuccess(user);
                            }else {
                                userModelList.set(i, user);
                                viewInterface.onUserInfoChangeSuccess(i);
                            }
                        }
                    });
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot v) {
//                            db.collection(Constants.KEY_COLLECTION_USERS)
//                                    .document(userId)
//
//
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            viewInterface.onGetMemberError();
//                        }
//                    });

        }
    }
    public void delReq(String userId, int pos){
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
//                        viewInterface.delReqSuccess(pos);
//                    }
//                });
    }

}
