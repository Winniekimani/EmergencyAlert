package com.example.emergencyalert;

import java.io.Serializable;

public class EmergencyRequest implements Serializable {

    String request_id, requester_userId, emergency_centreID, emergency_typeID, status,dateStr;

    public EmergencyRequest() {
    }

    public EmergencyRequest(String request_id, String requester_userId, String emergency_centreID, String emergency_typeID, String status) {
        this.request_id = request_id;
        this.requester_userId = requester_userId;
        this.emergency_centreID = emergency_centreID;
        this.emergency_typeID = emergency_typeID;
        this.status = status;

    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getRequester_userId() {
        return requester_userId;
    }

    public void setRequester_userId(String requester_userId) {
        this.requester_userId = requester_userId;
    }

    public String getEmergency_centreID() {
        return emergency_centreID;
    }

    public void setEmergency_centreID(String emergency_centreID) {
        this.emergency_centreID = emergency_centreID;
    }

    public String getEmergency_typeID() {
        return emergency_typeID;
    }

    public void setEmergency_typeID(String emergency_typeID) {
        this.emergency_typeID = emergency_typeID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
