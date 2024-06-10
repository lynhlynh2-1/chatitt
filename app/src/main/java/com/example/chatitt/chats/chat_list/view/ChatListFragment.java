package com.example.chatitt.chats.chat_list.view;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.chat_list.model.Chat;
import com.example.chatitt.chats.chat_list.presenter.ChatListContract;
import com.example.chatitt.chats.chat_list.presenter.ChatListPresenter;
import com.example.chatitt.chats.individual_chat.create_new.view.CreatePrivateChatActivity;
import com.example.chatitt.databinding.FragmentChatListBinding;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment implements ChatListContract.ViewInterface {
    private boolean isExpanded = false;
    private FragmentChatListBinding binding;
    private PreferenceManager preferenceManager;
    private ChatListPresenter chatsPresenter;
    private Animation fromBottomFabAnim;
    private Animation toBottomFabAnim;
    private Animation rotateClockWiseFabAnim;
    private Animation rotateAntiClockWiseFabAnim;
    private Animation fromBottomBgAnim;
    private Animation toBottomBgAnim;
    private List<Chat> conversations;
    private RecentConversationsAdapter conversationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatListBinding.inflate(inflater,container,false);
        View rootView = binding.getRoot();
        Helpers.setupUI(rootView, requireActivity());
        init();
        chatsPresenter.getMessaged();
        setListener();

        return rootView;
    }

    private void init(){
        binding.shimmerEffect.startShimmerAnimation();
        preferenceManager = new PreferenceManager(requireContext());
        chatsPresenter = new ChatListPresenter(this, preferenceManager);

        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations,this, preferenceManager);
        binding.conversationRecycleView.setAdapter(conversationsAdapter);

        if (fromBottomFabAnim == null){
            fromBottomFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_fab);
        }
        if (toBottomFabAnim == null){
            toBottomFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_fab);
        }
        if (fromBottomBgAnim == null){
            fromBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim);
        }
        if (toBottomBgAnim == null){
            toBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim);
        }
        if (rotateAntiClockWiseFabAnim == null){
            rotateAntiClockWiseFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clock_wise);
        }
        if (rotateClockWiseFabAnim == null){
            rotateClockWiseFabAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clock_wise);
        }

    }

    private void setListener(){
        binding.mainFabBtn.setOnClickListener(v->{
            if (isExpanded) {
                shrinkFab();
            } else {
                expandFab();
            }
        });
        binding.galleryFabBtn.setOnClickListener(v->{
//            Intent it = new Intent(requireContext(), CreateGroupChatActivity.class);
//            startActivity(it);
        });

        binding.shareFabBtn.setOnClickListener(v->{
            Intent it = new Intent(requireContext(), CreatePrivateChatActivity.class);
            it.putExtra("title", "Tin nhắn mới");
            startActivity(it);
        });

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatsPresenter.getMessaged();
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    List<Chat> chatList = Helpers.checkStringChatContain(query, conversations);
                    conversationsAdapter.reset(chatList);
                }else{
                    conversationsAdapter.reset(conversations);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(!TextUtils.isEmpty(newText.trim())){
                    List<Chat> chatList = Helpers.checkStringChatContain(newText, conversations);
                    conversationsAdapter.reset(chatList);
                }else{
                    conversationsAdapter.reset(conversations);
                }
                return false;
            }
        });

    }

    private void expandFab() {
        binding.transparentBg.startAnimation(fromBottomBgAnim);

        binding.mainFabBtn.startAnimation(rotateClockWiseFabAnim);
        binding.galleryFabBtn.startAnimation(fromBottomFabAnim);
        binding.shareFabBtn.startAnimation(fromBottomFabAnim);
        binding.galleryTv.startAnimation(fromBottomFabAnim);
        binding.shareTv.startAnimation(fromBottomFabAnim);

        isExpanded = !isExpanded;
    }

    private void shrinkFab() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.mainFabBtn.startAnimation(rotateAntiClockWiseFabAnim);
        binding.galleryFabBtn.startAnimation(toBottomFabAnim);
        binding.shareFabBtn.startAnimation(toBottomFabAnim);
        binding.galleryTv.startAnimation(toBottomFabAnim);
        binding.shareTv.startAnimation(toBottomFabAnim);
        isExpanded = !isExpanded;
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            if (isExpanded) {
                Rect outRect = new Rect();
                binding.fabConstraint.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    shrinkFab();
                }
            }
        }
    }

    @Override
    public void onConversionClicked(Chat chat) {

    }

    @Override
    public void onAddNewChatClick() {

    }

    @Override
    public void onRecentUserChatClick(User userModel) {

    }

    @Override
    public void onGetMessagedSuccess() {

    }

    @Override
    public void onGetMessagedError() {

    }

    @Override
    public void onNewChatCreate() {

    }

    @Override
    public void receiveNewMsgRealtime() {

    }

    @Override
    public void getMessaged(String userId) {

    }

    @Override
    public void isAddedChat() {

    }

    @Override
    public void updateChatRealtime() {

    }

    @Override
    public void onNewMemberAdded() {

    }
}