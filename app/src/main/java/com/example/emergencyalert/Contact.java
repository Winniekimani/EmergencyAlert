package com.example.emergencyalert;

import java.io.Serializable;

public class Contact implements Serializable {

    String User_Emergency_Contact_Id, User_Emergency_Contact_User_Id, User_Emergency_Contact_Name, User_Emergency_Contact_Mobile, User_Emergency_Contact_User_Relationship ;

    public Contact() {
    }

    public Contact(String user_Emergency_Contact_Id, String user_Emergency_Contact_User_Id, String user_Emergency_Contact_Name, String user_Emergency_Contact_Mobile, String user_Emergency_Contact_User_Relationship) {
        User_Emergency_Contact_Id = user_Emergency_Contact_Id;
        User_Emergency_Contact_User_Id = user_Emergency_Contact_User_Id;
        User_Emergency_Contact_Name = user_Emergency_Contact_Name;
        User_Emergency_Contact_Mobile = user_Emergency_Contact_Mobile;
        User_Emergency_Contact_User_Relationship = user_Emergency_Contact_User_Relationship;
    }

    public String getUser_Emergency_Contact_Id() {
        return User_Emergency_Contact_Id;
    }

    public void setUser_Emergency_Contact_Id(String user_Emergency_Contact_Id) {
        User_Emergency_Contact_Id = user_Emergency_Contact_Id;
    }

    public String getUser_Emergency_Contact_User_Id() {
        return User_Emergency_Contact_User_Id;
    }

    public void setUser_Emergency_Contact_User_Id(String user_Emergency_Contact_User_Id) {
        User_Emergency_Contact_User_Id = user_Emergency_Contact_User_Id;
    }

    public String getUser_Emergency_Contact_Name() {
        return User_Emergency_Contact_Name;
    }

    public void setUser_Emergency_Contact_Name(String user_Emergency_Contact_Name) {
        User_Emergency_Contact_Name = user_Emergency_Contact_Name;
    }

    public String getUser_Emergency_Contact_Mobile() {
        return User_Emergency_Contact_Mobile;
    }

    public void setUser_Emergency_Contact_Mobile(String user_Emergency_Contact_Mobile) {
        User_Emergency_Contact_Mobile = user_Emergency_Contact_Mobile;
    }

    public String getUser_Emergency_Contact_User_Relationship() {
        return User_Emergency_Contact_User_Relationship;
    }

    public void setUser_Emergency_Contact_User_Relationship(String user_Emergency_Contact_User_Relationship) {
        User_Emergency_Contact_User_Relationship = user_Emergency_Contact_User_Relationship;
    }
}
