package com.example.emergencyalert;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    String User_Name, User_Id, User_Mobile, User_Email, user_profile_pic, latitude, longitude;
    List<Contact> User_ContactList;

    public User() {
    }

    public User(String user_Name, String user_Id, String user_Mobile, String user_Email, String user_profile_pic, String latitude, String longitude, List<Contact> user_ContactList) {
        User_Name = user_Name;
        User_Id = user_Id;
        User_Mobile = user_Mobile;
        User_Email = user_Email;
        this.user_profile_pic = user_profile_pic;
        this.latitude = latitude;
        this.longitude = longitude;
        User_ContactList = user_ContactList;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Mobile() {
        return User_Mobile;
    }

    public void setUser_Mobile(String user_Mobile) {
        User_Mobile = user_Mobile;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
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

    public List<Contact> getUser_ContactList() {
        return User_ContactList;
    }

    public void setUser_ContactList(List<Contact> user_ContactList) {
        User_ContactList = user_ContactList;
    }
}
