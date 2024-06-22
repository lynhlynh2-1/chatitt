package com.example.chatitt.chats.chat.presenter;

import com.example.chatitt.chats.chat_list.model.Chat;

public class ChatContract {
    public interface ViewInterface{
        void onChatNotExist();
        void onUpdateInforSuccess(String name, String avatar, Boolean online);
        void onGetMessagesSuccess();
        void onGetMessagesError();

        void onFindChatError();

        void onSendError(int pos);

        void onSendSuccess(String content, String typeMess, int pos);
        void onCreateAndSendSuccess(String content, String typeMess, int pos);

        void receiveNewMsgRealtime(String userId,String username,String avatar,String room,String typeRoom,String publicKey,String text,String typeMess, String time);


        void onShowImageClick(String content);

        void onFindChatByIdSucces();

        void onFindChatByIdError();

        void updateChatRealtime(String chatId, String name, String avatar);

        void onInfoChatChanged(Chat chat);

        void updateMessList(int count);
    }
}
