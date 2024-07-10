package com.example.chatitt.chats.group_chat.create_new.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.group_chat.create_new.presenter.CreateGroupChatContract;
import com.example.chatitt.databinding.ItemContainerRecentConversionBinding;
import com.example.chatitt.ultilities.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.ListUserViewHolder> {
    private List<User> userModelList;

    static List<User> chosenList;
    private CreateGroupChatContract.ViewInterface viewInterface;

    public void reset(List<User> userModelList){
        this.userModelList = userModelList;
        notifyDataSetChanged();
    }

    public GroupUserAdapter(List<User> userModelList, CreateGroupChatContract.ViewInterface viewInterface) {
        this.userModelList = userModelList;
        this.viewInterface = viewInterface;
        chosenList = new ArrayList<>();
    }


    public List<User> getChosenList() {
        return chosenList;
    }

    @NonNull
    @Override
    public ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListUserViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserViewHolder holder, int position) {
        holder.setData(userModelList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }


    class ListUserViewHolder extends RecyclerView.ViewHolder {

        ItemContainerRecentConversionBinding binding;

        public ListUserViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(User userModel, int pos) {
            if (userModel.getOnline()){
                binding.imageStatus.setColorFilter(Color.rgb(0,255,0));
            }else {
                binding.imageStatus.setColorFilter(Color.rgb(255,165,0));
            }

            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getName());
            binding.textRecentMessage.setText(userModel.getPhonenumber());
            binding.textTime.setVisibility(View.GONE);
            binding.checkbox.setVisibility(View.VISIBLE);

            binding.checkbox.setOnCheckedChangeListener(null);
            binding.checkbox.setChecked(userModel.getChecked());
            binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    userModel.setChecked(isChecked);
                    if (isChecked){
                        if(!chosenList.contains(userModel))
                            chosenList.add(userModel);
                    }else {
                        chosenList.removeIf(u -> Objects.equals(u.getEmail(), userModel.getEmail()));
                    }



                }
            });



            binding.getRoot().setOnClickListener(view -> {

//                userModel.setChecked(!userModel.getChecked());
                binding.checkbox.setChecked(!userModel.getChecked());
                viewInterface.onUserClicked();

            });
        }
    }
}
