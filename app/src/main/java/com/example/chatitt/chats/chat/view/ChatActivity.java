package com.example.chatitt.chats.chat.view;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.chatitt.ultilities.Constants.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
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

import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordPermissionHandler;
import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat.presenter.ChatContract;
import com.example.chatitt.chats.chat.presenter.ChatPresenter;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.model.Message;
import com.example.chatitt.chats.group_chat.info.view.GroupChatInfoActivity;
import com.example.chatitt.chats.individual_chat.info.view.PrivateChatInfoActivity;
import com.example.chatitt.databinding.ActivityChatBinding;
import com.example.chatitt.firebase.AccessToken;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.Permissions;
import com.example.chatitt.ultilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity implements ChatContract.ViewInterface{

    private ActivityChatBinding binding;
    private ChatPresenter chatPresenter;
    private PreferenceManager preferenceManager;
    private Chat chat;
    private User userModel;
    private List<Message> messageList;
    private ChatAdapter chatAdapter;
    private boolean isSearchShow = false;
    private ArrayList<String> receivedList;
    private String group_name;
    private String encodedImage;
    private Uri imageUri;
    private MediaRecorder mediaRecorder;
    private String audioPath;
    private Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(),this);

        binding.recordButton.setRecordView(binding.recordView);
        binding.recordButton.setListenForRecord(false);

        init();
        setListener();
    }
    private void init(){
        permissions = new Permissions();
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatPresenter = new ChatPresenter(this, preferenceManager);

        messageList = new ArrayList<>();

        binding.shimmerEffect.startShimmerAnimation();

        chat = (Chat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_CHAT);

        userModel = (User) getIntent().getSerializableExtra(Constants.KEY_USER);

        String chatId = getIntent().getStringExtra("ChatId");
        String nameChat = getIntent().getStringExtra(Constants.KEY_NAME);

        if(chatId != null){
            chatPresenter.findChat(chatId);
            binding.textName.setText(nameChat);
        }

        if (chat != null){
            String receivedId = (String) getIntent().getSerializableExtra(Constants.KEY_RECEIVER_ID);
            if (chat.getName().equals("Han")){
                Log.d(TAG, "init(): " + chat.getName());
            }
            binding.textName.setText(chat.getName());
            if (chat.getAvatar() != null && !chat.getAvatar().isEmpty()){
                binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
            }
            binding.textOnline.setText(chat.getOnline().containsValue(true) ? "Đang hoạt động": "Ngoại tuyến");
            binding.textOnline.setTextColor(chat.getOnline().containsValue(true) ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
            chatPresenter.getMessages(chat.getId());
            chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chat.getType_chat(), this);
            List<String> memIds = new ArrayList<>(chat.getMembers());
            memIds.add(chat.getLeader());
            chatPresenter.listenChatInfo(chat.getId(), chat.getType_chat(), receivedId, memIds);
        }else if (userModel != null){
            binding.textName.setText(userModel.getName());
            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textOnline.setText(userModel.getOnline() ? "Đang hoạt động": "Ngoại tuyến");
            binding.textOnline.setTextColor(userModel.getOnline() ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
//            chatPresenter.findChat(userModel.getId());
            binding.shimmerEffect.stopShimmerAnimation();
            binding.shimmerEffect.setVisibility(View.GONE);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
            chatPresenter.listenReceiverInfo(userModel.getId());
        }else if (getIntent().getExtras()!=null){
            receivedList = getIntent().getExtras().getStringArrayList("create_group_chat");
            group_name = getIntent().getStringExtra(Constants.KEY_NAME);
            binding.textName.setText(group_name);
            binding.textOnline.setText("Ngoại tuyến");
            binding.textOnline.setTextColor( getResources().getColor(R.color.seed) );
            binding.shimmerEffect.stopShimmerAnimation();
            binding.shimmerEffect.setVisibility(View.GONE);
            binding.usersRecyclerView.setVisibility(View.VISIBLE);
        }

    }



    private void setListener() {
        binding.imageBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.recordButton.setOnClickListener(view -> {
            if (chat.getId() != null) {
                if (permissions.isRecordingOk(ChatActivity.this))
                    if (permissions.isStorageOk(ChatActivity.this))
                        binding.recordButton.setListenForRecord(true);
                    else permissions.requestStorage(ChatActivity.this);
                else permissions.requestRecording(ChatActivity.this);
            } else {
                Toast.makeText(ChatActivity.this, "Gửi tin nhắn văn bản trước khi thực hiện tác vụ này!!!", Toast.LENGTH_SHORT).show();
            }        });
        binding.recordView.setRecordPermissionHandler(new RecordPermissionHandler() {
            @Override
            public boolean isPermissionGranted() {

                boolean recordPermissionAvailable = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED;
                if (recordPermissionAvailable) {
                    return true;
                }

                ActivityCompat.
                        requestPermissions(ChatActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                0);

                return false;

            }
        });
        binding.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                binding.containerInput.setVisibility(View.INVISIBLE);
                Log.d("RecordView", "onStart");

                setUpRecording();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                Log.d("RecordView", "onCancel");

                binding.containerInput.setVisibility(View.VISIBLE);

                mediaRecorder.reset();
                mediaRecorder.release();
                File file = new File(audioPath);
                if (file.exists())
                    file.delete();


            }

            @Override
            public void onFinish(long recordTime, boolean limitReached) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.
                Log.d("RecordView", "onFinish");

                binding.containerInput.setVisibility(View.VISIBLE);

                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    sendRecodingMessage(audioPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond");

                binding.containerInput.setVisibility(View.VISIBLE);

                mediaRecorder.reset();
                mediaRecorder.release();

                File file = new File(audioPath);
                if (file.exists())
                    file.delete();
            }

            @Override
            public void onLock() {
                //When Lock gets activated
                Log.d("RecordView", "onLock");
            }

        });

//        binding.recordButton.setListenForRecord(false);
//
//        //ListenForRecord must be false ,otherwise onClick will not be called
//        binding.recordButton.setOnRecordClickListener(new OnRecordClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ChatActivity.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
//                Log.d("RecordButton","RECORD BUTTON CLICKED");
//            }
//        });

        binding.searchBtn.setOnClickListener(v->{
            if (isSearchShow){
                binding.searchFrame.setVisibility(View.GONE);
            }else {
                binding.searchFrame.setVisibility(View.VISIBLE);
            }
            isSearchShow= !isSearchShow;
        });
        binding.imageInfo.setOnClickListener(v->{
            Intent it;
            if (chat!=null ){
                if (Objects.equals(chat.getType_chat(), Constants.KEY_PRIVATE_CHAT)){
                    it = new Intent(getApplicationContext(), PrivateChatInfoActivity.class);
                }else {
                    it = new Intent(getApplicationContext(), GroupChatInfoActivity.class);
                }
                it.putExtra(Constants.KEY_COLLECTION_CHAT, chat);
                mStartForResult.launch(it);
            }
            else{
                Toast.makeText(getApplicationContext(),"Hãy gửi tin nhắn đầu tiên để xem thông tin", Toast.LENGTH_SHORT).show();
            }

        });
        binding.camBtn.setOnClickListener(v->{
            ImagePicker.with(this)
                    .cameraOnly()
                    .crop()//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(intent -> {
                        startForImageResult.launch(intent);
                        return null;
                    });
        });
        binding.galleryBtn.setOnClickListener(v->{
            ImagePicker.with(this)
                    .galleryOnly()
                    .crop()//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(intent -> {
                        startForImageResult.launch(intent);
                        return null;
                    });
        });
        binding.sendBtn.setOnClickListener(v->{
            String content = binding.inputMessage.getText().toString().trim();
            if (content.isEmpty()){
                return;
            }

            send(content, Constants.KEY_TYPE_TEXT);
            binding.inputMessage.setText("");
        });
        binding.sendImgBtn.setOnClickListener(v->{
            sendImg();
            binding.containerText.setVisibility(View.VISIBLE);
            binding.containerImg.setVisibility(View.GONE);
        });
        binding.exitImg.setOnClickListener(v->{
            binding.containerText.setVisibility(View.VISIBLE);
            binding.containerImg.setVisibility(View.GONE);
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    List<Message> messages = Helpers.checkStringMessageContain(query, messageList);
                    chatAdapter.reset(messages);
                }else{
                    chatAdapter.reset(messageList);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim())){
                    List<Message> messages = Helpers.checkStringMessageContain(newText, messageList);
                    chatAdapter.reset(messages);
                }else{
                    chatAdapter.reset(messageList);
                }
                return false;
            }
        });
        binding.inputMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final Handler handler = new Handler();
                if (hasFocus) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TransitionManager.beginDelayedTransition(binding.containerInput, new AutoTransition());
                            binding.camBtn.setVisibility(View.GONE);
                            binding.galleryBtn.setVisibility(View.GONE);
                        }
                    }, 200);

                }
                else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TransitionManager.beginDelayedTransition(binding.containerInput, new AutoTransition());
                            binding.camBtn.setVisibility(View.VISIBLE);
                            binding.galleryBtn.setVisibility(View.VISIBLE);
                        }
                    }, 200);

                }
            }
        });
    }
    private void sendRecodingMessage(String audioPath) {
        if (chat == null)
            Toast.makeText(this, "Gửi tin nhắn văn bản trước", Toast.LENGTH_SHORT).show();
        else {
            String chatID = chat.getId();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(chatID + "/Media/Recording/" + preferenceManager.getString(Constants.KEY_USED_ID) + "/" + System.currentTimeMillis());
            Uri audioFile = Uri.fromFile(new File(audioPath));
            storageReference.putFile(audioFile).addOnSuccessListener(success -> {
                Task<Uri> audioUrl = success.getStorage().getDownloadUrl();

                audioUrl.addOnCompleteListener(path -> {
                    if (path.isSuccessful()) {

                        String url = path.getResult().toString();
                        if (chat == null) {
                        } else {
                            chatPresenter.send(chatID, url, Constants.KEY_TYPE_VOICE, messageList.size());
                        }
                    }
                });
            });
        }
    }
    private void setUpRecording() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ChatMe/Media/Recording");
//
//        if (!file.exists())
//            file.mkdirs();
//        audioPath = file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".3gp";

        audioPath = getExternalCacheDir().getAbsolutePath();
        audioPath += File.separator + System.currentTimeMillis() + ".3gp";
        mediaRecorder.setOutputFile(audioPath);

    }

    private void send(String content, String type) {
            if (chat!=null && chat.getId() != null && !chat.getId().isEmpty()){
                chatPresenter.send(chat.getId(), content, type, messageList.size());
            }else if (userModel != null){
                ArrayList<String> mems = new ArrayList<>();
                mems.add(userModel.getId());
                chatPresenter.createChatAndSend(mems,"", Constants.KEY_PRIVATE_CHAT, type, content, messageList.size());
                chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), Constants.KEY_PRIVATE_CHAT, this);
                binding.usersRecyclerView.setAdapter(chatAdapter);

            }else if(receivedList != null){
                chatPresenter.createChatAndSend(receivedList, group_name, Constants.KEY_GROUP_CHAT, type, content, messageList.size());
                chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), Constants.KEY_GROUP_CHAT, this);
                binding.usersRecyclerView.setAdapter(chatAdapter);
            }



//        Message message = new Message(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),
//                preferenceManager.getString(Constants.KEY_AVATAR),
//                content,type, Helpers.getNow(),"1");
//        messageList.add(message);
//        chatAdapter.notifyItemRangeInserted(messageList.size(),messageList.size());
//        binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);

    }

    private void sendImg(){
        String filepath = "ChatImages/" +  System.currentTimeMillis();

        StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
        if (imageUri != null){
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            encodedImage = uri.toString();
                            if (chat!=null){
                                chatPresenter.send(chat.getId(), encodedImage,  Constants.KEY_TYPE_IMAGE, messageList.size());
                            }else if (userModel != null){
                                ArrayList<String> mems = new ArrayList<>();
                                mems.add(userModel.getId());
                                chatPresenter.createChatAndSend(mems,"", Constants.KEY_PRIVATE_CHAT, Constants.KEY_TYPE_IMAGE, encodedImage, messageList.size());
                                chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), Constants.KEY_PRIVATE_CHAT, ChatActivity.this);
                                binding.usersRecyclerView.setAdapter(chatAdapter);

                            }else if(receivedList != null){
                                chatPresenter.createChatAndSend(receivedList, group_name, Constants.KEY_GROUP_CHAT, Constants.KEY_TYPE_IMAGE, encodedImage, messageList.size());
                                chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), Constants.KEY_GROUP_CHAT, ChatActivity.this);
                                binding.usersRecyclerView.setAdapter(chatAdapter);
                            }
                        }
                    });
                }
            });
        }

    }

    private final ActivityResultLauncher<Intent> startForImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    Uri resultUri = data.getData();

                    binding.imageContent.setImageURI(resultUri);
                    binding.containerText.setVisibility(View.GONE);
                    binding.containerImg.setVisibility(View.VISIBLE);

                    imageUri = result.getData().getData();
//                    encodedImage = imageUri.toString();

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                    Toast.makeText(getApplicationContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Hủy chọn ảnh!", Toast.LENGTH_SHORT).show();
                }
            });
    @Override
    public void onChatNotExist() {
//        chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), "PRIVATE_CHAT");
//        binding.usersRecyclerView.setAdapter(chatAdapter);

//        binding.usersRecyclerView.setVisibility(View.VISIBLE);
//        binding.shimmerEffect.stopShimmerAnimation();
//        binding.shimmerEffect.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateInforSuccess(String name, String avatar) {
        chat.setName(name);

//        chatPresenter.joinChat(preferenceManager.getString(Constants.KEY_USED_ID),preferenceManager.getString(Constants.KEY_NAME),preferenceManager.getString(Constants.KEY_AVATAR), chatNoLastMessObj.getId(), chatNoLastMessObj.getType(), preferenceManager.getString(Constants.KEY_PUBLIC_KEY));
//        chatPresenter.getMessages(chatNoLastMessObj.getId());
//        chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chatNoLastMessObj.getType(), secretKey, this)
        if (name.equals("Han")){
            Log.d(TAG, "onUpdateInforSuccess: " + name);
        }
        binding.textName.setText(name);
        if (chat.getAvatar() != null && !chat.getAvatar().isEmpty()){
            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(avatar));
            chat.setAvatar(avatar);
        }
        //        showToast(getApplicationContext(), "Thông tin nhóm vừa được thay đổi!");
    }

    @Override
    public void onUpdateOnlineStatus(Boolean status) {
        binding.textOnline.setText(Objects.equals(status, true) ? "Đang hoạt động": "Ngoại tuyến");
        binding.textOnline.setTextColor(Objects.equals(status, true) ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
    }

    @Override
    public void onUpdateFcmOrInchat(Map<String, String> fcm, Map<String, Object> inchat) {
        chat.setFcm(fcm);
        chat.setInchat(inchat);
    }

    @Override
    public void onFindChatError() {
//        binding.shimmerEffect.stopShimmerAnimation();
//        binding.shimmerEffect.setVisibility(View.GONE);
//        Toast.makeText(getApplicationContext(),"Lỗi khi tìm đoạn chat", Toast.LENGTH_SHORT).show();
//        finish();

    }

    @Override
    public void onFindChatSuccess(Chat chat) {
        this.chat = chat;

        binding.textOnline.setText(chat.getOnline().containsValue(true) ? "Đang hoạt động": "Ngoại tuyến");
        binding.textOnline.setTextColor(chat.getOnline().containsValue(true) ? getResources().getColor(R.color.green): getResources().getColor(R.color.seed) );
        chatPresenter.getMessages(chat.getId());
        chatAdapter = new ChatAdapter(messageList, preferenceManager.getString(Constants.KEY_USED_ID), chat.getType_chat(), this);
        List<String> memIds = new ArrayList<>(chat.getMembers());
        memIds.add(chat.getLeader());
        chatPresenter.listenChatInfo(chat.getId(), chat.getType_chat(), "", memIds);
    }
    @Override
    public void onGetMessagesSuccess() {

        messageList = chatPresenter.getMessageList();
        chatAdapter.resetData(messageList);
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.usersRecyclerView.setAdapter(chatAdapter);
        binding.usersRecyclerView.setVisibility(View.VISIBLE);
        binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // call the invalidate()
//                binding.usersRecyclerView.smoothScrollToPosition(0);
//            }
//        });

    }

    @Override
    public void onGetMessagesError() {
        Toast.makeText(getApplicationContext(),"Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onSendError(int pos) {
        Toast.makeText(getApplicationContext(),"Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show();
        messageList.get(pos).setIsSending("0");
        chatAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onSendSuccess(String content, String typeMess, int pos) {



//        chatPresenter.getMessages(chatNoLastMessObj.getId());
        messageList.get(pos).setIsSending("2");
        chatAdapter.notifyItemChanged(pos);
//        chatPresenter.sendRealtime(content,typeMess, preferenceManager.getString(Constants.KEY_USED_ID), chatNoLastMessObj.getId());


        sendNoti(content,typeMess, chat.getName(), chat.getType_msg());

    }


    @Override
    public void onCreateAndSendSuccess(String content, String typeMess, int pos) {

        chat = chatPresenter.getChat();
        chatPresenter.getMessages(chat.getId());
//        messageList.get(pos).setIsSending("2");
        chatAdapter.notifyItemChanged(pos);

//        List<String> userIdList = new ArrayList<>();
//        if (Objects.equals(chat.getType_chat(), Constants.KEY_GROUP_CHAT)){
//            userIdList = receivedList;
//        }else{
//            userIdList.add(chat.getMembers().get(0));
//        }
//        userIdList.add(preferenceManager.getString(Constants.KEY_USED_ID));

//        if (userModel != mo)
        sendNoti(content,typeMess,chat.getName(),chat.getType_msg());
    }

    private void sendNoti(String content, String typeMess, String group_name, String type_chat){
        try {
            ArrayList<String> tokens = new ArrayList<>();
            for (String idOfflineUser : chat.getInchat().keySet()){
                if (!idOfflineUser.equals(preferenceManager.getString(Constants.KEY_USED_ID))
                        && !(Boolean) chat.getInchat().get(idOfflineUser)){
                    tokens.add(chat.getFcm().get(idOfflineUser));
                }
            }
            JSONObject data = new JSONObject();
            data.put("ChatId", chat.getId());
            data.put(Constants.KEY_NAME, group_name);
            data.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
            if (Objects.equals(typeMess, Constants.KEY_TYPE_TEXT))
                data.put(Constants.KEY_MESSAGE, content);
            data.put(Constants.TYPE_MESSAGES_SEND, typeMess);
            data.put("TypeChat", type_chat);

            JSONObject notification = new JSONObject();
            notification.put("title", "New Message");
            notification.put("body", "");


            JSONObject mess = new JSONObject();
            mess.put("notification", notification);
            mess.put("data", data);
            for(String token : tokens){
                mess.put("token", token);

                JSONObject body = new JSONObject();
                body.put("message", mess);

                Log.d("Send Noti Request", body.toString());
                chatPresenter.sendNotification(body.toString(), AccessToken.getAccessToken());
            }
        }catch (Exception e){
//            showToast(getApplicationContext(), e.getMessage());
            Log.d(TAG, "Send Noti Fail: " + e.getMessage());

        }
    }


    @Override
    public void onShowImageClick(String content) {
        Intent it = new Intent(this, ShowImageActivity.class);
        it.putExtra(Constants.KEY_TYPE_IMAGE, content);
        startActivity(it);
    }

    @Override
    public void onInfoChatChanged(Chat chat) {
        binding.textName.setText(chat.getName());
    }

    @Override
    public void updateMessList(int count) {
        binding.shimmerEffect.stopShimmerAnimation();
        binding.shimmerEffect.setVisibility(View.GONE);
        binding.usersRecyclerView.setVisibility(View.VISIBLE);
        binding.usersRecyclerView.setAdapter(chatAdapter);

        messageList = chatPresenter.getMessageList();
        if (count == 0){
            chatAdapter.resetData(messageList);
            chatAdapter.notifyDataSetChanged();
        }else {
            chatAdapter.notifyItemRangeInserted(messageList.size(),messageList.size());
            binding.usersRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            //chatAdapter.notifyDataSetChanged();
        }

    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        String newName = intent != null ? intent.getStringExtra("name") : null;
                        String newAvatar = intent != null ? intent.getStringExtra("avatar") : null;
                        if (chat!= null){
                            if (newName != null) chat.setName(newName);
                            if (newAvatar != null) chat.setAvatar(newAvatar);
                            if (chat.getName().equals("Han")){
                                Log.d(TAG, "mStartForResult: " + chat.getName());
                            }
                            binding.textName.setText(chat.getName());
                            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat.getAvatar()));
                        }
                    }
                }
            });
    @Override
    protected void onResume() {
        super.onResume();
        if(chat != null)
            chatPresenter.updateOnlineStatus(chat.getId(),true);
        Chat chat1 = chatPresenter.getChat();
        if (chat1.getName() != null && !chat1.getName().isEmpty() ){
            if (chat1.getName().equals("Han")){
                Log.d(TAG, "onResume(): " + chat1.getName());
            }
            binding.textName.setText(chat1.getName());
        }
        if (chat1.getAvatar()!= null && !chat1.getAvatar().isEmpty()){
            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chat1.getAvatar()));
        }
//        else if (chatNoLastMessObj!= null){
//            binding.textName.setText(chatNoLastMessObj.getName());
//            binding.imageInfo.setImageBitmap(Helpers.getBitmapFromEncodedString(chatNoLastMessObj.getAvatar()));
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chat != null && chat.getId() != null)
            chatPresenter.updateOnlineStatus(chat.getId(), false);
//        if(chat!= null){
//            chatPresenter.leaveChat(preferenceManager.getString(Constants.KEY_NAME),chat.getId());
//        }else if ( chatNoLastMessObj != null)
//            chatPresenter.leaveChat(preferenceManager.getString(Constants.KEY_NAME),chatNoLastMessObj.getId());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
//            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getGalleryImage();
//                } else {
//                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
//                }
//
//                break;

            case Constants.RECORDING_REQUEST_CODE:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    if (this.permissions.isStorageOk(ChatActivity.this))
                        binding.recordButton.setListenForRecord(true);
                    else this.permissions.requestStorage(ChatActivity.this);

                } else
                    Toast.makeText(this, "Recording permission denied", Toast.LENGTH_SHORT).show();
                break;
            case Constants.STORAGE_REQUEST_CODE:

                if (grantResults[0] == PERMISSION_GRANTED)
                    binding.recordButton.setListenForRecord(true);
                else
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
                break;


        }
    }
}