package com.example.chatitt.contacts.manage_request_friend.presenter;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
                            user = task.getResult().toObject(User.class);
                            if (user == null)
                                return;
                            if (user.getOther_request_friend() != null && !user.getOther_request_friend().isEmpty()) {
                                for (String friend : user.getOther_request_friend()) {
                                    db.collection(Constants.KEY_COLLECTION_USERS)
                                            .document(friend)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                                                        User tempUser = task.getResult().toObject(User.class);
                                                        userModelList.add(tempUser);
                                                        viewInterface.getReceiveReqSuccess();
                                                    } else
                                                        viewInterface.getReceiveReqFail();
                                                }
                                            });
                                }
                            }
                        }else
                            viewInterface.getReceiveReqFail();
                    }
                });
    }
    public void setAcceptFriend(String userId, String status, int pos){
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
