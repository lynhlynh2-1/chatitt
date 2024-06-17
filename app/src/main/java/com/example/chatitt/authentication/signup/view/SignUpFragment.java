package com.example.chatitt.authentication.signup.view;

import static com.example.chatitt.ultilities.Helpers.encodeImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatitt.MainActivity;
import com.example.chatitt.R;
import com.example.chatitt.authentication.model.User;
import com.example.chatitt.authentication.signup.presenter.SignUpContract;
import com.example.chatitt.authentication.signup.presenter.SignUpPresenter;
import com.example.chatitt.databinding.FragmentSignupBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.example.chatitt.ultilities.PreferenceManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpFragment extends Fragment implements SignUpContract.ViewInterface {

    private FragmentSignupBinding binding;
    private String encodedImage;
    private Boolean isHidePassEdt = true;
    private Boolean isHideRePassEdt = true;
    private PreferenceManager preferenceManager;
    private SignUpPresenter signUpPresenter;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout using DataBindingUtil
        binding = FragmentSignupBinding.inflate(inflater, container, false);

        // Get the root view from the binding
        View rootView = binding.getRoot();
        preferenceManager = new PreferenceManager(requireContext());
        signUpPresenter = new SignUpPresenter(requireContext(),this, preferenceManager);
        Helpers.setupUI(binding.signupFragment, requireActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        checkEnableSignup();
        setListener();
        onEditTextStatusChange();
        return rootView;
    }

    private void checkEnableSignup(){
        binding.edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.reEdtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty() && encodedImage != null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setListener(){

        binding.signup.setOnClickListener(view -> {
            loading(true);
            User user = new User(binding.edtPhone.getText().toString().trim(),
                    binding.edtPass.getText().toString().trim(),
                    binding.reEdtPass.getText().toString().trim(),
                    encodedImage,
                    binding.edtUsername.getText().toString().trim());
            if (isValidSignUpDetails(user)){
//                signUpPresenter.
                loading(true);
                db.collection(Constants.KEY_COLLECTION_USERS).whereEqualTo(Constants.KEY_EMAIL, user.getEmail())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if(querySnapshot != null && !querySnapshot.isEmpty()){
                                    // Email đã tồn tại
                                    loading(false);
                                    binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
                                    int colorEror = ContextCompat.getColor(requireContext(), R.color.error);
                                    binding.edtPhone.setHintTextColor(colorEror);
                                    binding.status.setText("Email đã tồn tại. Hãy thử lại với email khác!");
                                    binding.status.setVisibility(View.VISIBLE);
                                }else {
                                    // Email không tồn tại
                                    signup(user.getAvatar(), user.getName(), user.getEmail(), user.getPassword());
                                }
                            } else {
                                // Xử lý lỗi truy vấn
                                loading(false);
                                binding.status.setText("Lỗi!!");
                                binding.status.setVisibility(View.VISIBLE);
                            }
                        });

            }else

                loading(false);
        });
        binding.layoutImage.setOnClickListener(v -> {
            onUpdateProfileImgPressed();
        });
        binding.imgShowPass.setOnClickListener(view ->{
            if(isHidePassEdt){
                binding.edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imgShowPass.setImageResource(R.drawable.red);
                isHidePassEdt = false;
            }else {
                binding.edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imgShowPass.setImageResource(R.drawable.ic_remove_red_eye);
                isHidePassEdt = true;
            }
        });
        binding.reImgShowPass.setOnClickListener(view ->{
            if(isHideRePassEdt){
                binding.reEdtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.reImgShowPass.setImageResource(R.drawable.red);
                isHideRePassEdt = false;
            }else {
                binding.reEdtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.reImgShowPass.setImageResource(R.drawable.ic_remove_red_eye);
                isHideRePassEdt = true;
            }
        });
    }

    void signup(String avatar, String username,String email, String password){
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, username);
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_AVATAR, avatar);
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        System.out.println("create success");
                        FirebaseUser signedUser = task.getResult().getUser();
                        if(signedUser != null){
                            user.put("id", signedUser.getUid());
                        }
                        DocumentReference userInfor = db.collection(Constants.KEY_COLLECTION_USERS).document(firebaseAuth.getCurrentUser().getUid());
                        userInfor.set(user).addOnSuccessListener(documentReference -> {
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    preferenceManager.putString(Constants.KEY_USED_ID, firebaseAuth.getCurrentUser().getUid());
                                    preferenceManager.putString(Constants.KEY_NAME,username);
                                    preferenceManager.putString(Constants.KEY_AVATAR, avatar);
                                    preferenceManager.putString(Constants.KEY_EMAIL, email);
                                    onSignUpSuccess();
                                })
                                .addOnFailureListener(exception ->{
                                    onSignUpFail(exception.getMessage());
                                });

                    } else {
                        Log.d("SignUpFragment", "signup: "+ task.getException());
                        // If sign in fails, display a message to the user.
                        onSignUpFail("Đăng ký thất bại!");
                    }
                });
    }

    private Boolean isValidSignUpDetails(User user){
        int colorEror = ContextCompat.getColor(requireActivity(), R.color.md_theme_light_error);
        int colorDefault = ContextCompat.getColor(requireActivity(), R.color.black);

        if(user.getEmail().isEmpty()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);
            binding.rePass.setBackgroundResource(R.drawable.edit_text_bg);
            binding.reEdtPass.setHintTextColor(colorDefault);

            binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPhone.setTextColor(colorEror);
            binding.status.setText("Hãy nhập Email!");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isValidEmail()){
            binding.password.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPass.setTextColor(colorDefault);
            binding.rePass.setBackgroundResource(R.drawable.edit_text_bg);
            binding.reEdtPass.setHintTextColor(colorDefault);

            binding.phone.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPhone.setTextColor(colorEror);
            binding.status.setText("Định dạng email chưa đúng!");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isValidPass()){
            binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPhone.setTextColor(colorDefault);

            binding.password.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPass.setTextColor(colorEror);
            binding.status.setText("Password cần tối thiểu 6 ký tự");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else if(!user.isConfirmPass()){
            binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
            binding.edtPhone.setTextColor(colorDefault);

            binding.password.setBackgroundResource(R.drawable.background_input_wrong);
            binding.edtPass.setTextColor(colorEror);
            binding.rePass.setBackgroundResource(R.drawable.background_input_wrong);
            binding.reEdtPass.setTextColor(colorEror);
            binding.status.setText("Hãy kiểm tra lại sao cho password trùng nhau");
            binding.status.setVisibility(View.VISIBLE);
            return false;
        }else{
            return true;
        }
    }

    private void onEditTextStatusChange(){
        int colorFocus = ContextCompat.getColor(requireActivity(), R.color.md_theme_light_primary);
        int colorDefault = ContextCompat.getColor(requireActivity(), R.color.secondary_text);
        binding.edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.username.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtUsername.setHintTextColor(colorFocus);
                }
                else {
                    binding.username.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtUsername.setHintTextColor(colorDefault);
//                    if (binding.inputCurrentPass.getText().toString().isEmpty()) {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_wrong);
//                    } else {
//                        binding.inputCurrentPass.setBackgroundResource(R.drawable.background_input_good);
//                    }
                }
            }
        });
        binding.edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.phone.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtPhone.setHintTextColor(colorFocus);
                }
                else {
                    binding.phone.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtPhone.setHintTextColor(colorDefault);
                }
            }
        });
        binding.edtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.password.setBackgroundResource(R.drawable.background_input_good);
                    binding.edtPass.setHintTextColor(colorFocus);
                }
                else {
                    binding.password.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.edtPass.setHintTextColor(colorDefault);
                }
            }
        });
        binding.reEdtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.rePass.setBackgroundResource(R.drawable.background_input_good);
                    binding.reEdtPass.setHintTextColor(colorFocus);
                }
                else {
                    binding.rePass.setBackgroundResource(R.drawable.edit_text_bg);
                    binding.reEdtPass.setHintTextColor(colorDefault);
                }
            }
        });
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
                    binding.imageProfile.setImageURI(resultUri);
                    binding.signup.setEnabled(!binding.edtPhone.getText().toString().trim().isEmpty() && !binding.edtPass.getText().toString().trim().isEmpty() && !binding.edtUsername.getText().toString().trim().isEmpty() && !binding.reEdtPass.getText().toString().trim().isEmpty());
                    try {
                        InputStream inputStream = requireActivity().getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        encodedImage = encodeImage(bitmap);

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
    public void onSignUpFail(String msg) {
        loading(false);
        binding.status.setText(msg);
        binding.status.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSignUpSuccess() {
        loading(false);
        binding.status.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
    public void loading(boolean b) {
        if (b){
            binding.status.setVisibility(View.INVISIBLE);
        }
        binding.loading.setVisibility(b? View.VISIBLE : View.GONE);
        binding.signup.setVisibility(b? View.GONE : View.VISIBLE);

    }

}