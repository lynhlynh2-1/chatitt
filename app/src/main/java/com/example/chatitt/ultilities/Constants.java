package com.example.chatitt.ultilities;


import java.util.HashMap;

public class Constants {

    public static final String KEY_PREFERENCE_NAME = "ChatItt";
    public static final String TAG = "lynh.pham";

    public static final String KEY_NOT_FRIEND = "0";
    public static final String KEY_IS_FRIEND = "1";
    public static final String KEY_MY_REQ_FRIEND = "2";
    public static final String KEY_OTHER_REQ_FRIEND = "3";



    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS_CITY = "city";
    public static final String KEY_ADDRESS_COUNTRY = "country";
    public static final String KEY_ADDRESS_DETAIL = "address";
    public static final String KEY_PHONE = "phonenumber";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USED_ID = "userId";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_COVERIMAGE = "coverImage";
    public static final String KEY_COVERIMAGE_DEFAULT = "coverImage";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER = "user";
    public static final String KEY_ROOM = "room";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_COLLECTION_CHAT_NO_LMSG = "chatNoLastMsg";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_MEMBERS = "members";
    public static final String KEY_LEADER = "leader";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String MESS_SENDER_ID = "messSenderId";
    public static final String MESS_RECEIVER_ID = "messReceiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_COLLECTION_MESSAGE = "messages";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String isSeen = "isSeen";
    public static final String KEY_COLLECTION_CONVERSATIONS = "chats";

    public static final String KEY_CONVERSATION_ID = "conversationID_it";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_ONLINE = "online";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-type";

    public static final String REMOTE_MSG_DATA = "data";
    public static final String TYPE_MESSAGES_SEND = "type";
    public static final String KEY_TYPE = "type";
//    public static final List<UserGroup> userGroups = new ArrayList<>();
//    public static final UserGroup userCurrent = new UserGroup();
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String FRIEND_LIST = "friend_list";
    public static final String MY_REQ_FRIEND_LIST = "friend_list";
    public static final String OTHER_REQ_FRIEND_LIST = "friend_list";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TYPE_CHAT = "type_chat";
    public static final String KEY_TYPE_MSG = "type_msg";
    public static final String KEY_ID = "id";

    public static HashMap<String, String> remotoMsgHeaders = null;

    public static final String KEY_PRIVATE_CHAT = "PRIVATE_CHAT";
    public static final String KEY_GROUP_CHAT = "GROUP_CHAT";
    public static final String KEY_TYPE_TEXT = "TEXT";
    public static final String KEY_TYPE_IMAGE = "IMAGE";
//    public static final String BASE_URL = "http://192.168.50.128:8000/api/v1/";
    public static final String BASE_URL = "http://10.0.2.2:8000/api/v1/";
//    public static final String BASE_URL_SOCKET = "http://192.168.50.128:16000";
    public static final String BASE_URL_SOCKET = "http://10.0.2.2:16000";

    public static HashMap<String, String> getRemoteMsgHeaders(){
        if(remotoMsgHeaders == null){
            remotoMsgHeaders = new HashMap<>();
            remotoMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAA8b1Nfcw:APA91bGpmhEG-f5QAEVArQGsXLRhTA-oPurgmE7mUFOj2nnlZDbY23SpKnQByqLRRUhGfCPT6qOv2JtbtVGbTom3eWmZ7jQDFfNdAfleQxB8Libb9O_dL1UXItm10gZomOo2EbbXH47x"
            );
            remotoMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remotoMsgHeaders;
    }


}
