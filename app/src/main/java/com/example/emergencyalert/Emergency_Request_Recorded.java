package com.example.emergencyalert;

public class Emergency_Request_Recorded {
    String Emergency_Request_Recorded_Id, Emergency_Request_Recorded_Emergency_Request_Id;

    public Emergency_Request_Recorded() {
    }

    public Emergency_Request_Recorded(String emergency_Request_Recorded_Id, String emergency_Request_Recorded_Emergency_Request_Id) {
        Emergency_Request_Recorded_Id = emergency_Request_Recorded_Id;
        Emergency_Request_Recorded_Emergency_Request_Id = emergency_Request_Recorded_Emergency_Request_Id;
    }

    public String getEmergency_Request_Recorded_Id() {
        return Emergency_Request_Recorded_Id;
    }

    public void setEmergency_Request_Recorded_Id(String emergency_Request_Recorded_Id) {
        Emergency_Request_Recorded_Id = emergency_Request_Recorded_Id;
    }

    public String getEmergency_Request_Recorded_Emergency_Request_Id() {
        return Emergency_Request_Recorded_Emergency_Request_Id;
    }

    public void setEmergency_Request_Recorded_Emergency_Request_Id(String emergency_Request_Recorded_Emergency_Request_Id) {
        Emergency_Request_Recorded_Emergency_Request_Id = emergency_Request_Recorded_Emergency_Request_Id;
    }
}
