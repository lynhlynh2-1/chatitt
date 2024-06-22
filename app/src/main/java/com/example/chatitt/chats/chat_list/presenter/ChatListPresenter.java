package com.example.chatitt.chats.chat_list.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatListPresenter {
    private final ChatListContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private List<Chat> chatList;
    private List<User> userModelList;
    private FirebaseFirestore db;



    public ChatListPresenter(ChatListContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
        chatList = new ArrayList<>();
        JSONObject data = new JSONObject();

    }


    public List<Chat> getChatList() {
        return chatList;
    }
    public void resetChatList() {
        this.chatList = new ArrayList<>();
    }

    public List<User> getUserModelList() {
        return userModelList;
    }

    private int k;
    public void getMessaged(){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        if (value == null || value.isEmpty()){
                            Log.w(TAG, "Listen empty.", e);
                            return;
                        }
                        int count = chatList.size();
                        k = count;

                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED ){
                                QueryDocumentSnapshot docRef = documentChange.getDocument();
                                Chat chat = docRef.toObject(Chat.class);
                                k = k + 1;
                                chatList.add(chat);
                                if (chat.getType_chat().equals(Constants.KEY_PRIVATE_CHAT)){
                                    String receiverId = chat.getLeader();
                                    if (Objects.equals(receiverId, preferenceManager.getString(Constants.KEY_USED_ID))){
                                        receiverId = chat.getMembers().get(0);
                                    }
                                    db.collection(Constants.KEY_COLLECTION_USERS)
                                            .document(receiverId)
                                            .addSnapshotListener((v,err)->{
                                                if (err != null) {
                                                    Log.w(TAG, "Listen user in getChat List failed.", e);
                                                    return;
                                                }
                                                if (v == null || !v.exists()){
                                                    Log.w(TAG, "Listen user in getChat List empty.", e);
                                                    return;
                                                }
                                                chatList.get(k-1).setName(v.getString(Constants.KEY_NAME));
                                                chatList.get(k-1).setAvatar(v.getString(Constants.KEY_AVATAR));
                                                viewInterface.updateChatRealtime(k-1);
                                            });
                                }

                            }
                            if (documentChange.getType() == DocumentChange.Type.MODIFIED){

                            }
                        }
                        chatList.sort((obj1, obj2) -> obj2.getTimestamp().compareTo(obj1.getTimestamp()));
                        viewInterface.updateChatRealtime(count);
                        Log.d(TAG, "Current list chat: " + chatList.size());
                    }
                });
    }

}
