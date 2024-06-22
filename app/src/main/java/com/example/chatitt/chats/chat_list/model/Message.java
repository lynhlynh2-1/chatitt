package com.example.chatitt.chats.chat_list.model;

import com.example.chatitt.authentication.model.User;
import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Message implements Serializable {

//    private String id;
    private String chat;
    private String senderId;
    private String senderImage;
    private String senderName;
    private String content;
    private String type_msg;
    private String type_chat;
    private String timestamp;
    private String isSending = "2";
    //2: đã gửi, 1: đang gửi: 0: gửi thất bại

    public Message(){
    }
    public Message(String chat, User user, String content, String type_msg, String timestamp , String isSending) {
        this.chat = chat;
        this.content = content;
        this.type_msg = type_msg;
        this.timestamp = timestamp;
        this.isSending = isSending;
    }
    public Message(String senderId,String senderName, String senderImage, String content, String type, String timestamp , String isSending) {
        this.content = content;
        this.type_msg = type;
        this.timestamp = timestamp;
        this.isSending = isSending;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImage = senderImage;
    }

    public String getType_chat() {
        return type_chat;
    }

    public void setType_chat(String type_chat) {
        this.type_chat = type_chat;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType_msg() {
        return type_msg;
    }

    public void setType_msg(String type_msg) {
        this.type_msg = type_msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsSending() {
        return isSending;
    }

    public void setIsSending(String isSending) {
        this.isSending = isSending;
    }
}
