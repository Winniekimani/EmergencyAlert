package com.example.emergencyalert;

public class EmergencyType {
    String Emergency_Type_ID, Emergency_Type_Name, Emergency_Type_Description;

    public EmergencyType() {
    }

    public EmergencyType(String emergency_Type_ID, String emergency_Type_Name, String emergency_Type_Description) {
        Emergency_Type_ID = emergency_Type_ID;
        Emergency_Type_Name = emergency_Type_Name;
        Emergency_Type_Description = emergency_Type_Description;
    }

    public String getEmergency_Type_ID() {
        return Emergency_Type_ID;
    }

    public void setEmergency_Type_ID(String emergency_Type_ID) {
        Emergency_Type_ID = emergency_Type_ID;
    }

    public String getEmergency_Type_Name() {
        return Emergency_Type_Name;
    }

    public void setEmergency_Type_Name(String emergency_Type_Name) {
        Emergency_Type_Name = emergency_Type_Name;
    }

    public String getEmergency_Type_Description() {
        return Emergency_Type_Description;
    }

    public void setEmergency_Type_Description(String emergency_Type_Description) {
        Emergency_Type_Description = emergency_Type_Description;
    }
}
