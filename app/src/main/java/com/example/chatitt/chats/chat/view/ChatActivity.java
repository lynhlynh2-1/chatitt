package com.example.chatitt.chats.chat.view;

import static com.example.chatitt.ultilities.Constants.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat.presenter.ChatContract;
import com.example.chatitt.chats.chat.presenter.ChatPresenter;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.databinding.ActivityChatBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

public class ChatActivity extends AppCompatActivity {

//    private ActivityChatBinding binding;
//    private ChatPresenter chatPresenter;
//    private PreferenceManager preferenceManager;
//    private Chat chat;
//    private ChatNoLastMessObj chatNoLastMessObj;
//    private User userModel;
//
//    private List<Message> messageList;
//    private ChatAdapter chatAdapter;
//    private boolean isSearchShow = false;
//
//    private ArrayList<String> receivedList;
//    private String group_name;
//    private SecretKey secretKey = null;
//    private String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityChatBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        Helpers.setupUI(binding.getRoot(),this);
//
//        init();
//        setListener();



    }
//    private void init(){
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        chatPresenter = new ChatPresenter(this, preferenceManager);
//
//        messageList = new ArrayList<>();
//
//        binding.shimmerEffect.startShimmerAnimation();
//
//        if(getIntent().getStringExtra(Constants.KEY_COLLECTION_CHAT) != null){
//            chatPresenter.findChatById();
//            Log.e(TAG, "ChatId Existed");
//            binding.textName.setText(getIntent().getStringExtra(Constants.KEY_NAME));
//            binding.imageInfo.setImageResource(R.drawable.cover_img_placeholder);
//            binding.textOnline.setText( "Đang hoạt động");
//            binding.textOnline.setTextColor( getResources().getColor(R.color.green) );
//            chatPresenter.findChatById(getIntent().getStringExtra("ChatId"));
//        }
//
//        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);
//        userModel = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
//
//        if (chat != null){
////            chatPresenter.joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), chat.getId(), chat.getType(), preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
//            binding.textName.setText(chat.getName());
//            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
//            binding.textOnline.setText(Objects.equals(chat.getOnline(), "1") ? "Đang hoạt động": "Ngoại tuyến");
//            binding.textOnline.setTextColor(Objects.equals(chat.getOnline(), "1") ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
//            chatPresenter.getMessages(chat.getId());
//            chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chat.getType(), secretKey, this);
//
//        }else if (userModel != null){
//            binding.textName.setText(userModel.getName());
//            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
//            binding.textOnline.setText(Objects.equals(userModel.getOnline(), "1") ? "Đang hoạt động": "Ngoại tuyến");
//            binding.textOnline.setTextColor(Objects.equals(userModel.getOnline(), "1") ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
//            chatPresenter.findChat(userModel.getId());
//        }else if (getIntent().getExtras()!=null){
//            receivedList = getIntent().getExtras().getStringArrayList("create_group_chat");
//            group_name = getIntent().getStringExtra(Constants.KEY_NAME);
//            binding.textName.setText(group_name);
//            binding.textOnline.setText("Ngoại tuyến");
//            binding.textOnline.setTextColor( getResources().getColor(R.color.seed) );
//            binding.shimmerEffect.stopShimmerAnimation();
//            binding.shimmerEffect.setVisibility(View.GONE);
//            binding.usersRecyclerView.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//
//
//    private void setListener() {
//        binding.imageBack.setOnClickListener(v->{
//            onBackPressed();
//        });
//        binding.searchBtn.setOnClickListener(v->{
//            if (isSearchShow){
//                binding.searchFrame.setVisibility(View.GONE);
//            }else {
//                binding.searchFrame.setVisibility(View.VISIBLE);
//            }
//            isSearchShow= !isSearchShow;
//        });
//        binding.imageInfo.setOnClickListener(v->{
//            Intent it;
//
//            if (chat!=null ){
//                if (Objects.equals(chat.getType(), Constants.KEY_PRIVATE_CHAT)){
//                    it = new Intent(getApplicationContext(), PrivateChatInfoActivity.class);
//                }else {
//                    it = new Intent(getApplicationContext(), GroupChatInfoActivity.class);
//                }
//                it.putExtra(Constants.KEY_COLLECTION_CHAT, chat);
//                mStartForResult.launch(it);
//            }else if(chatNoLastMessObj != null){
//                if (Objects.equals(chatNoLastMessObj.getType(), Constants.KEY_PRIVATE_CHAT)){
//                    it = new Intent(getApplicationContext(), PrivateChatInfoActivity.class);
//                }else {
//                    it = new Intent(getApplicationContext(), GroupChatInfoActivity.class);
//                }
//                it.putExtra(Constants.KEY_COLLECTION_CHAT_NO_LMSG, chatNoLastMessObj);
//                mStartForResult.launch(it);
//            }else{
//                Toast.makeText(getApplicationContext(),"Hãy gửi tin nhắn đầu tiên để xem thông tin", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//        binding.camBtn.setOnClickListener(v->{
//            ImagePicker.with(this)
//                    .cameraOnly()
//                    .crop()//Crop image(Optional), Check Customization for more option
//                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                    .createIntent(intent -> {
//                        startForImageResult.launch(intent);
//                        return null;
//                    });
//        });
//        binding.galleryBtn.setOnClickListener(v->{
//            ImagePicker.with(this)
//                    .galleryOnly()
//                    .crop()//Crop image(Optional), Check Customization for more option
//                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                    .createIntent(intent -> {
//                        startForImageResult.launch(intent);
//                        return null;
//                    });
//        });
//        binding.sendBtn.setOnClickListener(v->{
//            String content = binding.inputMessage.getText().toString().trim();
//            if (content.isEmpty()){
//                return;
//            }
//
//            send(content, Constants.KEY_TYPE_TEXT);
//            binding.inputMessage.setText("");
//        });
//        binding.sendImgBtn.setOnClickListener(v->{
//            send(encodedImage, Constants.KEY_TYPE_IMAGE);
//            binding.containerText.setVisibility(View.VISIBLE);
//            binding.containerImg.setVisibility(View.GONE);
//        });
//        binding.exitImg.setOnClickListener(v->{
//            binding.containerText.setVisibility(View.VISIBLE);
//            binding.containerImg.setVisibility(View.GONE);
//        });
//        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(!TextUtils.isEmpty(query.trim())){
//                    List<Message> messages = Helpers.checkStringMessageContain(query, messageList, secretKey);
//                    chatAdapter.reset(messages);
//                }else{
//                    chatAdapter.reset(messageList);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if(!TextUtils.isEmpty(newText.trim())){
//                    List<Message> messages = Helpers.checkStringMessageContain(newText, messageList, secretKey);
//                    chatAdapter.reset(messages);
//                }else{
//                    chatAdapter.reset(messageList);
//                }
//                return false;
//            }
//        });
//        binding.inputMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                final Handler handler = new Handler();
//                if (hasFocus) {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            TransitionManager.beginDelayedTransition(binding.containerInput, new AutoTransition());
//                            binding.camBtn.setVisibility(View.GONE);
//                            binding.galleryBtn.setVisibility(View.GONE);
//                        }
//                    }, 200);
//
//                }
//                else {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            TransitionManager.beginDelayedTransition(binding.containerInput, new AutoTransition());
//                            binding.camBtn.setVisibility(View.VISIBLE);
//                            binding.galleryBtn.setVisibility(View.VISIBLE);
//                        }
//                    }, 200);
//
//                }
//            }
//        });
//    }
//
//    private void send(String content, String type) {
//
//        if (chat!=null){
//            if (Objects.equals(chat.getType(), Constants.KEY_PRIVATE_CHAT)){
//                content = ECCc.encryptString(secretKey,content);
//            }
//            chatPresenter.send(chat.getId(),content, chat.getType(),  type, messageList.size());
//        }else if (chatNoLastMessObj != null){
//            if (Objects.equals(chatNoLastMessObj.getType(), Constants.KEY_PRIVATE_CHAT)){
//                content = ECCc.encryptString(secretKey,content);
//            }
//            chatPresenter.send(chatNoLastMessObj.getId(),content, chatNoLastMessObj.getType(),  type, messageList.size());
//        }else if (userModel != null){
//            content = ECCc.encryptString(secretKey, content);
//            chatPresenter.createAndSendPrivate(userModel.getId(),content, Constants.KEY_PRIVATE_CHAT, type, messageList.size());
//            chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), Constants.KEY_PRIVATE_CHAT, secretKey, this);
//            binding.usersRecyclerView.setAdapter(chatAdapter);
//            binding.usersRecyclerView.setVisibility(View.VISIBLE);
//        }else if(receivedList != null){
//            chatPresenter.createAndSendGroup(receivedList, group_name, content, Constants.KEY_GROUP_CHAT, type, messageList.size());
//            chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), Constants.KEY_GROUP_CHAT, secretKey, this);
//            binding.usersRecyclerView.setAdapter(chatAdapter);
//        }
//        User userModel1 = new User(preferenceManager.getString(Constants.KEY_USED_ID));
//        Message message = new Message(userModel1,content,type, Helpers.getNow() ,"1");
//        messageList.add(message);
//        chatAdapter.notifyItemRangeInserted(messageList.size(),messageList.size());
//        binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);
//
//    }
//
//
//    private final ActivityResultLauncher<Intent> startForImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                Intent data = result.getData();
//                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
//                    Uri resultUri = data.getData();
//
//                    binding.imageContent.setImageURI(resultUri);
//                    binding.containerText.setVisibility(View.GONE);
//                    binding.containerImg.setVisibility(View.VISIBLE);
//
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(resultUri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        encodedImage = Helpers.encodeSentImage(bitmap);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
//                    Toast.makeText(getApplicationContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(getApplicationContext(), "Hủy chọn ảnh!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//    @Override
//    public void onChatNotExist() {
////        chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), "PRIVATE_CHAT");
////        binding.usersRecyclerView.setAdapter(chatAdapter);
//        binding.usersRecyclerView.setVisibility(View.VISIBLE);
//        binding.shimmerEffect.stopShimmerAnimation();
//        binding.shimmerEffect.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onFindChatSucces() {
//        chat = chatPresenter.getChatNoLastMessObj();
//        if (secretKey == null) getSharedKey(chatNoLastMessObj.getPublicKey());
////        chatPresenter.joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), chatNoLastMessObj.getId(), chatNoLastMessObj.getType(), preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
//        chatPresenter.getMessages(chatNoLastMessObj.getId());
//        chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chatNoLastMessObj.getType(), secretKey, this);
//
//    }
//    @Override
//    public void onFindChatError() {
//        binding.shimmerEffect.stopShimmerAnimation();
//        binding.shimmerEffect.setVisibility(View.GONE);
//        Toast.makeText(getApplicationContext(),"Lỗi khi tìm đoạn chat", Toast.LENGTH_SHORT).show();
//        finish();
//    }
//    @Override
//    public void onGetMessagesSuccess() {
//
//        messageList = chatPresenter.getMessageList();
//        chatAdapter.resetData(messageList);
//        binding.shimmerEffect.stopShimmerAnimation();
//        binding.shimmerEffect.setVisibility(View.GONE);
//        binding.usersRecyclerView.setAdapter(chatAdapter);
//        binding.usersRecyclerView.setVisibility(View.VISIBLE);
//        binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                // call the invalidate()
////                binding.usersRecyclerView.smoothScrollToPosition(0);
////            }
////        });
//
//    }
//
//    @Override
//    public void onGetMessagesError() {
//        Toast.makeText(getApplicationContext(),"Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
//    }
//
//
//
//    @Override
//    public void onSendError(int pos) {
//        Toast.makeText(getApplicationContext(),"Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show();
//        messageList.get(pos).setSending("0");
//        chatAdapter.notifyItemChanged(pos);
//    }
//
//    @Override
//    public void onSendSuccess(String content, String typeMess, int pos) {
//
//        chatNoLastMessObj = chatPresenter.getChatNoLastMessObj();
//
////        chatPresenter.getMessages(chatNoLastMessObj.getId());
//        messageList.get(pos).setSending("2");
//        chatAdapter.notifyItemChanged(pos);
//        chatPresenter.sendRealtime(content,typeMess, preferenceManager.getString(Constants.KEY_USED_ID), chatNoLastMessObj.getId());
//
//
//        sendNoti(content,typeMess, chatNoLastMessObj.getName(), chatNoLastMessObj.getType());
//
//    }
//
//
//    @Override
//    public void onCreateAndSendSuccess(String content, String typeMess, int pos) {
//
//        chatNoLastMessObj = chatPresenter.getChatNoLastMessObj();
//        messageList.get(pos).setSending("2");
//        chatAdapter.notifyItemChanged(pos);
//
//        chatPresenter.joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), chatNoLastMessObj.getId(), chatNoLastMessObj.getType(), preferenceManager.getString(Constants.KEY_PUBLIC_KEY));        // Tạo danh sách userId ví dụ
//        List<String> userIdList = new ArrayList<>();
//        if (Objects.equals(chatNoLastMessObj.getType(), Constants.KEY_GROUP_CHAT)){
//            userIdList = receivedList;
//        }else{
//            userIdList.add(chatNoLastMessObj.getMember().get(0));
//        }
//        userIdList.add(preferenceManager.getString(Constants.KEY_USED_ID));
//        chatPresenter.createChat(userIdList);
//        chatPresenter.sendRealtime(content,typeMess, preferenceManager.getString(Constants.KEY_USED_ID), chatNoLastMessObj.getId());
//
//        sendNoti(content,typeMess,chatNoLastMessObj.getName(),chatNoLastMessObj.getType());
//    }
//
//    private void sendNoti(String content, String typeMess, String group_name, String type_chat){
//        try {
//            JSONArray tokens = new JSONArray(chatNoLastMessObj.getFcm());
//
//            JSONObject data = new JSONObject();
//            data.put("ChatId", chatNoLastMessObj.getId());
//            data.put(Constants.KEY_NAME, group_name);
//            data.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
//            data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
//            if (Objects.equals(typeMess, Constants.KEY_TYPE_TEXT))
//                data.put(Constants.KEY_MESSAGE, content);
//            data.put(Constants.TYPE_MESSAGES_SEND, typeMess);
//            data.put(Constants.KEY_PUBLIC_KEY, preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
//            data.put("TypeChat", type_chat);
//
//
//
//            JSONObject body = new JSONObject();
//            body.put(Constants.REMOTE_MSG_DATA,data);
//            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
//
//            Log.d("Send Noti Request", body.toString());
//
//            chatPresenter.sendNotification(body.toString());
//        }catch (Exception e){
//            showToast(e.getMessage());
//        }
//    }
//    @Override
//    public void receiveNewMsgRealtime(String userId,String username , String avatar,String room,String typeRoom,String publicKey,String text,String typeMess, String time) {
//        User userModel1 = new User(userId, username, avatar);
//        Message message = new Message(room,userModel1,text,typeMess,time , "2");
//        messageList.add(message);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // call the invalidate()
//                chatAdapter.notifyItemRangeInserted(messageList.size(),messageList.size());
//                binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);
//            }
//        });
////        chatAdapter.addData();
//
//    }
//
//    @Override
//    public void updateChatRealtime(String chatId, String name, String avatar) {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                final Toast toast = Toast.makeText(getApplicationContext(), "Thông tin nhóm vừa được thay đổi!", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
//        if (name != null){
//            if (chat != null && chat.getId().equals(chatId))
//                chat.setName(name);
//            else if (chatNoLastMessObj != null && chatNoLastMessObj.getId().equals(chatId))
//                chatNoLastMessObj.setName(name);
//            binding.textName.setText(name);
//        }
//        if (avatar != null){
//            if (chat != null && chat.getId().equals(chatId))
//                chat.setAvatar(avatar);
//            else if (chatNoLastMessObj != null && chatNoLastMessObj.getId().equals(chatId))
//                chatNoLastMessObj.setAvatar(avatar);
//            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(avatar));
//        }
//
//    }
//
//
//    @Override
//    public void onShowImageClick(String content) {
//        Intent it = new Intent(this, ShowImageActivity.class);
//        it.putExtra(Constants.KEY_TYPE_IMAGE, content);
//        startActivity(it);
//    }
//
//    @Override
//    public void onFindChatByIdSucces() {
//        chat = chatPresenter.getChat();
//        binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
//        chatPresenter.getMessages(chat.getId());
//        chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chat.getType(), secretKey, this);
//    }
//
//    @Override
//    public void onFindChatByIdError() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                final Toast toast = Toast.makeText(getApplicationContext(), "Lấy thông tin thất bại!", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
//        finish();
//    }
//
//    @Override
//    public void onInfoChatChanged(Chat chat) {
//        binding.textName.setText(chat.getName());
//    }
//
//    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent intent = result.getData();
//                        // Handle the Intent
//                        String newName = intent != null ? intent.getStringExtra("name") : null;
//                        String newAvatar = intent != null ? intent.getStringExtra("avatar") : null;
//                        if (chat!= null){
//                            if (newName != null) chat.setName(newName);
//                            if (newAvatar != null) chat.setAvatar(newAvatar);
//                            binding.textName.setText(chat.getName());
//                            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
//                        }else if (chatNoLastMessObj!= null){
//
//                            if (newName != null) chatNoLastMessObj.setName(newName);
//                            if (newAvatar != null) chatNoLastMessObj.setAvatar(newAvatar);
//                            binding.textName.setText(chatNoLastMessObj.getName());
//                            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chatNoLastMessObj.getAvatar()));
//                        }
//                    }
//                }
//            });
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (chat!= null){
//            binding.textName.setText(chat.getName());
//            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
//        }else if (chatNoLastMessObj!= null){
//            binding.textName.setText(chatNoLastMessObj.getName());
//            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chatNoLastMessObj.getAvatar()));
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        if(chat!= null){
////            chatPresenter.leaveChat(preferenceManager.getString(Constants.KEY_NAME),chat.getId());
////        }else if ( chatNoLastMessObj != null)
////            chatPresenter.leaveChat(preferenceManager.getString(Constants.KEY_NAME),chatNoLastMessObj.getId());
//    }
}