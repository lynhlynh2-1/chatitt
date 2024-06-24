package com.example.chatitt.chats.group_chat.info.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.net.http.HttpException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AddMemPresenter {
    private final Contract.AddMemViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private List<User> userModelList;
    private FirebaseFirestore db;

    public AddMemPresenter(Contract.AddMemViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
        userModelList = new ArrayList<>();
    }

    public List<User> getUserModelList() {
        return userModelList;
    }

    public void searchUser (String keyword){
        Query query = db.collection(Constants.KEY_COLLECTION_USERS).where(Filter.or(
                Filter.equalTo(Constants.KEY_EMAIL,keyword),
                Filter.equalTo(Constants.KEY_PHONE,keyword),
                Filter.equalTo(Constants.KEY_NAME,keyword)
        ));
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            if (task.getResult() == null || task.getResult().getDocuments().size() == 0) {
                                viewInterface.onNoUser();
                                return;
                            }
                            List<User> usersFind = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                user.setChecked(false);
                                usersFind.add(user);
                            }
                            viewInterface.onSearchUserSuccess(usersFind);
                        } else {
                            Log.d(TAG, "Error getting ListFriend ", task.getException());
                            viewInterface.onSearchUserError();
                        }
                    }
                });
    }

    public void getListFriend (String user_id){
        db.collection(Constants.KEY_COLLECTION_USERS).
                whereArrayContains(Constants.FRIEND_LIST, preferenceManager.getString(Constants.KEY_USED_ID))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                user.setChecked(false);
                                userModelList.add(user);
                            }
                            viewInterface.onGetListFriendSuccess();
                        } else {
                            Log.d(TAG, "Error getting ListFriend ", task.getException());
                            viewInterface.onGetListFriendError();
                        }
                    }
                });
    }
}
