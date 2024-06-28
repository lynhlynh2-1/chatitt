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
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
                            user = task.getResult().toObject(User.class);
                            if (user == null)
                                return;
                            if (user.getMy_friend_request() != null && !user.getMy_friend_request().isEmpty()) {
                                for (String friend : user.getMy_friend_request()) {
                                    db.collection(Constants.KEY_COLLECTION_USERS)
                                            .document(friend)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                                                        User tempUser = task.getResult().toObject(User.class);
                                                        userModelList.add(tempUser);
                                                        viewInterface.getSendReqSuccess(tempUser);
                                                    } else
                                                        viewInterface.getSendReqFail();
                                                }
                                            });
                                }
                            }
                        }else
                            viewInterface.getSendReqFail();
                    }
                });
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
    public void delReq(String userId, int pos){
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
