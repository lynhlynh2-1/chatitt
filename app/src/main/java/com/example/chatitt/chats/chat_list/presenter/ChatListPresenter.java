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
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        db.collection(Constants.KEY_COLLECTION_CHAT).where(Filter.or(
                Filter.equalTo(Constants.KEY_LEADER, preferenceManager.getString(Constants.KEY_USED_ID)),
                Filter.arrayContains(Constants.KEY_MEMBERS, preferenceManager.getString(Constants.KEY_USED_ID))
        )).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        boolean hasAdd = false;
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED ){
                                hasAdd = true;
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
                                                int chatIndex = chatList.indexOf(chat);
                                                chat.setName(v.getString(Constants.KEY_NAME));
                                                chat.setAvatar(v.getString(Constants.KEY_AVATAR));
                                                viewInterface.updateChat(chatIndex);
                                            });
                                }

                            }
                            if (documentChange.getType() == DocumentChange.Type.MODIFIED){
                                QueryDocumentSnapshot docRef = documentChange.getDocument();
                                Chat chat = docRef.toObject(Chat.class);
                                String chatId = chat.getId(); // Name to search for
                                int chatIndex = chatList.stream()
                                        .map(Chat::getId)
                                        .collect(Collectors.toList())
                                        .indexOf(chatId);
                                Chat modifiedChat = chatList.get(chatIndex);
                                if (!Objects.equals(chat.getType_chat(), Constants.KEY_PRIVATE_CHAT) && !chat.getName().equals(modifiedChat.getName())){
                                    modifiedChat.setName(chat.getName());
                                    modifiedChat.setAvatar(chat.getAvatar());
                                    viewInterface.updateChat(chatIndex);
                                    continue;
                                }
                                modifiedChat.setLastMessage(chat.getLastMessage());
                                modifiedChat.setSenderName(chat.getSenderName());
                                modifiedChat.setTimestamp(chat.getTimestamp());
                                chatList.sort((o1,o2)-> o2.getTimestamp().compareTo(o1.getTimestamp()));
                                viewInterface.notifyChatMove(chatIndex,0);
                            }
                        }
                        if (hasAdd){
                            chatList.sort((o1,o2)-> o2.getTimestamp().compareTo(o1.getTimestamp()));
                            viewInterface.updateChatRealtime(count);
                        }

                        Log.d(TAG, "Current list chat: " + chatList.size());
                    }
                });
    }

}
