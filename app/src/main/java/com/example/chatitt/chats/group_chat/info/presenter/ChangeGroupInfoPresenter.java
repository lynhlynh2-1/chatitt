package com.example.chatitt.chats.group_chat.info.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

import java.util.HashMap;
import java.util.Map;


public class ChangeGroupInfoPresenter {
    private final Contract.ChangeGroupInfoViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private Chat chat;
    private FirebaseFirestore db;

    public ChangeGroupInfoPresenter(Contract.ChangeGroupInfoViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
    }

    public Chat getChat() {
        return chat;
    }

    public void updateChatInfo(String chatId, String name, String avatar){
        Map<String, Object> data = new HashMap<>();
        if (name != null)
            data.put(Constants.KEY_NAME, name);
        if (avatar != null)
            data.put(Constants.KEY_AVATAR, avatar);
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            viewInterface.onUpdateSuccess();
                        }else {
                            viewInterface.onUpdateEror();
                        }
                    }
                });
    }

    public void getInfoRealtime (String chatId){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){

                            return;
                        }
                        if (value != null && value.exists()){
                            chat = value.toObject(Chat.class);
                            viewInterface.onChatInfoChanged(chat.getName(), chat.getAvatar());
                        }
                    }
                });
    }

    public void deleteMember (String chatId, String user_id){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .update(Constants.KEY_MEMBERS, FieldValue.arrayRemove(user_id))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        viewInterface.onDelMemSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        viewInterface.onDelMemEror();
                    }
                });
    }

    public void deleteGroup(String chatId) {
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        viewInterface.onDelGroupSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        viewInterface.onDelMemEror();
                    }
                });
    }
}
