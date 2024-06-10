package com.example.chatitt.chats.chat_list.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChatListPresenter {
    private final ChatListContract.ViewInterface viewInterface;
    private final PreferenceManager preferenceManager;
    private List<Chat> chatList;
    private List<User> userModelList;



    public ChatListPresenter(ChatListContract.ViewInterface viewInterface, PreferenceManager preferenceManager) {
        this.viewInterface = viewInterface;
        this.preferenceManager = preferenceManager;

        JSONObject data = new JSONObject();

    }


    public List<Chat> getChatList() {
        return chatList;
    }

    public List<User> getUserModelList() {
        return userModelList;
    }

    public void joinChat(String userId, String username,String avatar, String chatId, String typeRoom, String publicKey){
    }
    public void registerOnLeaveChatEvent(){
        // Đăng ký lắng nghe sự kiện
    }
    public void registerOnNewMemberAddEvent(){
        // Đăng ký lắng nghe sự kiện
    }
    public void registerOnUpdateChatEvent(){
        // Đăng ký lắng nghe sự kiện
    }
    public void registerOnAddedChatEvent(){
        // Đăng ký lắng nghe sự kiện
    }
    public void registerOnMessageEvent(){
        // Đăng ký lắng nghe sự kiện
    }
    public void registerOnCreateRoomEvent(){
        // Đăng ký lắng nghe sự kiện
    }
    public void getMessaged(){

    }

}
