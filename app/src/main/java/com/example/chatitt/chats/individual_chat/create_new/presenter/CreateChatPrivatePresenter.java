package com.example.chatitt.chats.individual_chat.create_new.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.net.http.HttpException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CreateChatPrivatePresenter {
    private CreateChatPrivateContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private List<User> userModelList;
    private FirebaseFirestore db;

    public List<User> getUserModelList() {
        return userModelList;
    }
    private Chat chat;

    public Chat getChat() {
        return chat;
    }

    public CreateChatPrivatePresenter(CreateChatPrivateContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
        userModelList = new ArrayList<>();
    }

    public void searchUser (String keyword){
        Query query = db.collection(Constants.KEY_COLLECTION_USERS).where(Filter.or(
                Filter.equalTo(Constants.KEY_EMAIL,keyword),
                Filter.equalTo(Constants.KEY_PHONE,keyword),
                Filter.equalTo(Constants.KEY_NAME,keyword)
        ));
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                usersFind.add(user);
                            }
                            viewInterface.onSearchUserSuccess(usersFind);
                        } else {
                            Log.d(TAG, "Error search User ", task.getException());
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

    public void deleteFriend(String id) {

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


//            db.collection(Constants.KEY_COLLECTION_CHAT).document(
//                    chatId
//            ).addSnapshotListener((value, error) -> {
//                if (error != null){
//
//                    return;
//                }
//                if (value != null){
//
//                }
//            });
                    }else {
                        viewInterface.onFindChatError();
                    }
                });
    }


    public void findChat(User user) {
        checkForConversionRemotely(preferenceManager.getString(Constants.KEY_USED_ID), user.getId(), user);
    }
}
