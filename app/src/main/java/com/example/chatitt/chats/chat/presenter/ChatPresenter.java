package com.example.chatitt.chats.chat.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.networking.APIServices;
import com.example.chatitt.networking.ApiClientFirebase;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatPresenter {
    private ChatContract.ViewInterface viewInterface ;
    private final PreferenceManager preferenceManager;
    private List<Message> messageList;
    private Chat chat;
    private FirebaseFirestore db;
    private String conversionId = "";

    public ChatPresenter(ChatContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;
        db = FirebaseFirestore.getInstance();
        messageList = new ArrayList<>();
        chat = new Chat();
    }

    public String getConversionId() {
        return conversionId;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public Chat getChat() {
        return chat;
    }


    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void getMessages(String chatId){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .collection(Constants.KEY_COLLECTION_MESSAGE)
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
                        int count = messageList.size();
                        int k=0;
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                QueryDocumentSnapshot docRef = documentChange.getDocument();
                                Message message = docRef.toObject(Message.class);
                                messageList.add(message);
                                k=k+1;
                            }
                        }
                        messageList.sort(Comparator.comparing(Message::getTimestamp));
                        viewInterface.updateMessList(count);
                        Log.d(TAG, "Current messages in Chat: " + messageList.size());
                    }
                });
    }


    public void findChatById(String chatId) {
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (value != null && value.exists()) {
                            Log.d(TAG, "Current data: " + value.getData());
                            chat = value.toObject(Chat.class);
                            viewInterface.onInfoChatChanged(chat);
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }

    public void createChatAndSend(ArrayList<String> receivers, String name, String typeChat, String typeMess, String content, int pos){
        // Add a new document with a generated id.
        DocumentReference chatRef = db.collection(Constants.KEY_COLLECTION_CHAT).document();

        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_LEADER, preferenceManager.getString(Constants.KEY_USED_ID));
        data.put(Constants.KEY_MEMBERS, receivers);
        data.put(Constants.KEY_TYPE_CHAT, typeChat);
        data.put(Constants.KEY_NAME, name);
        data.put(Constants.KEY_TYPE_MSG, typeMess);
        data.put(Constants.KEY_LAST_MESSAGE, content);
        data.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
        data.put(Constants.KEY_TIMESTAMP, Helpers.getNow());
        data.put(Constants.KEY_ID, chatRef.getId());
        chatRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "New Chat written with ID: " + chatRef.getId());
                        chatRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
                                    chat = task.getResult().toObject(Chat.class);

                                    DocumentReference messRef = db.collection(Constants.KEY_COLLECTION_CHAT).document(chatRef.getId())
                                            .collection(Constants.KEY_COLLECTION_MESSAGE).document();
                                    Map<String, Object> msg = new HashMap<>();
                                    msg.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USED_ID));
                                    msg.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_AVATAR));
                                    msg.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                    msg.put(Constants.KEY_TYPE_MSG, typeMess);
                                    msg.put(Constants.KEY_CONTENT, content);
                                    msg.put(Constants.KEY_TIMESTAMP, Helpers.getNow());
                                    msg.put(Constants.KEY_ID, messRef.getId());

                                    messRef.set(msg)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    viewInterface.onCreateAndSendSuccess(content, typeMess, pos);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    viewInterface.onSendError(pos);
                                                }
                                            });
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding new chat", e);
                        viewInterface.onSendError(pos);
                    }
                });
    }

    public void createAndSendPrivate(String receiveId, String content,String typeChat, String typeMess, int pos) {
//        APIServices.apiServices.createPriAndsend(token,receiveId,content,typeChat,typeMess)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<SendResponse>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposables.add( d);
//                    }
//
//                    @Override
//                    public void onNext(@NonNull SendResponse sendResponse) {
//                        chatNoLastMessObj = sendResponse.getData().getChat();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        e.printStackTrace();
//
//                        viewInterface.onSendError(pos);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.onCreateAndSendSuccess(content, typeMess, pos);
//                    }
//                });
    }

    public void send(String chatId, String content, String typeMess, int pos) {
        DocumentReference messRef = db.collection(Constants.KEY_COLLECTION_CHAT).document(chatId)
                .collection(Constants.KEY_COLLECTION_MESSAGE).document();
        Map<String, Object> msg = new HashMap<>();
        msg.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USED_ID));
        msg.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_AVATAR));
        msg.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
        msg.put(Constants.KEY_TYPE_MSG, typeMess);
        msg.put(Constants.KEY_CONTENT, content);
        msg.put(Constants.KEY_TIMESTAMP, Helpers.getNow());
        msg.put(Constants.KEY_ID, messRef);

        messRef.set(msg)
                .addOnSuccessListener(unused -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put(Constants.KEY_TYPE_MSG, typeMess);
                    data.put(Constants.KEY_LAST_MESSAGE, content);
                    data.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
                    data.put(Constants.KEY_TIMESTAMP, Helpers.getNow());
                    db.collection(Constants.KEY_COLLECTION_CHAT).document(chatId)
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    viewInterface.onSendSuccess(content, typeMess, pos);
                                }
                            });
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        viewInterface.onSendError(pos);
                    }
                });


    }
    public void createAndSendGroup(List<String> member, String name,String content, String typeChat, String typeMess, int pos) {
//        APIServices.apiServices.createGroupAndsend(token,member,name,content,typeChat, typeMess)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<SendResponse>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposables.add(d);
//                    }
//
//                    @Override
//                    public void onNext(@NonNull SendResponse sendResponse) {
//                        chatNoLastMessObj = sendResponse.getData().getChat();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        e.printStackTrace();
//
//                        viewInterface.onSendError(pos);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.onCreateAndSendSuccess(content, typeMess, pos);
//
//                    }
//                });
    }


    public void sendNotification(String messageBody){
        ApiClientFirebase.getClient().create(APIServices.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<String> call, @androidx.annotation.NonNull Response<String> response) {
                if(response.isSuccessful()){
                    try{
                        if(response.body() != null){
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if(responseJson.getInt("failure") == 1){
                                JSONObject error = (JSONObject) results.get(0);
//                                viewInterface.showToast(error.getString("error"));
                                return;
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    viewInterface.showToast("Gửi thông báo thành công");
                }else {
//                    viewInterface.showToast("Thất bại: Code" + response.code());
                    Log.d("demo", ""+ response.code());
                }
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<String> call, @androidx.annotation.NonNull Throwable t) {
//                viewInterface.showToast(t.getMessage());
            }
        });

    }

    public void listenChatInfo(String chatId, String type_chat, String receiverId) {

        db.collection(Constants.KEY_COLLECTION_CHAT)
                .document(chatId)
                .addSnapshotListener((value, e)->{
                    if (e != null){
                        viewInterface.onFindChatError();
                        return;
                    }
                    if (value == null || !value.exists()){
                        viewInterface.onChatNotExist();
                        return;
                    }
                    chat = value.toObject(Chat.class);
                    if (Objects.equals(type_chat, Constants.KEY_PRIVATE_CHAT)){
                        db.collection(Constants.KEY_COLLECTION_USERS)
                                .document(receiverId)
                                .addSnapshotListener((v,err)->{
                                    if (v == null || !v.exists()){
                                        return;
                                    }
                                    chat.setAvatar(v.getString(Constants.KEY_AVATAR));
                                    chat.setName(v.getString(Constants.KEY_NAME));
//                                    chat.setOnline(Boolean.FALSE.equals(v.getBoolean(Constants.KEY_ONLINE)));
                                    viewInterface.onUpdateInforSuccess(v.getString(Constants.KEY_NAME), v.getString(Constants.KEY_AVATAR), v.getBoolean(Constants.KEY_ONLINE));
                                });
                    }else
                        viewInterface.onUpdateInforSuccess(value.getString(Constants.KEY_NAME), value.getString(Constants.KEY_AVATAR), value.getBoolean(Constants.KEY_ONLINE));
                });

    }

    public void listenReceiverInfo(String id) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(id)
                .addSnapshotListener((value, e)->{
                    if (e != null){
                        viewInterface.onFindChatError();
                        return;
                    }
                    if (value == null || value.exists()){
                        viewInterface.onChatNotExist();
                        return;
                    }
                    viewInterface.onUpdateInforSuccess(value.getString(Constants.KEY_NAME), value.getString(Constants.KEY_AVATAR), value.getBoolean(Constants.KEY_ONLINE));

                });
    }

    public void updateOnlineStatus(String chatId, boolean b) {
//        db.collection(Constants.KEY_COLLECTION_CHAT)
//                .document(chatId)
//                .update(Constants.KEY_ONLINE,)
    }
}
