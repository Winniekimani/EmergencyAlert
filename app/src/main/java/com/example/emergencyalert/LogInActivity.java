package com.example.emergencyalert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencyalert.Dash.DashboardProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText edt_email, edt_password;
    private TextView txt_error;
    private SweetAlertDialog sweetAlertDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        txt_error = findViewById(R.id.txt_error);

        auth = FirebaseAuth.getInstance();


        findViewById(R.id.btn_LogIn).setOnClickListener(view -> logInUser());
    }

    private void logInUser() {
        if (TextUtils.isEmpty(Objects.requireNonNull(edt_email.getText()).toString()))
            edt_email.setError(this.getString(R.string.input_error));
        else if (TextUtils.isEmpty(Objects.requireNonNull(edt_password.getText()).toString()))
            edt_password.setError(this.getString(R.string.input_error));
        else {

            sweetAlertDialog = new SweetAlertDialog(LogInActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setTitleText("Logging in");
            sweetAlertDialog.setContentText("Still logging in");
            sweetAlertDialog.show();

            auth.signInWithEmailAndPassword(edt_email.getText().toString().trim(), edt_password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            nextActivity();
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).addOnFailureListener(e -> {

                        txt_error.setText(e.getLocalizedMessage());
                        txt_error.setVisibility(View.VISIBLE);
                        sweetAlertDialog.dismissWithAnimation();
                    });
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(LogInActivity.this, DashboardProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}