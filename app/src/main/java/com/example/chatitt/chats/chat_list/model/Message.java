package com.example.chatitt.chats.chat_list.model;

import com.example.chatitt.authentication.model.User;

import java.io.Serializable;

public class Message implements Serializable {

    private String id;
    private String chat;
    private Chat chatObject;
    private User user;
    private String content;
    private String type;
    private String createdAt;
    private String updatedAt;
    private String isSending = "2";
    //2: đã gửi, 1: đang gửi: 0: gửi thất bại

    public Message(String chat, User user, String content, String type, String updatedAt , String isSending) {
        this.chat = chat;
        this.user = user;
        this.content = content;
        this.type = type;
        this.updatedAt = updatedAt;
        this.isSending = isSending;
    }
    public Message(User user, String content, String type, String updatedAt , String isSending) {
        this.content = content;
        this.type = type;
        this.updatedAt = updatedAt;
        this.isSending = isSending;
        this.user = user;
    }
    public String isSending() {
        return isSending;
    }

    public void setSending(String sending) {
        isSending = sending;
    }

    public Chat getChatObject() {
        return chatObject;
    }

    public void setChatObject(Chat chatObject) {
        this.chatObject = chatObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
