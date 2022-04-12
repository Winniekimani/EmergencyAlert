package com.example.emergencyalert;

import java.io.Serializable;

public class EmergencyRequest implements Serializable {

    public String emergency_request_id, emergency_request_user_id , emergency_request_emergency_centre_id, emergency_request_emergency_type_id , emergency_request_status;

    public EmergencyRequest() {
    }

    public EmergencyRequest(String emergency_request_id, String emergency_request_user_id, String emergency_request_emergency_centre_id, String emergency_request_emergency_type_id, String emergency_request_status) {
        this.emergency_request_id = emergency_request_id;
        this.emergency_request_user_id = emergency_request_user_id;
        this.emergency_request_emergency_centre_id = emergency_request_emergency_centre_id;
        this.emergency_request_emergency_type_id = emergency_request_emergency_type_id;
        this.emergency_request_status = emergency_request_status;
    }

    public String getEmergency_request_id() {
        return emergency_request_id;
    }

    public void setEmergency_request_id(String emergency_request_id) {
        this.emergency_request_id = emergency_request_id;
    }

    public String getEmergency_request_user_id() {
        return emergency_request_user_id;
    }

    public void setEmergency_request_user_id(String emergency_request_user_id) {
        this.emergency_request_user_id = emergency_request_user_id;
    }

    public String getEmergency_request_emergency_centre_id() {
        return emergency_request_emergency_centre_id;
    }

    public void setEmergency_request_emergency_centre_id(String emergency_request_emergency_centre_id) {
        this.emergency_request_emergency_centre_id = emergency_request_emergency_centre_id;
    }

    public String getEmergency_request_emergency_type_id() {
        return emergency_request_emergency_type_id;
    }

    public void setEmergency_request_emergency_type_id(String emergency_request_emergency_type_id) {
        this.emergency_request_emergency_type_id = emergency_request_emergency_type_id;
    }

    public String getEmergency_request_status() {
        return emergency_request_status;
    }

    public void setEmergency_request_status(String emergency_request_status) {
        this.emergency_request_status = emergency_request_status;
    }

    @Override
    public String toString() {
        StringBuilder dataBuilder = new StringBuilder();
        appendFieldValue(dataBuilder, emergency_request_id);
        appendFieldValue(dataBuilder, emergency_request_user_id);
        appendFieldValue(dataBuilder, emergency_request_emergency_centre_id);
        appendFieldValue(dataBuilder, emergency_request_emergency_type_id);
        appendFieldValue(dataBuilder, emergency_request_status);

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
