package com.example.emergencyalert;

import java.io.Serializable;

public class Contact implements Serializable {

    String contact_id, user_id, name, number, relationship;

    public Contact() {
    }

    public Contact(String contact_id, String user_id, String name, String number, String relationship) {
        this.contact_id = contact_id;
        this.user_id = user_id;
        this.name = name;
        this.number = number;
        this.relationship = relationship;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
