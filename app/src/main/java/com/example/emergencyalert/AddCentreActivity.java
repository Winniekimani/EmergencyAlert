package com.example.emergencyalert;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencyalert.Dash.DashboardProfileActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddCentreActivity extends AppCompatActivity {

    User currentUser;
    EmergencyCentreInfo emergencyCentreInfo;
    private SweetAlertDialog sweetAlertDialog;
    private Uri image_uri;
    ImageView centrePic;
    TextInputEditText edt_centreName, edt_centrePhone;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_centre);

        edt_centreName = findViewById(R.id.edt_centreName);
        edt_centrePhone = findViewById(R.id.edt_centrePhone);
        centrePic = findViewById(R.id.centrePic);

        Log.d("ertdryfgvubhknjl", DashboardProfileActivity.currentUser.getLatitude());


        //Listen to any photo which comes back
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                image_uri = result.getData().getData();
                Picasso.get().load(image_uri).into(centrePic);
            }
        });

        centrePic.setOnClickListener(view -> openGallery());

        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        emergencyCentreInfo = new EmergencyCentreInfo();

        findViewById(R.id.btn_addCentre).setOnClickListener(view -> addCentre(currentUser.getUserId()));


    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intentActivityResultLauncher.launch(intent);
    }

    private void addCentre(String userId) {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_centreName.getText()).toString()))
            edt_centreName.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_centrePhone.getText()).toString()))
            edt_centrePhone.setError(this.getString(R.string.input_error));
        else if (image_uri == null) Toast.makeText(this, "Image needed", Toast.LENGTH_SHORT).show();
        else {
            sweetAlertDialog = new SweetAlertDialog(AddCentreActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText("Creating centre");
            sweetAlertDialog.setContentText("Creating your centre");
            sweetAlertDialog.show();

            emergencyCentreInfo.setCentreName(edt_centreName.getText().toString().trim());
            emergencyCentreInfo.setCentreContact(edt_centrePhone.getText().toString().trim());
            emergencyCentreInfo.setLatitude(DashboardProfileActivity.currentUser.getLatitude());
            emergencyCentreInfo.setLongitude(DashboardProfileActivity.currentUser.getLongitude());
            emergencyCentreInfo.setPublisher_id(userId);

            FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                    .add(emergencyCentreInfo)
                    .addOnSuccessListener(documentReference -> setPhoto(documentReference.getId()));
        }
    }

    private void setPhoto(String docID) {
        Log.d("CREATE_UPDATES: ", "photo started to upload");
        sweetAlertDialog.setContentText("Uploading photo");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(docID);
        final StorageReference image_name = storageReference.child("images");

        try {
            image_name.putFile(image_uri).addOnSuccessListener(taskSnapshot -> image_name.getDownloadUrl().addOnSuccessListener(uri ->
                    FirebaseFirestore.getInstance().collection("Emergency_Centers_info")
                            .document(docID).update("centre_pic", uri.toString())
                            .addOnSuccessListener(unused -> {
                                Log.d("CREATE_UPDATES: ", "photo saved, registration complete");
                                Toast.makeText(AddCentreActivity.this, "Centre saved", Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.dismiss();
                                super.onBackPressed();


                            }).addOnFailureListener(runnable1 -> Log.d("CREATE_UPDATES: ", "photo failed to upload because " + runnable1.getMessage()))
            ));
        } catch (Exception e) {
            Log.d("CREATE_UPDATES: ", "failed to push file to firebase because " + e.getMessage());
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_hospital:
                if (checked)
                emergencyCentreInfo.setCentreType("hospital");
                break;

            case R.id.checkbox_police:
                if (checked)
                    emergencyCentreInfo.setCentreType("police");

                break;
            case R.id.checkbox_fire:
                if (checked)
                    emergencyCentreInfo.setCentreType("fire");
                break;
        }
    }
}