package com.example.chatitt.chats.group_chat.info.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatitt.authentication.model.User;
import com.example.chatitt.chats.group_chat.info.presenter.Contract;
import com.example.chatitt.databinding.ItemMemberBinding;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{

    private List<User> userModelList;
    private boolean isAdmin ;
    private Contract.MemListViewInterface viewInterface;
    private String chatId;
    private String leaderId;

    public MemberAdapter(List<User> userModelList, boolean isAdmin, Contract.MemListViewInterface viewInterface, String chatId, String leaderId) {
        this.userModelList = userModelList;
        this.isAdmin = isAdmin;
        this.viewInterface = viewInterface;
        this.chatId = chatId;
        this.leaderId = leaderId;
    }
    public void reset(List<User> userModelList){
        this.userModelList = userModelList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMemberBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(userModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemMemberBinding binding;

        public ViewHolder(ItemMemberBinding itemMemberBinding) {
            super(itemMemberBinding.getRoot());
            binding = itemMemberBinding;
        }

        void setData(User userModel){
            binding.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
            binding.textName.setText(userModel.getName());
            if (userModel.getId().equals(leaderId))
                binding.title.setVisibility(View.VISIBLE);
            if (!isAdmin) {
                binding.btnDelete.setVisibility(View.GONE);
                return;
            }

            binding.btnDelete.setOnClickListener(v -> {
                    // Tạo AlertDialog khi cần
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
                    alertDialogBuilder.setTitle("Xác nhận");
                    alertDialogBuilder.setMessage("Bạn có chắc chắn muốn người này khỏi nhóm không!");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấn OK
                            viewInterface.deleteMember(chatId, userModel.getId());
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
