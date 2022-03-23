package com.example.emergencyalert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class EmergencyCentresActivity extends AppCompatActivity {

    List<EmergencyCentreInfo> emergencyCentreInfoList;
    RecyclerView myCentres_rc;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_centres);

        myCentres_rc = findViewById(R.id.myCentres_rc);
        currentUser = new User();
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");

        Log.d("sdfjhgf", currentUser.getUsername());

        findViewById(R.id.btn_addCentre).setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCentreActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        });

        emergencyCentreInfoList = new ArrayList<>();


        FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                .whereEqualTo("publisher_id", currentUser.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (error == null && value != null){
                        if (value.isEmpty()){
                            findViewById(R.id.empty_centre).setVisibility(View.VISIBLE);
                        }
                        emergencyCentreInfoList.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            EmergencyCentreInfo emergencyCentreInfo = documentSnapshot.toObject(EmergencyCentreInfo.class);
                            emergencyCentreInfoList.add(emergencyCentreInfo);

                        }
                        myCentres_rc.setAdapter(new EmergencyCentreAdapter());
                        myCentres_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    }
                });
    }

    private class EmergencyCentreAdapter extends RecyclerView.Adapter<EmergencyCentreAdapter.CentreViewHolder>{

        @NonNull
        @Override
        public CentreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CentreViewHolder(LayoutInflater.from(EmergencyCentresActivity.this).inflate(R.layout.emergency_centre_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CentreViewHolder holder, int position) {

            Picasso.get().load(emergencyCentreInfoList.get(position).getCentre_pic()).into(holder.centrePic);
            holder.centre_name.setText(emergencyCentreInfoList.get(position).getCentreName());
            holder.centre_contact.setText(MessageFormat.format("Phone : {0}", emergencyCentreInfoList.get(position).getCentreContact()));

        }

        @Override
        public int getItemCount() {
            return emergencyCentreInfoList.size();
        }

        public  class CentreViewHolder extends RecyclerView.ViewHolder{

            ImageView centrePic;
            TextView centre_name, centre_contact;

            public CentreViewHolder(@NonNull View itemView) {
                super(itemView);

                centrePic = itemView.findViewById(R.id.centrePic);
                centre_name = itemView.findViewById(R.id.centre_name);
                centre_contact = itemView.findViewById(R.id.centre_contact);

            }
        }
    }
}