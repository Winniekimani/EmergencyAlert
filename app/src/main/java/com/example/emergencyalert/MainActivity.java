package com.example.emergencyalert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.emergencyalert.Dash.DashboardProfileActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText edt_username, edt_email, edt_password, edt_password_confirm, edt_phone;
    String dialogContent2;
    private SweetAlertDialog sweetAlertDialog;
    private CircleImageView img_userDp;
    private TextView txt_photo_error;
    private FirebaseAuth mAuth;
    private Uri image_uri;
    private String userID;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        //Check if user exists and take him to the dashboard
        if (mAuth.getCurrentUser() != null){
            nextActivityifExists();
        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 0);
        }

        dialogContent2 = this.getString(R.string.setting_up_photo);

        //Listen to any photo which comes back
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                image_uri = result.getData().getData();
                Picasso.get().load(image_uri).into(img_userDp);
            }
        });


        //Find views
        edt_username = findViewById(R.id.edt_username);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_password_confirm = findViewById(R.id.edt_password_confirm);
        txt_photo_error = findViewById(R.id.txt_photo_error);
        img_userDp = findViewById(R.id.img_userDp);
        edt_phone = findViewById(R.id.edt_phone);

        findViewById(R.id.btn_signUp).setOnClickListener(view -> signUpUser());
        findViewById(R.id.btn_toLogIn).setOnClickListener(view -> backPress());
        img_userDp.setOnClickListener(view1 -> openGallery());

    }

    private void signUpUser() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_username.getText()).toString()))
            edt_username.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_email.getText()).toString()))
            edt_email.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_phone.getText()).toString()))
            edt_email.setError(this.getString(R.string.input_error));
        else if (Objects.requireNonNull(edt_password.getText()).toString().length() < 6)
            edt_password.setError(this.getString(R.string.password_length_error));
        else if (!Objects.requireNonNull(edt_password.getText()).toString().equals(Objects.requireNonNull(edt_password_confirm.getText()).toString()))
            edt_password_confirm.setError(this.getString(R.string.password_matching_error));
        else if (image_uri == null)
            txt_photo_error.setVisibility(View.VISIBLE);
        else {
            txt_photo_error.setVisibility(View.GONE);

            sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText(this.getString(R.string.creating_account_dialog_title));
            sweetAlertDialog.setContentText(this.getString(R.string.account_dialog_content));
            sweetAlertDialog.show();

            mAuth.createUserWithEmailAndPassword(edt_email.getText().toString().trim(), edt_password.getText().toString())
                    .addOnSuccessListener(runnable -> {
                        this.userID = Objects.requireNonNull(runnable.getUser()).getUid();
                        Log.d("CREATE_UPDATES", "userID: " + userID);
                        User user = new User();

                        user.setUser_Name(edt_username.getText().toString());
                        user.setUser_Id(userID);
                        user.setUser_Email(edt_email.getText().toString());
                        user.setUser_Mobile(edt_phone.getText().toString());
                        user.setLongitude("0");
                        user.setLatitude("0");
                        user.setUser_ContactList(new ArrayList<>());

                        FirebaseFirestore.getInstance().collection("Users")
                                .document(userID).set(user)
                                .addOnSuccessListener(runnable1 -> {

                                    HashMap<String, Object> login = new HashMap<>();
                                    login.put("Login_Username", user.getUser_Name());
                                    login.put("Login_Password", Hashing.sha512().hashString( edt_password.getText().toString(), StandardCharsets.UTF_8).toString());
                                    login.put("Login_Rank", "user");

                                    FirebaseFirestore.getInstance().collection("Login")
                                            .document(userID)
                                            .set(login)
                                            .addOnSuccessListener(unused -> {
                                                FirebaseFirestore.getInstance().collection("Login")
                                                        .document(userID)
                                                        .update("Login_ID", userID);
                                                setDP();
                                            });
                                })
                                .addOnFailureListener(runnable2 -> nextActivity());

                    }).addOnFailureListener(e -> {
                sweetAlertDialog.dismissWithAnimation();
                Toast.makeText(MainActivity.this, "Authentication failed. Try again later", Toast.LENGTH_SHORT).show();
                Log.d("CREATE_UPDATES: ", "Failed to create user because " + e.getMessage());
            });


        }
    }

    private void setDP() {
        Log.d("CREATE_UPDATES: ", "photo started to upload");
        sweetAlertDialog.setContentText(dialogContent2);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("public_dp");
        final StorageReference image_name = storageReference.child("image_public_dp");

        try {
            image_name.putFile(image_uri).addOnSuccessListener(taskSnapshot -> image_name.getDownloadUrl().addOnSuccessListener(uri ->
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(this.userID).update("user_profile_pic", uri.toString())
                            .addOnSuccessListener(unused -> {
                                Log.d("CREATE_UPDATES: ", "photo saved, registration complete");
                                Toast.makeText(MainActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                                sweetAlertDialog.dismiss();
                                nextActivity();

                            }).addOnFailureListener(runnable1 -> Log.d("CREATE_UPDATES: ", "photo failed to upload because " + runnable1.getMessage()))
            ));
        } catch (Exception e) {
            Log.d("CREATE_UPDATES: ", "failed to push file to firebase because " + e.getMessage());
            nextActivity();
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", userID);
        intent.putExtra("FROM", "sign");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void nextActivityifExists() {
        Intent intent = new Intent(MainActivity.this, DashboardProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", userID);
        intent.putExtra("FROM", "sign");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }



    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intentActivityResultLauncher.launch(intent);
    }


    private void backPress() {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }
}