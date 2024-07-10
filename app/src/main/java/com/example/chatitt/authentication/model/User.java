package com.example.chatitt.authentication.model;

import android.util.Patterns;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    private String email, phonenumber,  password, re_password, avatar, name, id, coverImage, address, country, city, gender, birthday;
    private Boolean checked = false;
    private Boolean online = false;
    private List<String> friend_list, my_friend_request, other_request_friend;
    public User(String email, String password, String re_password, String avatar, String name) {
        this.email = email;
        this.password = password;
        this.re_password = re_password;
        this.avatar = avatar;
        this.name = name;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User() {
    }

    public List<String> getFriend_list() {
        return friend_list;
    }

    public void setFriend_list(List<String> friend_list) {
        this.friend_list = friend_list;
    }

    public List<String> getMy_friend_request() {
        return my_friend_request;
    }

    public void setMy_friend_request(List<String> my_friend_request) {
        this.my_friend_request = my_friend_request;
    }

    public List<String> getOther_request_friend() {
        return other_request_friend;
    }

    public void setOther_request_friend(List<String> other_request_friend) {
        this.other_request_friend = other_request_friend;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public User(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Boolean getOnline() {
        return online;
    }
    public void Boolean(Boolean online) {
        this.online = online;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
    public Boolean isValidPhone(){
        return Patterns.PHONE.matcher(phonenumber).matches() && phonenumber.startsWith("0") && phonenumber.length()!=10;
    }


}
