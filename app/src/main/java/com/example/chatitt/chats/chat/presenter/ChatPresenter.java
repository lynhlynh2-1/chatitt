package com.example.chatitt.chats.chat.presenter;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.net.http.HttpException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat.view.ChatActivity;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatPresenter {
//    private ChatContract.ViewInterface viewInterface ;
//    private final PreferenceManager preferenceManager;
//    private List<Message> messageList;
//    private Chat chat;
//    private ChatNoLastMessObj chatNoLastMessObj;
//    private FirebaseFirestore db;
//    private String conversionId = "";
//
//    public ChatPresenter(ChatContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
//        this.viewInterface = viewInterface;
//        this.preferenceManager = preferenceManager;
//        db = FirebaseFirestore.getInstance();
//
//    }
//
//    public String getConversionId() {
//        return conversionId;
//    }
//
//    public List<Message> getMessageList() {
//        return messageList;
//    }
//
//    public Chat getChat() {
//        return chat;
//    }
//
//    public ChatNoLastMessObj getChatNoLastMessObj() {
//        return chatNoLastMessObj;
//    }
//
//    public void setChat(Chat chat) {
//        this.chat = chat;
//    }
//
//    public void getMessages(String chatId){
//        APIServices.apiServices.getMessages(token,chatId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ListMessagesResponse>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposables.add( d);
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ListMessagesResponse listMessagesResponse) {
//                        messageList = listMessagesResponse.getData();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        viewInterface.onGetMessagesError();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.onGetMessagesSuccess();
//                    }
//                });
//    }
//
//
//    public void findChatById(String chatId) {
//        db.collection(Constants.KEY_COLLECTION_CHAT)
//                .document(chatId)
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
//                            return;
//                        }
//
//                        if (value != null && value.exists()) {
//                            Log.d(TAG, "Current data: " + value.getData());
//                            chat = value.toObject(Chat.class);
//                            viewInterface.onInfoChatChanged(chat);
//                        } else {
//                            Log.d(TAG, "Current data: null");
//                        }
//                    }
//                });
//    }
//
//    public void createChat(){
//
//    }
//
//    public void send(){
//
//    }
//    public void createAndSendPrivate(String receiveId, String content,String typeChat, String typeMess, int pos) {
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
//    }
//
//    public void send(String receiveId, String content,String typeChat, String typeMess, int pos) {
//        APIServices.apiServices.send(token,receiveId,content,typeChat,typeMess)
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
//                        viewInterface.onSendError(pos);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        viewInterface.onSendSuccess(content, typeMess, pos);
//
//                    }
//                });
//
//    }
//    public void createAndSendGroup(List<String> member, String name,String content, String typeChat, String typeMess, int pos) {
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
//    }
//
//    public void sendRealtime(String content, String typeMess, String userId, String room) {
//        // Gửi emit
//        JSONObject data = new JSONObject();
//        try {
//            data.put("msg", content);
//            data.put("typeMess", typeMess);
//            data.put("userId", userId);
//            data.put("room", room);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (socket != null) {
//            socket.emit("chatMessage", data);
//        } else {
//            // Xử lý trường hợp Socket.IO object là null
//        }
//    }
//    public void sendNotification(String messageBody){
//        ApiClientFirebase.getClient().create(APIServices.class).sendMessage(
//                Constants.getRemoteMsgHeaders(),
//                messageBody
//        ).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(@androidx.annotation.NonNull Call<String> call, @androidx.annotation.NonNull Response<String> response) {
//                if(response.isSuccessful()){
//                    try{
//                        if(response.body() != null){
//                            JSONObject responseJson = new JSONObject(response.body());
//                            JSONArray results = responseJson.getJSONArray("results");
//                            if(responseJson.getInt("failure") == 1){
//                                JSONObject error = (JSONObject) results.get(0);
//                                viewInterface.showToast(error.getString("error"));
//                                return;
//                            }
//                        }
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//                    viewInterface.showToast("Gửi thông báo thành công");
//                }else {
//                    viewInterface.showToast("Thất bại: Code" + response.code());
//                    Log.d("demo", ""+ response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(@androidx.annotation.NonNull Call<String> call, @androidx.annotation.NonNull Throwable t) {
//
//                viewInterface.showToast(t.getMessage());
//            }
//        });
//
//    }
}
