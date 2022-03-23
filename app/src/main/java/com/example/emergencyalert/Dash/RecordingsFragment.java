package com.example.emergencyalert.Dash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyalert.EmergencyCentreInfo;
import com.example.emergencyalert.EmergencyRequest;
import com.example.emergencyalert.EmergencyType;
import com.example.emergencyalert.R;
import com.example.emergencyalert.RequestViewActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RecordingsFragment extends Fragment {

    private List<EmergencyRequest> emergencyRequestList;
    private RecyclerView records_rc;
    private View view;


    public RecordingsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recordings, container, false);

        records_rc = view.findViewById(R.id.records_rc);
        emergencyRequestList = new ArrayList<>();

        return view;
    }

    private class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.RecordingsViewHolder>{

        @NonNull
        @Override
        public RecordingsAdapter.RecordingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecordingsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecordingsAdapter.RecordingsViewHolder holder, int position) {

            final EmergencyCentreInfo[] emergencyCentreInfo = {new EmergencyCentreInfo()};

            FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                    .document(emergencyRequestList.get(position).getEmergency_centreID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        emergencyCentreInfo[0] = documentSnapshot.toObject(EmergencyCentreInfo.class);
                        if (emergencyCentreInfo[0] != null){
                            Picasso.get().load(emergencyCentreInfo[0].getCentre_pic()).into(holder.centrePic);
                            holder.centre_name.setText(emergencyCentreInfo[0].getCentreName());
                            holder.centre_contact.setText(emergencyCentreInfo[0].getCentreContact());
                        }
                    });

            final EmergencyType[] emergencyType = {new EmergencyType()};

            FirebaseFirestore.getInstance().collection("Emergency_Type")
                    .document(emergencyRequestList.get(position).getEmergency_typeID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        emergencyType[0] = documentSnapshot.toObject(EmergencyType.class);
                        if (emergencyType[0] != null){
                            holder.emergency_type.setText(emergencyType[0].getEmergencyTypeName());
                            holder.txt_emergency_des.setText(emergencyType[0].getEmergencyTypeDescription());
                        }
                    });

            FirebaseFirestore.getInstance().collection("Emergency_Request")
                    .document(emergencyRequestList.get(position).getRequest_id())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            long timestamp1 = documentSnapshot.getTimestamp("time").toDate().getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm", Locale.US);
                            String dateStr = sdf.format(timestamp1);
                            holder.txt_time.setText(dateStr);

                        }
                    });

            holder.txt_status.setText(emergencyRequestList.get(position).getStatus());

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), RequestViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("emergencyItem", emergencyRequestList.get(position));
                bundle.putString("pic", emergencyCentreInfo[0].getCentre_pic());
                bundle.putString("name", emergencyCentreInfo[0].getCentreName());
                bundle.putString("phone", emergencyCentreInfo[0].getCentreContact());
                bundle.putString("type", emergencyType[0].getEmergencyTypeName());
                bundle.putString("description", emergencyType[0].getEmergencyTypeDescription());
                intent.putExtras(bundle);
                startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return emergencyRequestList.size();
        }
        private class RecordingsViewHolder extends RecyclerView.ViewHolder{

            ImageView centrePic;
            TextView centre_name, centre_contact, txt_status, txt_time, emergency_type, txt_emergency_des;

            public RecordingsViewHolder(@NonNull View itemView) {
                super(itemView);

                centrePic = itemView.findViewById(R.id.centrePic);
                centre_name = itemView.findViewById(R.id.centre_name);
                centre_contact = itemView.findViewById(R.id.centre_contact);
                txt_status = itemView.findViewById(R.id.txt_status);
                txt_time = itemView.findViewById(R.id.txt_time);
                emergency_type = itemView.findViewById(R.id.emergency_type);
                txt_emergency_des = itemView.findViewById(R.id.txt_emergency_des);


            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        new Handler().postDelayed(() -> FirebaseFirestore.getInstance().collection("Emergency_Request")
                .whereEqualTo("requester_userId", DashboardProfileActivity.currentUser.getUserId())
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error == null && value != null){
                        view.findViewById(R.id.progress_circular).setVisibility(View.GONE);
                        emergencyRequestList.clear();
                        for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                            EmergencyRequest emergencyRequest = documentSnapshot.toObject(EmergencyRequest.class);
                            emergencyRequest.setRequest_id(documentSnapshot.getId());
                            emergencyRequestList.add(emergencyRequest);

                        }
                        records_rc.setAdapter(new RecordingsAdapter());
                        records_rc.setHasFixedSize(true);
                        records_rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }

                }), 1000);

    }
}