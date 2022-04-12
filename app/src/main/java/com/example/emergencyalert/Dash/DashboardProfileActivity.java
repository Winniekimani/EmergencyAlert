package com.example.emergencyalert.Dash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.emergencyalert.Contact;
import com.example.emergencyalert.R;
import com.example.emergencyalert.User;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DashboardProfileActivity extends AppCompatActivity {

    public static User currentUser;
    public static String locationToSend;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 10000;
    private BottomNavigationView bottomNavigationView;
    private static final int GPS_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_profile);

        currentUser = new User();

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener((value, error) -> {
                    if (error == null && value != null){
                        currentUser = value.toObject(User.class);

                        FirebaseFirestore.getInstance().collection("User_Emergency_Contact")
                                .whereEqualTo("user_Emergency_Contact_User_Id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addSnapshotListener((value1, error1) -> {
                                    if (error1 == null && value1 != null){
                                        List<Contact> contactList = new ArrayList<>();
                                        for (DocumentSnapshot contactSnap: value1.getDocuments()) {
                                            Contact contact = contactSnap.toObject(Contact.class);
                                            contact.setUser_Emergency_Contact_Id(contactSnap.getId());
                                            contactList.add(contact);
                                            currentUser.setUser_ContactList(contactList);
                                        }
                                    }

                                });

                    }

                });





        DashPagerAdapter dashPagerAdapter = new DashPagerAdapter(this);
        ViewPager2 dashViewPager = findViewById(R.id.container);
        dashViewPager.setOffscreenPageLimit(4);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dashViewPager.setUserInputEnabled(false);

        dashViewPager.setAdapter(dashPagerAdapter);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        checkLocationPermission();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_home:
                    dashViewPager.setCurrentItem(0);
                    break;
                case R.id.menu_maps:
                    dashViewPager.setCurrentItem(1);
                    break;
                case R.id.menu_activity:
                    dashViewPager.setCurrentItem(2);
                    break;
                case R.id.menu_account:
                    dashViewPager.setCurrentItem(3);
                    break;
            }
            return true;
        });

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                //Below unassigned variable needed
                LocationSettingsResponse response = task.getResult(ApiException.class);
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(DashboardProfileActivity.this, GPS_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }

    public static class DashPagerAdapter extends FragmentStateAdapter {

        public DashPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new HomeFragment();

                case 1:
                    return new MapsFragment();
                case 2:
                    return new RecordingsFragment();
                case 3:
                    return new AccountFragment();
            }
            return new HomeFragment();
        }

        @Override
        public int getItemCount() {
            return 4;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            switch (resultCode) {
                case DashboardProfileActivity.RESULT_OK:
                    Toast.makeText(this, "GPS turned on successfully", Toast.LENGTH_SHORT).show();
                    checkLocationPermission();

                    break;
                case DashboardProfileActivity.RESULT_CANCELED:
                    Toast.makeText(this, "Location permission needed", Toast.LENGTH_SHORT).show();
                    checkLocationPermission();

                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + requestCode);
            }
        }

    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("Please grant location permissions")
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(DashboardProfileActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            getLocation();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                getLocation();
            } else {
                //Permission denied
                currentUser.setLongitude(String.valueOf(0));
                currentUser.setLatitude(String.valueOf(0));

            }
        }
    }

    public void getLocation() {
        Log.d("jdhakscnxsja", "Method called");
        fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            try {
                Geocoder geocoder = new Geocoder(DashboardProfileActivity.this, Locale.getDefault());
                 List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                currentUser.setLatitude(String.valueOf(addresses.get(0).getLatitude()));
                currentUser.setLongitude(String.valueOf(addresses.get(0).getLongitude()));
                locationToSend = addresses.get(0).getLocality();

                Log.d("jdhakscnxsja", currentUser.getLatitude());
                Log.d("longitude", currentUser.getLongitude());

                HashMap<String, Object> locationMap = new HashMap<>();
                locationMap.put("latitude", String.valueOf(addresses.get(0).getLatitude()));
                locationMap.put("longitude", String.valueOf(addresses.get(0).getLongitude()));

                FirebaseFirestore.getInstance().collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(locationMap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }).addOnFailureListener(e -> Toast.makeText(DashboardProfileActivity.this, "Could not get your location", Toast.LENGTH_SHORT).show());
    }


}