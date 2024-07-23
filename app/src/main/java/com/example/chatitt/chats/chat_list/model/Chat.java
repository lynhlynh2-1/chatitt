package com.example.chatitt.chats.chat_list.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Object> inchat = new HashMap<>();
    private Map<String, Object> online = new HashMap<>();
    private String avatar;
    private Map<String, String> fcm = new HashMap<>();

    public Chat(){
    }
    public Chat(String id, String name, String leader, List<String> members, String type_chat, String type_msg, String timestamp, String lastMessage, String senderName, Map<String, Object> inchat, String avatar, Map<String, String> fcm) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.type_chat = type_chat;
        this.type_msg = type_msg;
        this.timestamp = timestamp;
        this.lastMessage = lastMessage;
        this.senderName = senderName;
        this.inchat = inchat;
        this.avatar = avatar;
        this.fcm = fcm;
    }

    public Map<String, Object> getOnline() {
        return online;
    }

    public void updateOnline(String id, Boolean status) {
        if (this.online == null) this.online = new HashMap<>();
        this.online.put(id, status);
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }


    public Map<String, String> getFcm() {
        return fcm;
    }

    public void setFcm(Map<String, String> fcm) {
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

    public Map<String, Object> getInchat() {
        return inchat;
    }

    public void setInchat(Map<String, Object> inchat) {
        this.inchat = inchat;
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

    public void updateFCMUser(String id, String fcmToken) {
        if (this.fcm == null) this.fcm = new HashMap<>();
        this.fcm.put(id, fcmToken);
    }
}
