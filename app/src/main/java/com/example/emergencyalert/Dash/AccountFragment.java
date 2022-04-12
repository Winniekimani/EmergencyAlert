package com.example.emergencyalert.Dash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.emergencyalert.AddContactActivity;
import com.example.emergencyalert.EmergencyCentresActivity;
import com.example.emergencyalert.LogInActivity;
import com.example.emergencyalert.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.MessageFormat;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    TextView txt_num, username;
    private CircleImageView img_userDp;
    private MaterialButton btn_addCentre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        txt_num = view.findViewById(R.id.txt_num);
        img_userDp = view.findViewById(R.id.img_userDp);
        username = view.findViewById(R.id.username);
        btn_addCentre = view.findViewById(R.id.btn_addCentre);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (DashboardProfileActivity.currentUser.getUser_ContactList() != null){
                    txt_num.setText(MessageFormat.format("Contacts : {0}", DashboardProfileActivity.currentUser.getUser_ContactList().size()));
                    Picasso.get().load(DashboardProfileActivity.currentUser.getUser_profile_pic()).into(img_userDp);
                    username.setText(DashboardProfileActivity.currentUser.getUser_Name());
                }
            }
        }, 1000);



        btn_addCentre.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), EmergencyCentresActivity.class);
            intent.putExtra("currentUser", DashboardProfileActivity.currentUser);
            startActivity(intent);
        });


        txt_num.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AddContactActivity.class);
            intent.putExtra("selected_contacts", (Serializable) DashboardProfileActivity.currentUser.getUser_ContactList());
            intent.putExtra("FROM", "account");
            startActivity(intent);
        });

        view.findViewById(R.id.btn_Logout).setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LogInActivity.class));
            getActivity().finish();
        });



        return view;
    }
}