package com.example.emergencyalert;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SendEmeregencyRequestActivity extends AppCompatActivity {

    private User currentUser;
    private EmergencyCentreInfo emergencyCentreInfo;
    private ImageView centrePic;
    private TextView centre_name, centre_number;
    private TextInputEditText edt_emg_type, edt_emg_description;
    private MaterialButton btn_sendRequest;
    private EmergencyRequest emergencyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_emeregency_request);

        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        emergencyCentreInfo = (EmergencyCentreInfo) getIntent().getExtras().getSerializable("centre");
        emergencyRequest = new EmergencyRequest();

        centrePic = findViewById(R.id.centrePic);
        centre_name = findViewById(R.id.centre_name);
        centre_number = findViewById(R.id.centre_number);
        edt_emg_type = findViewById(R.id.edt_emg_type);
        edt_emg_description = findViewById(R.id.edt_emg_description);
        btn_sendRequest = findViewById(R.id.btn_sendRequest);



        Picasso.get().load(emergencyCentreInfo.getCentre_pic()).into(centrePic);
        centre_name.setText(emergencyCentreInfo.getEmergency_Center_Name());
        centre_number.setText(emergencyCentreInfo.getEmergency_Center_Contact());

        btn_sendRequest.setOnClickListener(view -> sendRequest());

    }

    private void sendRequest() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_emg_type.getText()).toString()))
            edt_emg_type.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_emg_description.getText()).toString()))
            edt_emg_description.setError(this.getString(R.string.input_error));
        else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SendEmeregencyRequestActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText("Sending request");
            sweetAlertDialog.setContentText("Sending request");
            sweetAlertDialog.show();

            EmergencyType emergencyType = new EmergencyType();
            emergencyType.setEmergency_Type_Name(edt_emg_type.getText().toString().trim());
            emergencyType.setEmergency_Type_Description(edt_emg_description.getText().toString().trim());


            FirebaseFirestore.getInstance().collection("Emergency_Type")
                    .add(emergencyType)
                    .addOnSuccessListener(documentReference -> {

                        FirebaseFirestore.getInstance().collection("Emergency_Type")
                                .document(documentReference.getId())
                                .update("emergency_Type_ID", documentReference.getId());



                        emergencyRequest.setEmergency_request_emergency_type_id(documentReference.getId());
                        emergencyRequest.setEmergency_request_emergency_centre_id(emergencyCentreInfo.getEmergency_Center_Id());
                        emergencyRequest.setEmergency_request_user_id(currentUser.getUser_Id());
                        emergencyRequest.setEmergency_request_status("pending");


                        FirebaseFirestore.getInstance().collection("Emergency_Request")
                                .add(emergencyRequest)
                                .addOnSuccessListener(documentReference1 -> {

                                    FirebaseFirestore.getInstance().collection("Emergency_Request")
                                            .document(documentReference1.getId())
                                            .update("emergency_request_id", documentReference1.getId());

                                    Toast.makeText(SendEmeregencyRequestActivity.this, "Request sent, wait", Toast.LENGTH_LONG).show();

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("time", Timestamp.now());
                                    hashMap.put("emergency_request_id", documentReference1.getId());

                                    FirebaseFirestore.getInstance().collection("Emergency_Request")
                                            .document(documentReference1.getId())
                                            .update(hashMap)
                                            .addOnSuccessListener(unused -> {
                                                sweetAlertDialog.dismissWithAnimation();
                                                SendEmeregencyRequestActivity.super.onBackPressed();
                                            });
                                });
                    });

        }

    }
}