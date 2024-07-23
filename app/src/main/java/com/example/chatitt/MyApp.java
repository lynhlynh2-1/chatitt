package com.example.chatitt;

import static com.example.chatitt.firebase.MessagingService.channelId;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Security;

public class MyApp extends Application implements DefaultLifecycleObserver {
    PreferenceManager preferenceManager;
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Chat Message";
            String channelDescription = "This notification channel is used for chat message notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        preferenceManager = new PreferenceManager(getApplicationContext());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_CREATE");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d(getClass().getSimpleName(), "ON_START");
        if (preferenceManager.getString(Constants.KEY_USED_ID) != null && !preferenceManager.getString(Constants.KEY_USED_ID).isEmpty())
            FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                    .document(preferenceManager.getString(Constants.KEY_USED_ID))
                    .update(Constants.KEY_ONLINE,true);
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