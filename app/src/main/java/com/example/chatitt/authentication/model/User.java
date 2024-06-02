package com.example.chatitt.authentication.model;

import android.util.Patterns;

import java.util.Objects;

public class User {
    private String email, password, re_password, avatar, publicKey, username, id, coverImage;

    public User(String email, String password, String re_password, String avatar, String username) {
        this.email = email;
        this.password = password;
        this.re_password = re_password;
        this.avatar = avatar;
        this.username = username;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isValidEmail(){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public Boolean isValidPass(){
        return password.length() > 5;
    }
    public Boolean isConfirmPass(){
        return Objects.equals(password, re_password);
    }
}
