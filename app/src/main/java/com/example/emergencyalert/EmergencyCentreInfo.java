package com.example.emergencyalert;

import java.io.Serializable;

public class EmergencyCentreInfo implements Serializable {

    String centre_pic, Emergency_Center_Id, Emergency_Center_Name, Emergency_Center_Contact, Emergency_Center_Type, Emergency_Center_Publisher_Id, latitude, longitude;

    public EmergencyCentreInfo() {
        //Needed
    }

    public EmergencyCentreInfo(String centre_pic, String emergency_Center_Id, String emergency_Center_Name, String emergency_Center_Contact, String emergency_Center_Type, String emergency_Center_Publisher_Id, String latitude, String longitude) {
        this.centre_pic = centre_pic;
        Emergency_Center_Id = emergency_Center_Id;
        Emergency_Center_Name = emergency_Center_Name;
        Emergency_Center_Contact = emergency_Center_Contact;
        Emergency_Center_Type = emergency_Center_Type;
        Emergency_Center_Publisher_Id = emergency_Center_Publisher_Id;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public String getCentre_pic() {
        return centre_pic;
    }

    public void setCentre_pic(String centre_pic) {
        this.centre_pic = centre_pic;
    }

    public String getEmergency_Center_Id() {
        return Emergency_Center_Id;
    }

    public void setEmergency_Center_Id(String emergency_Center_Id) {
        Emergency_Center_Id = emergency_Center_Id;
    }

    public String getEmergency_Center_Name() {
        return Emergency_Center_Name;
    }

    public void setEmergency_Center_Name(String emergency_Center_Name) {
        Emergency_Center_Name = emergency_Center_Name;
    }

    public String getEmergency_Center_Contact() {
        return Emergency_Center_Contact;
    }

    public void setEmergency_Center_Contact(String emergency_Center_Contact) {
        Emergency_Center_Contact = emergency_Center_Contact;
    }

    public String getEmergency_Center_Type() {
        return Emergency_Center_Type;
    }

    public void setEmergency_Center_Type(String emergency_Center_Type) {
        Emergency_Center_Type = emergency_Center_Type;
    }

    public String getEmergency_Center_Publisher_Id() {
        return Emergency_Center_Publisher_Id;
    }

    public void setEmergency_Center_Publisher_Id(String emergency_Center_Publisher_Id) {
        Emergency_Center_Publisher_Id = emergency_Center_Publisher_Id;
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

    @Override
    public String toString() {
        StringBuilder dataBuilder = new StringBuilder();
        appendFieldValue(dataBuilder, centre_pic);
        appendFieldValue(dataBuilder, Emergency_Center_Id);
        appendFieldValue(dataBuilder, Emergency_Center_Name);
        appendFieldValue(dataBuilder, Emergency_Center_Contact);
        appendFieldValue(dataBuilder, Emergency_Center_Type);
        appendFieldValue(dataBuilder, Emergency_Center_Publisher_Id);
        appendFieldValue(dataBuilder, latitude);
        appendFieldValue(dataBuilder, longitude);

        return dataBuilder.toString();
    }

    private void appendFieldValue(StringBuilder dataBuilder, String fieldValue) {
        if(fieldValue != null) {
            dataBuilder.append(fieldValue).append(",");
        } else {
            dataBuilder.append("").append(",");
        }
    }
}
