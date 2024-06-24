package com.example.chatitt.contacts.main.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.chatitt.R;
import com.example.chatitt.chats.individual_chat.create_new.view.CreatePrivateChatActivity;
import com.example.chatitt.contacts.main.presenter.ContactsContract;
import com.example.chatitt.contacts.main.presenter.ContactsPresenter;
import com.example.chatitt.contacts.manage_request_friend.view.ManageReqFrieActivity;
import com.example.chatitt.contacts.send_request.view.ProfileScanUserActivity;
import com.example.chatitt.contacts.send_request.view.QRScanFriendActivity;
import com.example.chatitt.databinding.FragmentContactsBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class ContactsFragment extends Fragment implements ContactsContract.ViewInterface {

    private FragmentContactsBinding binding;
    private PreferenceManager preferenceManager;

    private ContactsPresenter presenter;
    private Animation fromBottomBgAnim;
    private Animation toBottomBgAnim;
    private String STR ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater,container,false);
        View rootView = binding.getRoot();
        Helpers.setupUI(rootView, requireActivity());
        preferenceManager = new PreferenceManager(requireContext());
        presenter = new ContactsPresenter(this);
        if (fromBottomBgAnim == null){
            fromBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim);
        }
        if (toBottomBgAnim == null){
            toBottomBgAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim);
        }
        init();
        setListener();
        return rootView;
    }

    private void init(){
        binding.username.setText(preferenceManager.getString(Constants.KEY_NAME));

        // Chuyển đổi dữ liệu thành định dạng JSON
        STR = preferenceManager.getString(Constants.KEY_USED_ID);

        try {
            // Tính toán kích thước ảnh QR dựa trên kích thước màn hình
            int qrCodeSize = getQRCodeSize();
            //Tạo qr
            Bitmap bitmap = encodeAsBitmap(STR, qrCodeSize);
            // Hiển thị lên imv
            binding.qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Thất bại khi tạo mã QR", Toast.LENGTH_SHORT).show();
        }
        binding.swipeLayout.setRefreshing(false);
    }
    private void setListener(){
        binding.swipeLayout.setOnRefreshListener(this::init);
        binding.cardviewListFriend.setOnClickListener(v->{
            Intent it = new Intent(requireContext(), CreatePrivateChatActivity.class);
            it.putExtra("title", "Danh sách bạn bè");
            startActivity(it);
        });
        binding.btnScan.setOnClickListener(v->{
            ScanOptions options = new ScanOptions();
            options.setPrompt("Hướng camera về phía mã QR");
            options.setCameraId(0);  // Use a specific camera of the device
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(QRScanFriendActivity.class);
            barcodeLauncher.launch(options);

        });
        binding.cardviewRequest.setOnClickListener(v->{
            Intent it = new Intent(requireContext(), ManageReqFrieActivity.class);
            startActivity(it);
        });

        binding.makeFriend.setOnClickListener(v -> {
            String email = binding.editPhone.getText().toString().trim();
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                if (email.equals(preferenceManager.getString(Constants.KEY_EMAIL))){
                    Toast.makeText(requireContext(), "Bạn không thể kết bạn chính mình", Toast.LENGTH_SHORT).show();
                }else {
                    binding.transparentBg.startAnimation(fromBottomBgAnim);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    presenter.searchUser(email);
                }
            }else {
                Toast.makeText(requireContext(), "Email không đúng định dạng, hãy thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        binding.editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = !TextUtils.isEmpty(s);
                binding.makeFriend.setEnabled(hasText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private int getQRCodeSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int smallerDimension = Math.min(screenWidth, screenHeight);
        return (int) (smallerDimension * 0.8); // Sử dụng tỷ lệ 80% của kích thước màn hình
    }
    Bitmap encodeAsBitmap(String str, int qrCodeSize) throws WriterException {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.encodeBitmap(str, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(requireContext(), "Quét QR Code bị hủy", Toast.LENGTH_LONG).show();
                } else {
                    // if the intentResult is not null we'll set
                    // the content and format of scan message
                    try {
                        String userId = result.getContents();

                        // Xử lý dữ liệu
                        if (userId.equals(preferenceManager.getString(Constants.KEY_PHONE))){
                            Toast.makeText(requireContext(), "Bạn đang quét QR của chính bạn!", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(requireContext(), ProfileScanUserActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

    @Override
    public void onSearchUserError() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Địa chỉ email chưa đăng ký tài khoản, vui lòng thử số khác!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchUserSuccess() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(requireContext(), ProfileScanUserActivity.class);
        intent.putExtra("userId", presenter.getUserModels().getId());
        startActivity(intent);
    }
}