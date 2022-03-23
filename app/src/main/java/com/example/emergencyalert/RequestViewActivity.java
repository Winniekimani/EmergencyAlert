package com.example.emergencyalert;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class RequestViewActivity extends AppCompatActivity {

    private EmergencyRequest emergencyRequest;
    private ImageView centrePic;
    private TextView centre_name, centre_contact, emergency_type, txt_emergency_des, txt_status, txt_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_view);

        emergencyRequest = (EmergencyRequest) getIntent().getExtras().getSerializable("emergencyItem");
        centrePic = findViewById(R.id.centrePic);
        centre_name = findViewById(R.id.centre_name);
        centre_contact = findViewById(R.id.centre_contact);
        emergency_type = findViewById(R.id.emergency_type);
        txt_emergency_des = findViewById(R.id.txt_emergency_des);
        txt_status = findViewById(R.id.txt_status);
        txt_time = findViewById(R.id.txt_time);


        Picasso.get().load(String.valueOf(getIntent().getExtras().get("pic"))).into(centrePic);
        centre_name.setText(getIntent().getExtras().getString("name"));
        emergency_type.setText(getIntent().getExtras().getString("type"));
        txt_emergency_des.setText(getIntent().getExtras().getString("description"));
        txt_status.setText(emergencyRequest.getStatus());

        if (emergencyRequest.getStatus().equals("Served")){
            findViewById(R.id.btn_status).setEnabled(false);
        }

        findViewById(R.id.btn_status).setOnClickListener(view -> {
            findViewById(R.id.btn_status).setEnabled(false);

                txt_status.setText("Served");

                FirebaseFirestore.getInstance().collection("Emergency_Request")
                        .document(emergencyRequest.getRequest_id())
                        .update("status", "Served")
                        .addOnSuccessListener(RequestViewActivity.this, unused -> RequestViewActivity.super.onBackPressed());
        });





    }
}