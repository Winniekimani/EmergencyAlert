package com.example.emergencyalert;

public class EmergencyType {
    String emergencyTypeID, emergencyTypeName, emergencyTypeDescription;

    public EmergencyType() {
    }

    public EmergencyType(String emergencyTypeID, String emergencyTypeName, String emergencyTypeDescription) {
        this.emergencyTypeID = emergencyTypeID;
        this.emergencyTypeName = emergencyTypeName;
        this.emergencyTypeDescription = emergencyTypeDescription;
    }

    public String getEmergencyTypeID() {
        return emergencyTypeID;
    }

    public void setEmergencyTypeID(String emergencyTypeID) {
        this.emergencyTypeID = emergencyTypeID;
    }

    public String getEmergencyTypeName() {
        return emergencyTypeName;
    }

    public void setEmergencyTypeName(String emergencyTypeName) {
        this.emergencyTypeName = emergencyTypeName;
    }

    public String getEmergencyTypeDescription() {
        return emergencyTypeDescription;
    }

    public void setEmergencyTypeDescription(String emergencyTypeDescription) {
        this.emergencyTypeDescription = emergencyTypeDescription;
    }
}
