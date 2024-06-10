package com.example.chatitt.profile.view;

import static com.example.chatitt.ultilities.Helpers.encodeCoverImage;
import static com.example.chatitt.ultilities.Helpers.encodeImage;
import static com.example.chatitt.ultilities.Helpers.showToast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatitt.R;
import com.example.chatitt.authentication.LoginActivity;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.databinding.FragmentProfileBinding;
import com.example.chatitt.profile.presenter.ProfileContract;
import com.example.chatitt.profile.presenter.ProfilePresenter;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileFragment extends Fragment implements ProfileContract.ViewInterface {
    private FragmentProfileBinding binding;
    private PreferenceManager preferenceManager;
    private ProfilePresenter profilePresenter;
    private DatePickerDialog datePickerDialog;
    private final ActivityResultLauncher<Intent> startForCoverImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    Uri resultUri = data.getData();
                    try {
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        String encodedImage = encodeCoverImage(bitmap);
                        profilePresenter.updateCoverImage(encodedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View rootView = binding.getRoot();
        Helpers.setupUI(binding.getRoot(),requireActivity());
        init();
        setListener();
        showUserInfo();
        return rootView;
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireContext());
        profilePresenter = new ProfilePresenter(this,preferenceManager);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                binding.editBirthday.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(requireContext(),style,dateSetListener,y,m,d );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        binding.editBirthday.setText(getTodayDate());

        String[] gender = {"Nam", "Nữ", "Bí mật"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_gender_item, gender);
        binding.autoCompleteText.setAdapter(adapter);
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        m = m + 1;
        return d + "/" + m + "/" + y;
    }

    private void setListener(){
        binding.include.logout.setOnClickListener(v -> {
            onLogOut();
        });
        //phone interaction
        binding.itemPhone.btnAdd.setOnClickListener(v -> onAddPhoneNumberPressed());
        binding.itemPhone.btnOK.setOnClickListener(v -> {
            if (isValidPhone(binding.itemPhone.editTextPhoneNumber.getText().toString().trim())){
                onOKPhoneNumberPressed();
            }
        });
        binding.itemPhone.btnHide.setOnClickListener(v -> onHidePhoneNumberPressed());
        binding.itemPhone.btnCancel.setOnClickListener(v -> onCancelPhoneEditPressed());
        binding.itemPhone.icEdit.setOnClickListener(v -> onEditPhonePressed());

        //email interaction
        binding.itemEmail.btnOK.setOnClickListener(v -> {
            if (isValidEmail(binding.itemEmail.editTextEmail.getText().toString().trim())){
                onOKEmailPressed();
            }
        });
        binding.itemEmail.btnHide.setOnClickListener(v -> onHideEmailPressed());
        binding.itemEmail.btnCancel.setOnClickListener(v -> onCancelEmailEditPressed());
        binding.itemEmail.icEdit.setOnClickListener(v -> {
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                onLoginAgainBeforeChangeEmail();
            }else {
                onEditEmailPressed();
            }

        });

        //avatar interaction
        binding.include.icEdit.setOnClickListener(v -> onEditNamePressed());
        binding.include.btnChangePass.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
        });
        binding.updateProfileImg.setOnClickListener(view ->
                        ImagePicker.with(this)
                                .crop()//Crop image(Optional), Check Customization for more option
//                    .compress(4096)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(2160, 2160)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .createIntent(intent -> {
                                    startForCoverImageResult.launch(intent);
                                    return null;
                                })
        );
        binding.include.updateProfileImg.setOnClickListener(view -> onUpdateProfileImgPressed());
        binding.include.imageProfile.setOnClickListener(view -> {

        });

        //address interaction
        binding.itemAddress.btnAdd.setOnClickListener(view -> onAddAdressPressed());
        binding.itemAddress.btnOK.setOnClickListener(view -> {
            if (isValidAdress()) {
                onOKAdressPressed();
            }
        });
        binding.itemAddress.icEdit.setOnClickListener(view -> onEditAdressPressed());
        binding.itemAddress.btnCancel.setOnClickListener(view -> onCancelAdressPressed());
        binding.itemAddress.icHide.setOnClickListener(view -> onHideAdressPressed());

        //birthday interaction
        binding.icEditBirthday.setOnClickListener(view -> onEditBirthdayPressed());
        binding.btnOKBirthday.setOnClickListener(view -> onOKBirthdayPressed());
        binding.editBirthday.setOnClickListener(view -> datePickerDialog.show());

        //gender interaction
        binding.btnOKGender.setOnClickListener(view -> profilePresenter.updateGender(binding.autoCompleteText.getText().toString()));
        binding.icEditGender.setOnClickListener(view -> onEditGenderPressed());
        binding.swipeLayout.setOnRefreshListener(() -> {
            // Xử lý logic khi làm mới dữ liệu, ví dụ tải dữ liệu mới
            profilePresenter.getProfile();
        });
    }
    private Boolean isValidPhone(String textPhone){
        if (textPhone.isEmpty()){
            showToast(requireContext(),"Hãy nhập số điện thoại!");
            return false;
        }else if(!Helpers.isValidPhone(textPhone)){
            showToast(requireContext(),"Số điện thoại không đúng định dạng! Hãy kiểm tra lại!!");
            return false;
        }
        return true;
    }
    private void onAddPhoneNumberPressed(){
//        TransitionManager.beginDelayedTransition(binding.itemInfo.cardview, new AutoTransition());
        binding.itemPhone.btnAdd.setVisibility(View.GONE);
        binding.itemPhone.btnHide.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemPhone.editTextPhoneNumber.setVisibility(View.VISIBLE);
        binding.itemPhone.containerBtn.setVisibility(View.VISIBLE);
//        binding.itemInfo.container.startAnimation(slideUp);
    }
    private void onOKPhoneNumberPressed(){
        String textPhone = binding.itemPhone.editTextPhoneNumber.getText().toString().trim();
        profilePresenter.updatePhoneNumber(textPhone);
    }
    private void onHidePhoneNumberPressed(){
        binding.itemPhone.btnHide.setVisibility(View.GONE);
        binding.itemPhone.btnAdd.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemPhone.editTextPhoneNumber.setVisibility(View.GONE);
        binding.itemPhone.containerBtn.setVisibility(View.GONE);
    }
    private void onCancelPhoneEditPressed(){
        binding.itemPhone.editTextPhoneNumber.setVisibility(View.GONE);
        binding.itemPhone.btnCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemPhone.textPhone.setVisibility(View.VISIBLE);
        binding.itemPhone.icEdit.setVisibility(View.VISIBLE);
        binding.itemPhone.containerBtn.setVisibility(View.GONE);
    }
    private void onEditPhonePressed(){
        binding.itemPhone.btnCancel.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemPhone.textPhone.setVisibility(View.GONE);
        binding.itemPhone.icEdit.setVisibility(View.GONE);
        binding.itemPhone.editTextPhoneNumber.setVisibility(View.VISIBLE);
        binding.itemPhone.containerBtn.setVisibility(View.VISIBLE);
    }
    private Boolean isValidEmail(String textPhone){
        if (textPhone.isEmpty()){
            showToast(requireContext(),"Hãy nhập Email!");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(textPhone).matches()){
            showToast(requireContext(),"Email không đúng định dạng ");
            return false;
        }
        return true;
    }
    private void onOKEmailPressed(){
        String textEmail = binding.itemEmail.editTextEmail.getText().toString().trim();
        profilePresenter.updateEmail(textEmail);
    }
    private void onHideEmailPressed(){
        binding.itemEmail.btnHide.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemEmail.editTextEmail.setVisibility(View.GONE);
        binding.itemEmail.containerBtn.setVisibility(View.GONE);
    }
    private void onCancelEmailEditPressed(){
        binding.itemEmail.editTextEmail.setVisibility(View.GONE);
        binding.itemEmail.btnCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemEmail.textEmail.setVisibility(View.VISIBLE);
        binding.itemEmail.icEdit.setVisibility(View.VISIBLE);
        binding.itemEmail.containerBtn.setVisibility(View.GONE);
    }
    private void onEditEmailPressed(){
        binding.itemEmail.btnCancel.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemEmail.textEmail.setVisibility(View.GONE);
        binding.itemEmail.icEdit.setVisibility(View.GONE);
        binding.itemEmail.editTextEmail.setVisibility(View.VISIBLE);
        binding.itemEmail.containerBtn.setVisibility(View.VISIBLE);
    }
    private void onEditBirthdayPressed() {
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        if (binding.containerBirthdayBtn.getVisibility() == View.VISIBLE){
            binding.containerBirthdayBtn.setVisibility(View.GONE);
            binding.editBirthday.setVisibility(View.GONE);
            if (preferenceManager.getString(Constants.KEY_BIRTHDAY) != null){
                binding.textBirthday.setVisibility(View.VISIBLE);
            }
        }else {
            binding.containerBirthdayBtn.setVisibility(View.VISIBLE);
            binding.editBirthday.setVisibility(View.VISIBLE);
            binding.textBirthday.setVisibility(View.GONE);
        }
    }
    private void onOKBirthdayPressed() {
        profilePresenter.updateBirthday(binding.editBirthday.getText().toString().trim());
    }
    private void onEditGenderPressed(){
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        if (binding.containerGenderBtn.getVisibility() == View.VISIBLE){
            binding.containerGenderBtn.setVisibility(View.GONE);
            binding.spinnerGender.setVisibility(View.GONE);
            if (preferenceManager.getString(Constants.KEY_BIRTHDAY) != null){
                binding.textGender.setVisibility(View.VISIBLE);
            }
        }else {
            binding.containerGenderBtn.setVisibility(View.VISIBLE);
            binding.spinnerGender.setVisibility(View.VISIBLE);
            binding.textGender.setVisibility(View.GONE);
        }
    }
    private void onEditNamePressed() {
        // Sử dụng LayoutInflater để tạo ra view từ tệp tin layout tùy chỉnh
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        // Lấy tham chiếu đến các phần tử trong layout tùy chỉnh
        TextView textViewTitle = dialogView.findViewById(R.id.textViewTitle);
        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        Button buttonOK = dialogView.findViewById(R.id.btn_OK);
        Button buttonEnd = dialogView.findViewById(R.id.btn_Cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm ra bên ngoài
        alertDialog.setCanceledOnTouchOutside(false);

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm nút Back
        alertDialog.setCancelable(false);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = editTextName.getText().toString().trim();
                        if (!newName.isEmpty() && !Patterns.DOMAIN_NAME.matcher(editTextName.getText().toString()).matches()) {
                            // Thay đổi tên người dùng thành newName ở đây
                            profilePresenter.updateUsername(newName);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(requireContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void onLoginAgainBeforeChangeEmail() {
        // Sử dụng LayoutInflater để tạo ra view từ tệp tin layout tùy chỉnh
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_dialog_layout, null);

        // Lấy tham chiếu đến các phần tử trong layout tùy chỉnh
        TextView textViewTitle = dialogView.findViewById(R.id.textViewTitle);
        EditText editTextEmail= dialogView.findViewById(R.id.edit_text_email);
        EditText editTextPass = dialogView.findViewById(R.id.edit_text_pass);
        EditText status = dialogView.findViewById(R.id.status);
        Button buttonOK = dialogView.findViewById(R.id.btn_OK);
        Button buttonEnd = dialogView.findViewById(R.id.btn_Cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm ra bên ngoài
        alertDialog.setCanceledOnTouchOutside(false);

        // Ngăn người dùng đóng Alert Dialog bằng cách bấm nút Back
        alertDialog.setCancelable(false);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = editTextEmail.getText().toString().trim();
                        String pass = editTextPass.getText().toString().trim();
                        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches() && !pass.isEmpty()) {
                            // Thay đổi tên người dùng thành newName ở đây
                            if(profilePresenter.isLoginAgainSuccessfully(email, pass)){
                                onEditEmailPressed();
                                alertDialog.dismiss();
                            }
                            status.setText("Email hoặc mật khẩu chưa đúng!! Hãy thử lại!");
                            status.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(requireContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
    private Boolean isValidAdress(){
        if (binding.itemAddress.editTextCity.getText().toString().trim().isEmpty()){
            showToast(requireContext(),"Hãy nhập tên thành phố");
            return false;
        }else if(binding.itemAddress.editTextCountry.getText().toString().trim().isEmpty()){
            showToast(requireContext(),"Hãy nhập tên quốc gia");
            return false;
        }else if(binding.itemAddress.editTextDetail.getText().toString().trim().isEmpty()){
            showToast(requireContext(),"Hãy nhập địa chỉ cụ thể");
            return false;
        }
        return true;
    }
    private void onAddAdressPressed() {
        binding.itemAddress.btnAdd.setVisibility(View.GONE);
        binding.itemAddress.btnCancel.setVisibility(View.GONE);
        binding.itemAddress.icHide.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemAddress.containerEditAddres.setVisibility(View.VISIBLE);
        binding.itemAddress.containerBtn.setVisibility(View.VISIBLE);
    }
    private void onEditAdressPressed() {
        if(binding.itemAddress.btnCancel.getVisibility() == View.GONE){
            binding.itemAddress.btnCancel.setVisibility(View.VISIBLE);
        }
        binding.itemAddress.icEdit.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemAddress.containerAddress.setVisibility(View.GONE);
        binding.itemAddress.editTextCountry.setText(preferenceManager.getString(Constants.KEY_ADDRESS_COUNTRY));
        binding.itemAddress.editTextCity.setText(preferenceManager.getString(Constants.KEY_ADDRESS_CITY));
        binding.itemAddress.editTextDetail.setText(preferenceManager.getString(Constants.KEY_ADDRESS_DETAIL));
        binding.itemAddress.containerEditAddres.setVisibility(View.VISIBLE);
        binding.itemAddress.containerBtn.setVisibility(View.VISIBLE);
    }
    private void onOKAdressPressed(){
        HashMap<String, Object> user = new HashMap<>();
        String city = binding.itemAddress.editTextCity.getText().toString();
        String country = binding.itemAddress.editTextCountry.getText().toString();
        String address = binding.itemAddress.editTextDetail.getText().toString();

        profilePresenter.updateAddress(address,city,country);
    }
    private void onCancelAdressPressed() {
        binding.itemAddress.icEdit.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemAddress.containerEditAddres.setVisibility(View.GONE);
        binding.itemAddress.containerBtn.setVisibility(View.GONE);
        binding.itemAddress.containerAddress.setVisibility(View.VISIBLE);
    }
    private void onHideAdressPressed() {
        binding.itemAddress.icHide.setVisibility(View.GONE);
        binding.itemAddress.btnAdd.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
        binding.itemAddress.containerEditAddres.setVisibility(View.GONE);
        binding.itemAddress.containerBtn.setVisibility(View.GONE);
    }

    private void onUpdateProfileImgPressed() {
        ImagePicker.with(this)
                .cropSquare()//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent(intent -> {
                    startForProfileImageResult.launch(intent);
                    return null;
                });
    }
    private final ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    Uri resultUri = data.getData();
                    try {
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        String encodedImage = encodeImage(bitmap);
                        profilePresenter.updateAvatar(encodedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else if (result.getResultCode() == ImagePicker.RESULT_ERROR){
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
    private void showUserInfo(){
        if(preferenceManager.getString(Constants.KEY_AVATAR) != null){
            byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_AVATAR), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.include.imageProfile.setImageBitmap(bitmap);
        }

        if(preferenceManager.getString(Constants.KEY_EMAIL) != null){
            binding.itemEmail.textEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
            binding.itemEmail.btnHide.setVisibility(View.GONE);
        }
        if(preferenceManager.getString(Constants.KEY_NAME) != null){
            binding.include.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        }
        if(preferenceManager.getString(Constants.KEY_PHONE) != null){

            binding.itemPhone.btnAdd.setVisibility(View.GONE);
            binding.itemPhone.btnHide.setVisibility(View.GONE);

            binding.itemPhone.textPhone.setText(preferenceManager.getString(Constants.KEY_PHONE));
            binding.itemPhone.textPhone.setVisibility(View.VISIBLE);
        }else {
            binding.itemPhone.icEdit.setVisibility(View.GONE);
            binding.itemPhone.btnHide.setVisibility(View.GONE);
            binding.itemPhone.btnAdd.setVisibility(View.VISIBLE);
            binding.itemPhone.textPhone.setVisibility(View.GONE);
        }
        if(preferenceManager.getString(Constants.KEY_ADDRESS_CITY) != null ){
            binding.itemAddress.btnAdd.setVisibility(View.GONE);
            binding.itemAddress.icHide.setVisibility(View.GONE);

            String city =  preferenceManager.getString(Constants.KEY_ADDRESS_CITY) + " (" + preferenceManager.getString(Constants.KEY_ADDRESS_COUNTRY) + ")";
            binding.itemAddress.textCity.setText(city);

            binding.itemAddress.textAddressDetail.setVisibility(View.VISIBLE);
            binding.itemAddress.textAddressDetail.setText(preferenceManager.getString(Constants.KEY_ADDRESS_DETAIL));

        }else {
            binding.itemAddress.icEdit.setVisibility(View.GONE);
            binding.itemAddress.icHide.setVisibility(View.GONE);
            binding.itemAddress.containerAddress.setVisibility(View.GONE);
        }
        if (preferenceManager.getString(Constants.KEY_BIRTHDAY) != null){
            binding.textBirthday.setText(preferenceManager.getString(Constants.KEY_BIRTHDAY));
            binding.textBirthday.setVisibility(View.VISIBLE);
        }else {
            binding.textBirthday.setVisibility(View.GONE);
        }
        if (preferenceManager.getString(Constants.KEY_GENDER) != null){
            binding.textGender.setText(preferenceManager.getString(Constants.KEY_GENDER));
            binding.textGender.setVisibility(View.VISIBLE);
        }else {
            binding.textGender.setVisibility(View.GONE);
        }
        if (preferenceManager.getString(Constants.KEY_COVERIMAGE) != null){
            binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_COVERIMAGE)));
        }
    }
    private void onLogOut(){
        profilePresenter.logOut();
        showToast(requireContext(),"Đang đăng xuất...");
        preferenceManager.clear();
        startActivity(new Intent(requireContext(), LoginActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onEditProfileSuccess(String type) {
        showToast(requireContext(),"Cập nhật thành công");
        switch (type){
            case Constants.KEY_AVATAR:
                String avatar = profilePresenter.getUserModel().getAvatar();
                preferenceManager.putString(Constants.KEY_AVATAR,avatar);
                binding.include.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(avatar));
                break;
            case Constants.KEY_NAME:
                String newName = profilePresenter.getUserModel().getUsername();
                preferenceManager.putString(Constants.KEY_NAME, newName);
                binding.include.textName.setText(newName);
                break;
            case Constants.KEY_COVERIMAGE:
                String cover_img = profilePresenter.getUserModel().getCoverImage();
                preferenceManager.putString(Constants.KEY_COVERIMAGE,cover_img);
                binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(cover_img));
                break;
            case Constants.KEY_EMAIL:
                String newEmail = profilePresenter.getUserModel().getEmail();
                preferenceManager.putString(Constants.KEY_EMAIL, newEmail);
                binding.itemEmail.btnHide.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.itemEmail.editTextEmail.setVisibility(View.GONE);
                binding.itemEmail.containerBtn.setVisibility(View.GONE);
                binding.itemEmail.textEmail.setText(newEmail);
                binding.itemEmail.textEmail.setVisibility(View.VISIBLE);
                binding.itemEmail.icEdit.setVisibility(View.VISIBLE);
                break;
            case Constants.KEY_PHONE:
                String newPhone = profilePresenter.getUserModel().getPhonenumber();
                preferenceManager.putString(Constants.KEY_PHONE, newPhone);
                binding.itemPhone.btnHide.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.itemPhone.editTextPhoneNumber.setVisibility(View.GONE);
                binding.itemPhone.containerBtn.setVisibility(View.GONE);
                binding.itemPhone.btnAdd.setVisibility(View.GONE);
                binding.itemPhone.textPhone.setText(newPhone);
                binding.itemPhone.textPhone.setVisibility(View.VISIBLE);
                binding.itemPhone.icEdit.setVisibility(View.VISIBLE);
                break;
            case Constants.KEY_ADDRESS_DETAIL:
                String city = profilePresenter.getUserModel().getCity();
                String country = profilePresenter.getUserModel().getCountry();
                String address = profilePresenter.getUserModel().getAddress();

                preferenceManager.putString(Constants.KEY_ADDRESS_CITY, city);
                preferenceManager.putString(Constants.KEY_ADDRESS_COUNTRY, country);
                preferenceManager.putString(Constants.KEY_ADDRESS_DETAIL, address);
                binding.itemAddress.icHide.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.itemAddress.containerEditAddres.setVisibility(View.GONE);
                binding.itemAddress.containerBtn.setVisibility(View.GONE);
                binding.itemAddress.btnAdd.setVisibility(View.GONE);
                String cityCountry = city + " (" + country + ")";
                binding.itemAddress.textCity.setText(cityCountry);
                binding.itemAddress.textAddressDetail.setText(address);
                binding.itemAddress.containerAddress.setVisibility(View.VISIBLE);
                binding.itemAddress.icEdit.setVisibility(View.VISIBLE);
                binding.itemAddress.editTextCity.getText().clear();
                binding.itemAddress.editTextCountry.getText().clear();
                binding.itemAddress.editTextDetail.getText().clear();
                break;
            case Constants.KEY_GENDER:
                String gender = profilePresenter.getUserModel().getGender();
                preferenceManager.putString(Constants.KEY_GENDER,gender);
                binding.textGender.setText(gender);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.containerGenderBtn.setVisibility(View.GONE);
                binding.spinnerGender.setVisibility(View.GONE);
                binding.textGender.setVisibility(View.VISIBLE);
                break;
            case Constants.KEY_BIRTHDAY:
                String birthday = profilePresenter.getUserModel().getBirthday();
                preferenceManager.putString(Constants.KEY_BIRTHDAY,birthday);
                binding.textBirthday.setText(birthday);
                TransitionManager.beginDelayedTransition(binding.layoutscroll, new AutoTransition());
                binding.containerBirthdayBtn.setVisibility(View.GONE);
                binding.editBirthday.setVisibility(View.GONE);
                binding.textBirthday.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onEditProfileError() {
        showToast(requireContext(),"Cập nhật hồ sơ thất bại, hãy thử lại!");
    }

    @Override
    public void onGetProfileSuccess() {
        User userModel = profilePresenter.getUserModel();

        binding.include.textName.setText(userModel.getUsername());
        preferenceManager.putString(Constants.KEY_NAME,userModel.getUsername());

        binding.include.imageProfile.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getAvatar()));
        if (userModel.getCoverImage() != null){
            preferenceManager.putString(Constants.KEY_COVERIMAGE, userModel.getCoverImage());
            binding.coverImg.setImageBitmap(Helpers.getBitmapFromEncodedString(userModel.getCoverImage()));
        }
        if (userModel.getBirthday() != null){
            preferenceManager.putString(Constants.KEY_BIRTHDAY, userModel.getBirthday());
            binding.textBirthday.setText(userModel.getBirthday());
            binding.textBirthday.setVisibility(View.VISIBLE);
        }else {
            binding.textBirthday.setVisibility(View.GONE);
        }
        if (userModel.getGender() != null){
            preferenceManager.putString(Constants.KEY_GENDER, userModel.getGender());
            binding.textGender.setText(userModel.getGender());
            binding.textGender.setVisibility(View.VISIBLE);
        }else {
            binding.textGender.setVisibility(View.GONE);
        }
        if (userModel.getCity() != null){
            preferenceManager.putString(Constants.KEY_ADDRESS_CITY, userModel.getCity());
            preferenceManager.putString(Constants.KEY_ADDRESS_COUNTRY, userModel.getCountry());
            preferenceManager.putString(Constants.KEY_ADDRESS_DETAIL, userModel.getAddress());
            String city = userModel.getCity()+" (" + userModel.getCountry()+")";
            binding.itemAddress.textCity.setText(city);
            binding.itemAddress.textAddressDetail.setText(userModel.getAddress());
        }
        //email info
        preferenceManager.putString(Constants.KEY_EMAIL, userModel.getEmail());
        binding.itemEmail.textEmail.setText(userModel.getEmail());
        binding.itemEmail.textEmail.setVisibility(View.VISIBLE);
        binding.itemEmail.btnHide.setVisibility(View.GONE);
        binding.itemEmail.icEdit.setVisibility(View.VISIBLE);

        if (userModel.getPhonenumber() != null){
            preferenceManager.putString(Constants.KEY_PHONE, userModel.getPhonenumber());

            binding.itemPhone.textPhone.setText(userModel.getPhonenumber());
            binding.itemPhone.textPhone.setVisibility(View.VISIBLE);
            binding.itemPhone.btnAdd.setVisibility(View.GONE);
            binding.itemPhone.btnHide.setVisibility(View.GONE);
            binding.itemPhone.icEdit.setVisibility(View.VISIBLE);
        }
        binding.swipeLayout.setRefreshing(false);
    }

    @Override
    public void onGetProfileError() {
        binding.swipeLayout.setRefreshing(false);
        showToast(requireContext(),"Lấy dữ liệu cá nhân thất bại");
    }
}