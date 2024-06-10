package com.example.chatitt.ultilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.SecretKey;

public class Helpers {
    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void setupUI(View view, Activity activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, activity);
            }
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            if (activity.getCurrentFocus() != null){

                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
                activity.getCurrentFocus().clearFocus();
            }

        }
    }
    public static String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }else{
            return null;
        }
    }

    public static Bitmap getBitmapFromEncodedString(String encodedImage){
        if (encodedImage != null){
            byte[] bytes = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }else {
            return null;
        }

    }

    public static List<User> checkStringContain(String searchStr, List<User> userModelList){
        List<User> result = new ArrayList<>();
        for (User u : userModelList){
            if (u.getUsername().toLowerCase().contains(searchStr.toLowerCase())){
                result.add(u);
            }else if (u.getEmail().contains(searchStr)){
                result.add(u);
            }
        }
        return result;
    }

    public static List<Chat> checkStringChatContain(String searchStr, List<Chat> chatList){
        List<Chat> result = new ArrayList<>();
        for (Chat u : chatList){
            if (u.getName().toLowerCase().contains(searchStr.toLowerCase())){
                result.add(u);
            }
        }
        return result;
    }

//    public static List<Message> checkStringMessageContain(String searchStr, List<Message> messageList, SecretKey secretKey){
//        List<Message> result = new ArrayList<>();
//        for (Message u : messageList){
//            String content;
//            content = u.getContent();
//
//            if (content.contains(searchStr.toLowerCase())){
//                result.add(u);
//            }
//        }
//        return result;
//    }
    public static String encodeCoverImage(Bitmap bitmap){
        int previewWidth = 600;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }else{
            return null;
        }
    }

    public static String formatTime(String time, boolean inChat){
        // Định nghĩa định dạng của đầu vào (UTC)
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Định nghĩa định dạng của đầu ra (giờ địa phương)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
        dateFormat.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        hourFormat.setTimeZone(TimeZone.getDefault());

        Date now = new Date();
        String nowLocalDateTime = dateFormat.format(now);

        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd-MM");

        try {
            Date date = inputFormat.parse(time);
            String localDateTime = dateFormat.format(date);
            if (inChat){
                return outputFormat.format(date);
            }else {
                if (localDateTime.equals(nowLocalDateTime)){
                    return hourFormat.format(date);
                }else {
                    return localDateTime;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return time;
        }
//        }

    }
    public static Boolean isValidPhone(String phonenumber){
        return Patterns.PHONE.matcher(phonenumber).matches() && phonenumber.startsWith("0") && phonenumber.length()==10;
    }
}
