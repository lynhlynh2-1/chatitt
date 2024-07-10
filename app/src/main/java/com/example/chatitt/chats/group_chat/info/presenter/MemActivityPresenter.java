package com.example.chatitt.chats.group_chat.info.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MemActivityPresenter {
    private final Contract.MemListViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private List<User> userModelList;
    private FirebaseFirestore db;

    public MemActivityPresenter(Contract.MemListViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
        userModelList = new ArrayList<>();
    }

    public List<User> getUserModelList() {
        return userModelList;
    }

    public void registerMemberListener(String chatId){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .addSnapshotListener((v,err)->{
                    if (err != null) {
                        Log.w(TAG, "Listen failed.", err);
                        viewInterface.onGetMemberError();
                        return;
                    }
                    if (v != null && v.exists()){
                        Chat chat = v.toObject(Chat.class);
                        if (userModelList.size() == 0){
                            ArrayList<String> list = new ArrayList<>();
                            list.add(chat.getLeader());
                            getUserInforAndListener(list);
                        }
                        if (chat.getMembers().size() + 1 > userModelList.size()){
                            //Add Mem
                            int oldSize = userModelList.size() == 0 ? 0 : userModelList.size() - 1;

                            int newSize = chat.getMembers().size();
                            List<String> userIDList = new ArrayList<>();
                            for (int i = oldSize; i < newSize; i++) {
                                String userId = chat.getMembers().get(i);
                                userIDList.add(userId);
                            }
                            getUserInforAndListener(userIDList);
                        }else if (chat.getMembers().size() + 1 < userModelList.size()){
                            //Del Mem
                            int i = 0;
                            int j = 0;
                            for (User user : userModelList){
                                if (j == 0) {
                                    j++;
                                    continue;
                                }
                                if (i == chat.getMembers().size()){
                                    i ++;
                                    break;
                                }
                                if (user.getId().equals(chat.getMembers().get(i))){
                                    i++;
                                }else {
                                    break;
                                }
                            }
                            userModelList.remove(i);
                            viewInterface.onDelMemSuccess(i);
                        }
                    }
                });
    }
    public void addMember (String chatId, List<String> user_id){
        for (String item : user_id)
            db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .update(Constants.KEY_MEMBERS, FieldValue.arrayUnion(item));
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            for(String userId : user_id){
//                                db.collection(Constants.KEY_COLLECTION_USERS)
//                                        .document(userId)
//                                        .addSnapshotListener((v,err)->{
//                                            if (err != null) {
//                                                Log.w(TAG, "Listen failed.", err);
//                                                viewInterface.onAddMemEror();
//                                                return;
//                                            }
//                                            if (v != null && v.exists()){
//                                                User user = v.toObject(User.class);
//                                                userModelList.add(user);
//                                                viewInterface.onAddMemSuccess();
//                                            }
//                                        });
//                            }
//                        } else {
//                            Log.d(TAG, "Error add Member", task.getException());
//                            viewInterface.onAddMemEror();
//                        }
//                    }
//                });
    }
    public void deleteMember (String chatId, String user_id){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .update(Constants.KEY_MEMBERS, FieldValue.arrayRemove(user_id));
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            int i = 0;
//                            for (User user : userModelList){
//                                if (user.getId().equals(user_id)){
//                                    break;
//                                }
//                                i ++;
//                            }
//                            userModelList.remove(i);
////                            userModelList.removeIf(obj -> obj.getId().equals(user_id));
//                            viewInterface.onDelMemSuccess(chatId, user_id, i);
//                        } else {
//                            Log.d(TAG, "Error delete Member ", task.getException());
//                            viewInterface.onDelMemEror();
//                        }
//                    }
//                });
    }
    public void getMember(Chat chat){
        userModelList.clear();
        viewInterface.resetAdapter();
        List<String> userIDList = new ArrayList<>(chat.getMembers());
        userIDList.add(0, chat.getLeader());
        getUserInforAndListener(userIDList);
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
                                viewInterface.onGetMemberSuccess(user);
                            }else {
                                userModelList.set(i, user);
                                viewInterface.onMemInfoChangeSuccess(i);
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
}
