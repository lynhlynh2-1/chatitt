package com.example.chatitt.contacts.manage_request_friend.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReceiveReqPresenter {
    private ReceiveReqContract.ViewInterface viewInterface;
    private List<User> userModelList;
    private User user;

    public List<User> getUserModelList() {
        return userModelList;
    }
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    public ReceiveReqPresenter(ReceiveReqContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        db = FirebaseFirestore.getInstance();
        this.preferenceManager = preferenceManager;
        userModelList = new ArrayList<>();
    }

    public void getReceiveReq(){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .addSnapshotListener((v,err)->{
                    if (err != null) {
                        Log.w(TAG, "Listen failed.", err);
                        viewInterface.getReceiveReqFail();
                        return;
                    }
                    if (v != null && v.exists()){
                        User tempUser = v.toObject(User.class);
                        if (tempUser.getOther_request_friend() == null || tempUser.getOther_request_friend().size() == 0){
                            viewInterface.getReceiveReqEmpty();
                        }
                        if (tempUser.getOther_request_friend().size() > userModelList.size()){
                            //Add My Req
                            int oldSize = userModelList.size();
                            int newSize = tempUser.getOther_request_friend().size();
                            List<String> userIDList = new ArrayList<>();
                            for (int i = oldSize; i < newSize; i++) {
                                String userId = tempUser.getOther_request_friend().get(i);
                                userIDList.add(userId);
                            }
                            getUserInforAndListener(userIDList);
                        }else if (tempUser.getOther_request_friend().size() + 1 < userModelList.size()){
                            //Del My Req
                            int i = 0;
                            int j = 0;
                            for (User user : userModelList){
                                if (j == 0) {
                                    j++;
                                    continue;
                                }
                                if (i == tempUser.getOther_request_friend().size()){
                                    i ++;
                                    break;
                                }
                                if (user.getId().equals(tempUser.getOther_request_friend().get(i))){
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
//                            if (user.getOther_request_friend() != null && !user.getOther_request_friend().isEmpty()) {
//                                for (String friend : user.getOther_request_friend()) {
//                                    db.collection(Constants.KEY_COLLECTION_USERS)
//                                            .document(friend)
//                                            .get()
//                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
//                                                        User tempUser = task.getResult().toObject(User.class);
//                                                        userModelList.add(tempUser);
//                                                        viewInterface.getReceiveReqSuccess(tempUser);
//                                                    } else
//                                                        viewInterface.getReceiveReqFail();
//                                                }
//                                            });
//                                }
//                            }
//                        }else
//                            viewInterface.getReceiveReqFail();
//                    }
//                });
    }

    private void getUserInforAndListener(List<String> userIDList) {
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
                                viewInterface.getReceiveReqSuccess(user);
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

    public void setAcceptFriend(String userId, String status, int pos){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.FRIEND_LIST, FieldValue.arrayUnion(userId));
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(Constants.FRIEND_LIST, FieldValue.arrayUnion(preferenceManager.getString(Constants.KEY_USED_ID)));

        //
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.OTHER_REQ_FRIEND_LIST, FieldValue.arrayRemove(userId));
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(Constants.MY_REQ_FRIEND_LIST, FieldValue.arrayRemove(preferenceManager.getString(Constants.KEY_USED_ID)));
//        APIServices.apiServices.setAccept(token, userId,status)
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
//                        viewInterface.setAcceptSuccess(status, pos);
//                    }
//                });
    }


}
