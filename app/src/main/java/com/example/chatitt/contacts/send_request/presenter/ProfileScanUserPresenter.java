package com.example.chatitt.contacts.send_request.presenter;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileScanUserPresenter {

    private ProfileScanUserContract.ViewInterface viewInterface;
    private PreferenceManager preferenceManager;
    private User me, you;
    private String status;
    private Boolean isSend;
    private FirebaseFirestore db;


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
                                }else if (you.getMy_friend_request() != null && you.getMy_friend_request().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
                                    status = Constants.KEY_MY_REQ_FRIEND;

                                }else if (you.getOther_request_friend() != null && you.getOther_request_friend().contains(preferenceManager.getString(Constants.KEY_USED_ID))){
                                    status = Constants.KEY_OTHER_REQ_FRIEND;
                                }
                                viewInterface.getStatusFriendSuccess();
                            }else
                                viewInterface.getStatusFriendFail();
                        }
                    });
    }

    public void setReqFriend(String userId){
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

}
