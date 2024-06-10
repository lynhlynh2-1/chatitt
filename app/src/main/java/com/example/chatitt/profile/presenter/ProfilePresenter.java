package com.example.chatitt.profile.presenter;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfilePresenter {
    private final ProfileContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private User userModel;
    public User getUserModel() {
        return userModel;
    }
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DocumentReference user;


    public ProfilePresenter(ProfileContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = db.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USED_ID));
        userModel = new User();
    }

    public void updateAvatar(String avatar) {
        user.update(Constants.KEY_AVATAR,avatar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        userModel.setAvatar(avatar);
                        viewInterface.onEditProfileSuccess(Constants.KEY_AVATAR);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });
    }

    public void updateUsername(String username) {
        user.update(Constants.KEY_NAME,username)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        userModel.setUsername(username);
                        viewInterface.onEditProfileSuccess(Constants.KEY_NAME);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });
    }
    public void updateCoverImage(String cover_image) {
        user.update(Constants.KEY_COVERIMAGE,cover_image)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        userModel.setCoverImage(cover_image);
                        viewInterface.onEditProfileSuccess(Constants.KEY_COVERIMAGE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });
    }
    private boolean is = false;
    public boolean isLoginAgainSuccessfully(String email, String pass){

        auth.signInWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        is = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        is = false;
                    }
                });
        return is;
    }
    public void updateEmail(String email) {
        user.update(Constants.KEY_EMAIL,email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseUser user = auth.getCurrentUser();
                        if(user!= null){
                            user.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                userModel.setEmail(email);
                                                viewInterface.onEditProfileSuccess(Constants.KEY_EMAIL);
                                            }
                                        }
                                    });
                        }else viewInterface.onEditProfileError();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });

    }

    public void updatePhoneNumber(String phone) {
        user.update(Constants.KEY_PHONE,phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        userModel.setPhonenumber(phone);
                        viewInterface.onEditProfileSuccess(Constants.KEY_PHONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });

    }

    public void updateAddress(String address,String city,String country) {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_ADDRESS_DETAIL, address);
        data.put(Constants.KEY_ADDRESS_CITY, city);
        data.put(Constants.KEY_ADDRESS_COUNTRY, country);
        user.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        userModel.setAddress(address);
                        userModel.setCity(city);
                        userModel.setCountry(country);
                        viewInterface.onEditProfileSuccess(Constants.KEY_ADDRESS_DETAIL);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });
    }

    public void updateGender(String gender) {
        user.update(Constants.KEY_GENDER,gender)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        userModel.setGender(gender);
                        viewInterface.onEditProfileSuccess(Constants.KEY_GENDER);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });
    }
    public void updateBirthday(String birthday) {
        user.update(Constants.KEY_BIRTHDAY,birthday)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        userModel.setBirthday(birthday);
                        viewInterface.onEditProfileSuccess(Constants.KEY_BIRTHDAY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        viewInterface.onEditProfileError();
                    }
                });
    }

    public void getProfile() {
        DocumentReference docRef = db.collection("users").document(preferenceManager.getString(Constants.KEY_USED_ID));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    userModel.setAvatar(document.getString(Constants.KEY_AVATAR));
                    userModel.setCoverImage(document.getString(Constants.KEY_COVERIMAGE));
                    userModel.setUsername(document.getString(Constants.KEY_NAME));
                    userModel.setBirthday(document.getString(Constants.KEY_BIRTHDAY));
                    userModel.setGender(document.getString(Constants.KEY_GENDER));
                    userModel.setEmail(document.getString(Constants.KEY_EMAIL));
                    userModel.setPhonenumber(document.getString(Constants.KEY_PHONE));
                    userModel.setAddress(document.getString(Constants.KEY_ADDRESS_DETAIL));
                    userModel.setCountry(document.getString(Constants.KEY_ADDRESS_COUNTRY));
                    userModel.setCity(document.getString(Constants.KEY_ADDRESS_CITY));
                    viewInterface.onGetProfileSuccess();
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());

            }
        });
    }

    public void logOut(){
        auth.signOut();
    }
}
