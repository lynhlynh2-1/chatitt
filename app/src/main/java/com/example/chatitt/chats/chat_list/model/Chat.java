package com.example.chatitt.chats.chat_list.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Chat implements Serializable {
    private String id;
    private String name;
    private String leader;
    private List<String> members;
    private String type_chat;
    private String type_msg;
    private String timestamp;
    private String lastMessage;
    private String senderName;
    private boolean online;
    private String avatar;
    private List<String> fcm;

    public Chat(){
    }
    public Chat(String id, String name, String leader, List<String> members, String type_chat, String type_msg, String timestamp, String lastMessage, String senderName, boolean online, String avatar, List<String> fcm) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.type_chat = type_chat;
        this.type_msg = type_msg;
        this.timestamp = timestamp;
        this.lastMessage = lastMessage;
        this.senderName = senderName;
        this.online = online;
        this.avatar = avatar;
        this.fcm = fcm;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }


    public List<String> getFcm() {
        return fcm;
    }

    public void setFcm(List<String> fcm) {
        this.fcm = fcm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> member) {
        this.members = member;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getType_chat() {
        return type_chat;
    }

    public void setType_chat(String type_chat) {
        this.type_chat = type_chat;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
