package com.example.chatitt.chats.group_chat.info.presenter;

import com.example.chatitt.authentication.model.User;

import java.util.List;

public class Contract {
    public interface AddMemViewInterface{

        void onSearchUserError();

        void onSearchUserSuccess(List<User> usersFind);

        void onGetListFriendSuccess();

        void onGetListFriendError();

        void onUserClicked();

        void onNoUser();
    }
    public interface ChangeGroupInfoViewInterface{

        void onUpdateSuccess();
        void onUpdateEror();

        void updateChatRealtime(String chatId, String newName, String newAvatar);

        void onDelMemEror();

        void onDelMemSuccess();

        void onChatInfoChanged(String name, String avatar);

        void onDelGroupSuccess();
    }

    public interface MemListViewInterface{

        void onAddMemSuccess();
        void onDelMemSuccess(int i);
        void onAddMemEror();
        void onDelMemEror();

        void onGetMemberError();
        void onGetMemberSuccess(User user);

        void deleteMember(String chatId, String id);

        void leaveChat(String userId);

        void onNewMemberAdded();

        void onMemInfoChangeSuccess(int i);

        void resetAdapter();
    }
}
