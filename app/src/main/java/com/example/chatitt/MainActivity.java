package com.example.chatitt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MotionEvent;

import com.example.chatitt.chats.chat_list.view.ChatListFragment;
import com.example.chatitt.contacts.main.view.ContactsFragment;
import com.example.chatitt.databinding.ActivityMainBinding;
import com.example.chatitt.profile.view.ProfileFragment;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ChatListFragment chatsFragment = new ChatListFragment();
    private ContactsFragment contactsFragment = new ContactsFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Helpers.setupUI(binding.getRoot(), this);

        preferenceManager = new PreferenceManager(getApplicationContext());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view_tag, chatsFragment, "ChatsFragment").commit();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.fragment_container_view_tag, chatsFragment, "ChatsFragment");
//        ft.show(chatsFragment);
//        ft.commit();

        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();

        ft0.add(R.id.fragment_container_view_tag, contactsFragment, "ContactsFragment");
        ft0.add(R.id.fragment_container_view_tag, profileFragment, "ProfileFragment");

        ft0.hide(contactsFragment);
        ft0.hide(profileFragment);
        ft0.commit();


//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, chatsFragment, "ChatsFragment").commit();

        binding.bottomNav.setOnItemSelectedListener(item -> {
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            if (item.getItemId() == R.id.chats) {

                ft1.hide(contactsFragment);
                ft1.hide(profileFragment);
                ft1.show(chatsFragment);
                ft1.commit();
                return true;
            }else if (item.getItemId() == R.id.contact) {
                ft1.hide(chatsFragment);
                ft1.hide(profileFragment);
                ft1.show(contactsFragment);
                ft1.commit();
                return true;
            }else if (item.getItemId() == R.id.profile) {
                ft1.hide(chatsFragment);
                ft1.hide(contactsFragment);
                ft1.show(profileFragment);
                ft1.commit();
                return true;
            }
            return false;
        });

        JSONObject data = new JSONObject();
        try {
            data.put("userId", preferenceManager.getString(Constants.KEY_USED_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getToken();

    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        if (preferenceManager.getString(Constants.KEY_FCM_TOKEN) != null && preferenceManager.getString(Constants.KEY_FCM_TOKEN).equals(token)) return;
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("ChatsFragment");
        if (fragment instanceof ChatListFragment) {
            ChatListFragment chatsFragment1 = (ChatListFragment) fragment;
            // Sử dụng đối tượng YourFragment ở đây
            chatsFragment1.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);

    }

    @Override
    protected void onResume() {
        super.onResume();
        JSONObject data = new JSONObject();
        try {
            data.put("userId", preferenceManager.getString(Constants.KEY_USED_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}