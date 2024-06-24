package com.example.chatitt.chats.individual_chat.info.presenter;

import androidx.annotation.Nullable;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileDetailPresenter {
    private Contract.ProfileDetailInterface viewInterface;
    private FirebaseFirestore db;
    private User user;

    public User getUser() {
        return user;
    }

    public ProfileDetailPresenter(Contract.ProfileDetailInterface profileDetailInterface) {
        this.viewInterface = profileDetailInterface;
        db = FirebaseFirestore.getInstance();
    }

    public void getInfo(String userId){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            viewInterface.getInfoDetailFail();
                            return;
                        }
                        if (value != null && value.exists()){
                            user = value.toObject(User.class);
                            viewInterface.getInfoDetailSuccess();
                        }
                    }
                });
    }
}
