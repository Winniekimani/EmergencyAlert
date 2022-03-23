package com.example.emergencyalert;

import java.io.Serializable;

public class EmergencyCentreInfo implements Serializable {

    String centre_pic, centreID, centreName, centreContact, centreType, publisher_id, latitude, longitude;

    public EmergencyCentreInfo() {
        //Needed
    }

    public EmergencyCentreInfo(String centre_pic, String centreID, String centreName, String centreContact, String centreType, String publisher_id, String latitude, String longitude) {
        this.centre_pic = centre_pic;
        this.centreID = centreID;
        this.centreName = centreName;
        this.centreContact = centreContact;
        this.centreType = centreType;
        this.publisher_id = publisher_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCentre_pic() {
        return centre_pic;
    }

    public void setCentre_pic(String centre_pic) {
        this.centre_pic = centre_pic;
    }

    public String getCentreID() {
        return centreID;
    }

    public void setCentreID(String centreID) {
        this.centreID = centreID;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public String getCentreContact() {
        return centreContact;
    }

    public void setCentreContact(String centreContact) {
        this.centreContact = centreContact;
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

    public String getCentreType() {
        return centreType;
    }

    public void setCentreType(String centreType) {
        this.centreType = centreType;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }
}
