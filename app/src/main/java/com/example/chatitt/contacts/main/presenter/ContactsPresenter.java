package com.example.chatitt.contacts.main.presenter;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ContactsPresenter {
    private ContactsContract.ViewInterface viewInterface;
    private FirebaseFirestore db;

    User userModels;

    public User getUserModels() {
        return userModels;
    }

    public ContactsPresenter(ContactsContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
        db = FirebaseFirestore.getInstance();
    }

    public void searchUser(String keyword){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, keyword)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){
                            userModels = task.getResult().getDocuments().get(0).toObject(User.class);
                            viewInterface.onSearchUserSuccess();
                        }else
                            viewInterface.onSearchUserError();
                    }
                });
    }

    public void addUserListener(String userId) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .addSnapshotListener((v,e)->{
                    if(e != null) return;
                    if(v != null && v.exists()){
                        User user = v.toObject(User.class);
                        viewInterface.onUserUpdate(user);
                    }
                });
    }
}
