package com.example.emergencyalert;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    String username, userId, phone, user_profile_pic, latitude, longitude;
    List<Contact> contactList;

    public User() {
    }

    public User(String username, String userId, String phone, String user_profile_pic, String latitude, String longitude, List<Contact> contactList) {
        this.username = username;
        this.userId = userId;
        this.phone = phone;
        this.user_profile_pic = user_profile_pic;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactList = contactList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
