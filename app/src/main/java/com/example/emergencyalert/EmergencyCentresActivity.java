package com.example.emergencyalert;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmergencyCentresActivity extends AppCompatActivity {

    List<EmergencyCentreInfo> emergencyCentreInfoList;
    RecyclerView myCentres_rc;
    User currentUser;
    ImageView btn_dnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_centres);

        myCentres_rc = findViewById(R.id.myCentres_rc);
        btn_dnd = findViewById(R.id.btn_dnd);
        currentUser = new User();
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");

        Log.d("sdfjhgf", currentUser.getUser_Name());

        findViewById(R.id.btn_addCentre).setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCentreActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        });

        emergencyCentreInfoList = new ArrayList<>();


        FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                .whereEqualTo("emergency_Center_Publisher_Id", currentUser.getUser_Id())
                .addSnapshotListener((value, error) -> {
                    if (error == null && value != null){
                        if (value.isEmpty()){
                            findViewById(R.id.empty_centre).setVisibility(View.VISIBLE);

                        }
                        emergencyCentreInfoList.clear();
                        findViewById(R.id.empty_centre).setVisibility(View.GONE);
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            EmergencyCentreInfo emergencyCentreInfo = documentSnapshot.toObject(EmergencyCentreInfo.class);
                            emergencyCentreInfo.setEmergency_Center_Id(documentSnapshot.getId());
                            emergencyCentreInfoList.add(emergencyCentreInfo);

                        }
                        myCentres_rc.setAdapter(new EmergencyCentreAdapter());
                        myCentres_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    }
                });

        btn_dnd.setOnClickListener(view -> {
            Toast.makeText(EmergencyCentresActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            try {
                String[] columns = {"centre_pic", "Emergency_Center_Id", "Emergency_Center_Name", "Emergency_Center_Contact", "Emergency_Center_Type", "Emergency_Center_Publisher_Id", "latitude", "longitude"};
                exportCSV(emergencyCentreInfoList, Arrays.toString(columns));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR", e.getLocalizedMessage());
            }
        });
    }

    private void exportCSV(List<EmergencyCentreInfo> emergencyCentreInfoList, String header) throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);

        File file = new File(path, "/" + "emergencyCenters.csv");

        PrintWriter writer = new PrintWriter(file);
        writer.println(header);

        for (EmergencyCentreInfo emergencyCentreInfo : emergencyCentreInfoList) {
            writer.println(emergencyCentreInfo.toString());
        }
        writer.close();
        Toast.makeText(EmergencyCentresActivity.this, "Done", Toast.LENGTH_SHORT).show();

    }


    private class EmergencyCentreAdapter extends RecyclerView.Adapter<EmergencyCentreAdapter.CentreViewHolder>{

        @NonNull
        @Override
        public CentreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CentreViewHolder(LayoutInflater.from(EmergencyCentresActivity.this).inflate(R.layout.emergency_centre_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CentreViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Picasso.get().load(emergencyCentreInfoList.get(position).getCentre_pic()).into(holder.centrePic);
            holder.centre_name.setText(emergencyCentreInfoList.get(position).getEmergency_Center_Name());
            holder.centre_contact.setText(MessageFormat.format("Phone : {0}", emergencyCentreInfoList.get(position).getEmergency_Center_Contact()));
            holder.itemView.findViewById(R.id.btn_removeCentre).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(EmergencyCentresActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCanceledOnTouchOutside(true);
                    sweetAlertDialog.setTitleText("Delete Centre");
                    sweetAlertDialog.setContentText("Are you sure you want this centre deleted");
                    sweetAlertDialog.setConfirmButton("Delete", sweetAlertDialog1 -> {

                        FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                                .document(emergencyCentreInfoList.get(position).getEmergency_Center_Id())
                                .delete()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(EmergencyCentresActivity.this, "Centre deleted", Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.dismissWithAnimation();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(EmergencyCentresActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.dismissWithAnimation();
                                });
                    });
                    sweetAlertDialog.setCancelButton("Cancel", sweetAlertDialog12 -> sweetAlertDialog.dismissWithAnimation());
                    sweetAlertDialog.show();
                }
            });

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