package com.example.chatitt.firebase;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chatitt.MainActivity;
import com.example.chatitt.R;
import com.example.chatitt.chats.chat.view.ChatActivity;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class MessagingService extends FirebaseMessagingService {
    public static String channelId = "chat_message";
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db;

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM - HP", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token) {

        preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USED_ID))
                .update(Constants.KEY_FCM_TOKEN, token);
//        APIServices.apiServices.updateFCMToken("Bearer " + preferenceManager.getString(Constants.KEY_TOKEN), token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<EditProfileResponse>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull EditProfileResponse editProfileResponse) {
//
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
////                        Toast.makeText(MessagingService.this, "FCM token thay đổi! Cập nhật FCM token lên server thất bại", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        Toast.makeText(MessagingService.this, "FCM token thay đổi! Cập nhật FCM token lên server thành công", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> data = message.getData();

        // Kiểm tra các trường dữ liệu tùy chỉnh để phân biệt các loại thông báo
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, ChatActivity.class);
        resultIntent.putExtra("ChatId", data.get("ChatId"));
        resultIntent.putExtra(Constants.KEY_NAME, data.get(Constants.KEY_NAME));
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(data.get(Constants.KEY_NAME));


        int notificationId = new Random().nextInt();
        String plainMess = "";
        switch (data.get(Constants.TYPE_MESSAGES_SEND)){
            case Constants.KEY_TYPE_TEXT:
                plainMess =  data.get(Constants.KEY_MESSAGE);
                break;
            case Constants.KEY_TYPE_IMAGE:
                plainMess = "Hình ảnh";
                break;
            case Constants.KEY_TYPE_VOICE:
                plainMess = "Ghi âm";
                break;
        }
        if (data.containsKey("TypeChat")) {
            String notificationType = data.get("TypeChat");
            if (Objects.equals(notificationType, Constants.KEY_GROUP_CHAT)) {
                builder.setContentText(data.get(Constants.KEY_SENDER_NAME) +": "+ plainMess) ;
            }else
                builder.setContentText(plainMess) ;
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                    plainMess
            ));
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setContentIntent(resultPendingIntent);
            builder.setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, builder.build());
        }


    }
}
