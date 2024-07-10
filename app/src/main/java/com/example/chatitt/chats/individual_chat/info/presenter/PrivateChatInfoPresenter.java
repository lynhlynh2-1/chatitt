package com.example.chatitt.chats.individual_chat.info.presenter;

import androidx.annotation.NonNull;

import com.example.chatitt.ultilities.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class PrivateChatInfoPresenter {
    final private Contract.ChatInfoInterface viewInterface;
    private FirebaseFirestore db;

    public PrivateChatInfoPresenter(Contract.ChatInfoInterface viewInterface) {
        this.viewInterface = viewInterface;
        db = FirebaseFirestore.getInstance();
    }

    public void deleteChat(String chatId){
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
