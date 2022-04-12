package com.example.emergencyalert;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyalert.Dash.DashboardProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

public class RelationshipsActivity extends AppCompatActivity {

    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationships);

        contactList = (List<Contact>) getIntent().getExtras().getSerializable("selected_contacts");
        RecyclerView relationship_rc = findViewById(R.id.relationship_rc);

        relationship_rc.setAdapter(new ContactAdapter());
        relationship_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        findViewById(R.id.btn_finish).setOnClickListener(view -> finishRegistration());


    }

    private void finishRegistration() {
        for (Contact contact: contactList) {
            if (contact.getUser_Emergency_Contact_Id() == null || contact.getUser_Emergency_Contact_Id().isEmpty()){
                FirebaseFirestore.getInstance().collection("User_Emergency_Contact")
                        .add(contact)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                FirebaseFirestore.getInstance().collection("User_Emergency_Contact")
                                        .document(documentReference.getId())
                                        .update("user_Emergency_Contact_Id", documentReference.getId());
                            }
                        });
            }
        }
        startActivity(new Intent(RelationshipsActivity.this, DashboardProfileActivity.class));
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

        @NonNull
        @Override
        public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContactViewHolder(LayoutInflater.from(RelationshipsActivity.this).inflate(R.layout.relation_ship_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {

            holder.name_txt.setText(MessageFormat.format("What is your relationship to {0}", contactList.get(position).getUser_Emergency_Contact_Name()));
            try {
                holder.edt_rlship.setText(contactList.get(position).getUser_Emergency_Contact_User_Relationship());
            }catch (Exception e){
                Log.d("ContactException", e.getLocalizedMessage());
            }

            final int pos = position;

            holder.edt_rlship.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    contactList.get(pos).setUser_Emergency_Contact_User_Relationship(Objects.requireNonNull(holder.edt_rlship.getText()).toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder{

            TextView name_txt;
            TextInputEditText edt_rlship;

            public ContactViewHolder(@NonNull View itemView) {
                super(itemView);

                name_txt = itemView.findViewById(R.id.name_txt);
                edt_rlship = itemView.findViewById(R.id.edt_rlship);



            }
        }
    }
}