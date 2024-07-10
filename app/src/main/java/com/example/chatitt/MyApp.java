package com.example.chatitt;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.Application;
import android.util.Log;

import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyApp extends Application implements DefaultLifecycleObserver {
    PreferenceManager preferenceManager;
    @Override
    public void onCreate() {
        super.onCreate();
        preferenceManager = new PreferenceManager(getApplicationContext());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_CREATE");
        if (preferenceManager.getString(Constants.KEY_USED_ID) != null && !preferenceManager.getString(Constants.KEY_USED_ID).isEmpty())
            FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                    .update(Constants.KEY_ONLINE,true);

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_START");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_RESUME");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_PAUSE");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_STOP");
        if (preferenceManager.getString(Constants.KEY_USED_ID) != null && !preferenceManager.getString(Constants.KEY_USED_ID).isEmpty())
            FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                    .update(Constants.KEY_ONLINE,false);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_DESTROY");
    }
}