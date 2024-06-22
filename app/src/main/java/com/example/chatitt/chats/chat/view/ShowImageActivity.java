package com.example.chatitt.chats.chat.view;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatitt.MainActivity;
import com.example.chatitt.databinding.ActivityShowImageBinding;
import com.example.chatitt.ultilities.Constants;
import com.example.chatitt.ultilities.Helpers;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ShowImageActivity extends AppCompatActivity {

    private ActivityShowImageBinding binding;
    private String imgUri;
    private static final int REQUEST_PERMISSION_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        bitmap = Helpers.getBitmapFromEncodedString(getIntent().getStringExtra(Constants.KEY_TYPE_IMAGE));
//        binding.myZoomageView.setImageBitmap(bitmap);
        imgUri = getIntent().getStringExtra(Constants.KEY_TYPE_IMAGE);
        Picasso.get().load(Uri.parse(imgUri)).into(binding.myZoomageView);
        binding.exitImg.setOnClickListener(v-> onBackPressed());

        binding.actionDown.setOnClickListener(v -> {
            checkPermission();
        });
    }

    private void checkPermission() {
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            String [] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, REQUEST_PERMISSION_CODE);
        } else {
            startDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                Toast.makeText(this, "Quyền bị từ chối, bạn không thể tải ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDownload() {
        saveImage(imgUri, String.valueOf(System.currentTimeMillis()));
    }


    private void saveImage(String  imgUri, String image_name) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imgUri));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Tải ảnh...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()) + ".jpg");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if(downloadManager != null){
            downloadManager.enqueue(request);
            Helpers.showToast(getApplicationContext(),"Đang tải ảnh...");
        }
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/Download/";
//        File myDir = new File(root);
//        myDir.mkdirs();
//        String fname = "Image-" + image_name+ ".jpg";
//        File file = new File(myDir, fname);
//        if (file.exists()) file.delete();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//            Toast.makeText(getApplicationContext(), "Download successful", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}