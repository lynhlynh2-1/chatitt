package com.example.chatitt.chats.individual_chat.create_new.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.individual_chat.create_new.presenter.CreateChatPrivateContract;
import com.example.chatitt.databinding.ItemContainerRecentConversionBinding;
import com.example.chatitt.ultilities.Helpers;

import java.util.List;
import java.util.Objects;

public class PrivateUserAdapter extends RecyclerView.Adapter<PrivateUserAdapter.ListUserViewHolder> {
    private List<User> userModelList;
    private final CreateChatPrivateContract.ViewInterface viewInterface;
    private boolean isFriendTab;


    public PrivateUserAdapter(List<User> userModelList, CreateChatPrivateContract.ViewInterface viewInterface, boolean isFriendTab) {
        this.userModelList = userModelList;
        this.viewInterface = viewInterface;
        this.isFriendTab = isFriendTab;
    }

    public void reset(List<User> userModels) {
        this.userModelList = userModels;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrivateUserAdapter.ListUserViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserViewHolder holder, int position) {
        holder.setData(userModelList.get(position));
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

        void setData(User userModel) {
            if (userModel.getOnline()){
                binding.imageStatus.setColorFilter(Color.rgb(0,255,0));
            }else {
                binding.imageStatus.setColorFilter(Color.rgb(255,165,0));
            }

            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getName());
            binding.textRecentMessage.setText(userModel.getEmail());
            binding.textTime.setVisibility(View.GONE);

            binding.getRoot().setOnClickListener(view -> {
                viewInterface.onUserClicked(userModel);
            });
            if (isFriendTab) {
                binding.btnDelete.setVisibility(View.VISIBLE);
                binding.btnDelete.setOnClickListener(v->{
                    // Tạo AlertDialog khi cần
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
                    alertDialogBuilder.setTitle("Xác nhận");
                    alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa bạn bè không!");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấn OK
                            viewInterface.deleteFriend(userModel.getId());
                            dialog.dismiss(); // Đóng dialog
                        }
                    });

                    // Nút "Hủy"
                    alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấn Hủy
                            dialog.dismiss(); // Đóng dialog
                        }
                    });
                    // Tạo AlertDialog từ AlertDialogBuilder
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // Hiển thị AlertDialog
                    alertDialog.show();
                });
            }
        }
    }
}