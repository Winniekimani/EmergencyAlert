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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyalert.EmergencyCentreInfo;
import com.example.emergencyalert.R;
import com.example.emergencyalert.SendEmeregencyRequestActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private double current_lat;
    private double current_long;
    private RecyclerView centres_rc;
    private GoogleMap my_googleMap;
    private List<EmergencyCentreInfo> emergencyCentreInfoList;
    private Query query;
    private ChipGroup chipGroup;


    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            my_googleMap = googleMap;
            addMarkers();

        }
    };

    private void addMarkers() {

        if (my_googleMap != null) {
            my_googleMap.clear();
            LatLng myLocation = new LatLng(current_lat, current_long);
            my_googleMap.addMarker(new MarkerOptions().position(myLocation).title("Winnie"));
            my_googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));

            for (EmergencyCentreInfo emergencyCentreInfo : emergencyCentreInfoList){
                LatLng centreLocation = new LatLng(Float.parseFloat(emergencyCentreInfo.getLatitude()), Float.parseFloat(emergencyCentreInfo.getLongitude()));
                my_googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(centreLocation).title(emergencyCentreInfo.getEmergency_Center_Name()));
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        centres_rc = view.findViewById(R.id.centres_rc);
        emergencyCentreInfoList = new ArrayList<>();
        chipGroup = view.findViewById(R.id.chipGroup);

        new Handler().postDelayed(() -> {
            current_lat = Float.parseFloat(DashboardProfileActivity.currentUser.getLatitude());
            current_long = Double.parseDouble(DashboardProfileActivity.currentUser.getLongitude());
            addMarkers();
        }, 1000);


        query = FirebaseFirestore.getInstance().collection("Emergency_Centers_info");
        getCentres(query);

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.chip_hospitals:
                    query = FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                            .whereEqualTo("emergency_Center_Type", "hospital");
                    getCentres(query);
                    break;
                case R.id.chip_police:
                    query = FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                            .whereEqualTo("emergency_Center_Type", "police");
                    getCentres(query);
                    break;
                case R.id.chip_fire:
                    query = FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                            .whereEqualTo("emergency_Center_Type", "fire");
                    getCentres(query);
                    break;
            }
        });


        return view;
    }

    private void getCentres(Query query1) {
        query1.addSnapshotListener((value, error) -> {
                    if (error == null && value != null){
                        emergencyCentreInfoList.clear();
                        if (my_googleMap != null) my_googleMap.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            EmergencyCentreInfo emergencyCentreInfo = documentSnapshot.toObject(EmergencyCentreInfo.class);
                            if (emergencyCentreInfo != null)
                            emergencyCentreInfo.setEmergency_Center_Id(documentSnapshot.getId());
                            emergencyCentreInfoList.add(emergencyCentreInfo);

                        }
                        if (my_googleMap != null) addMarkers();
                        centres_rc.setAdapter(new EmergencyCentreAdapter());
                        centres_rc.setHasFixedSize(true);
                        centres_rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }

                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private class EmergencyCentreAdapter extends RecyclerView.Adapter<EmergencyCentreAdapter.CentreViewHolder>{

        @NonNull
        @Override
        public EmergencyCentreAdapter.CentreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EmergencyCentreAdapter.CentreViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.emergency_centre_map_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EmergencyCentreAdapter.CentreViewHolder holder, int position) {

            Picasso.get().load(emergencyCentreInfoList.get(position).getCentre_pic()).into(holder.centrePic);
            holder.centre_name.setText(emergencyCentreInfoList.get(position).getEmergency_Center_Name());
            holder.centre_contact.setText(MessageFormat.format("Phone : {0}", emergencyCentreInfoList.get(position).getEmergency_Center_Contact()));

            holder.btn_sendRequest.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), SendEmeregencyRequestActivity.class);
                intent.putExtra("centre", emergencyCentreInfoList.get(position));
                intent.putExtra("currentUser",DashboardProfileActivity.currentUser);
                startActivity(intent);

            });


        }

        @Override
        public int getItemCount() {
            return emergencyCentreInfoList.size();
        }

        public  class CentreViewHolder extends RecyclerView.ViewHolder{

            ImageView centrePic;
            TextView centre_name, centre_contact;
            MaterialButton btn_sendRequest;

            public CentreViewHolder(@NonNull View itemView) {
                super(itemView);

                centrePic = itemView.findViewById(R.id.centrePic);
                centre_name = itemView.findViewById(R.id.centre_name);
                centre_contact = itemView.findViewById(R.id.centre_contact);
                btn_sendRequest = itemView.findViewById(R.id.btn_sendRequest);

            }
        }
    }



}