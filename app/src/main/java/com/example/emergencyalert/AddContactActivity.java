package com.example.emergencyalert;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddContactActivity extends AppCompatActivity {

    RecyclerView recyclerView, selected_rc;
    List<Contact> contactList;
    List<Contact> selected_contactList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        contactList = new ArrayList<>();

        getContacts();

        findViewById(R.id.btn_continue).setOnClickListener(view -> {
            if (selected_contactList.size() > 0){
                Intent intent = new Intent(AddContactActivity.this, RelationshipsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                intent.putExtras(bundle);
                intent.putExtra("selected_contacts", (Serializable) selected_contactList);

                startActivity(intent);
                finish();
            }

        });

        if (getIntent().getStringExtra("FROM").equals("account")){
            selected_contactList = (List<Contact>) getIntent().getExtras().getSerializable("selected_contacts");
        }else selected_contactList = new ArrayList<>();
        selected_rc = findViewById(R.id.selected_rc);
        selected_rc.setAdapter(new SelectedAdapter());
        selected_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getContacts() {
        try {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

            Cursor  cursor = contentResolver.query(uri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            Log.i("CONTACT_PROVIDER_DEMO", "Total count ::: " + cursor.getCount());
            if (cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Log.i("CONTACT_PROVIDER_DEMO", "Contact name ::: " + contactName + "            PHONE ::: " + number);
                    Contact contact = new Contact();
                    contact.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    contact.setName(contactName);
                    contact.setNumber(number);

                    contactList.add(contact);

                    recyclerView.setAdapter(new Adapter());

                }
            }
        }catch (Exception e){
            Toast.makeText(AddContactActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ContactViewHolder>{

        @NonNull
        @Override
        public Adapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContactViewHolder(LayoutInflater.from(AddContactActivity.this).inflate(R.layout.row_display_contacts, parent, false));
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBindViewHolder(@NonNull Adapter.ContactViewHolder holder, int position) {

            holder.txt_name.setText(contactList.get(position).getName());
            holder.txt_number.setText(contactList.get(position).getNumber());
            contactList.get(position).setRelationship(contactList.get(position).getRelationship());

            holder.itemView.setOnClickListener(view -> {
                if (selected_contactList.size() < 5){
                    selected_contactList.add(contactList.get(position));
                    contactList.remove(position);
                    selected_rc.getAdapter().notifyDataSetChanged();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }else
                    Toast.makeText(AddContactActivity.this, "Maximum reached", Toast.LENGTH_SHORT).show();

            });
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder{
            TextView txt_name, txt_number;
            public ContactViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_number = itemView.findViewById(R.id.txt_number);
                txt_name = itemView.findViewById(R.id.txt_name);

            }
        }
    }

    private class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.ContactSelectedViewHolder>{

        @NonNull
        @Override
        public SelectedAdapter.ContactSelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContactSelectedViewHolder(LayoutInflater.from(AddContactActivity.this).inflate(R.layout.selected_contact, parent, false));
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBindViewHolder(@NonNull SelectedAdapter.ContactSelectedViewHolder holder, int position) {

            holder.txt_name.setText(selected_contactList.get(position).getName());
            holder.txt_number.setText(selected_contactList.get(position).getNumber());
            holder.remove.setOnClickListener(view -> {
                //Remove the contact
                contactList.add(selected_contactList.get(position));
                selected_contactList.remove(position);
                selected_rc.getAdapter().notifyDataSetChanged();

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddContactActivity.this, SweetAlertDialog.PROGRESS_TYPE);;

                try {


                    sweetAlertDialog.setTitleText("Removing activity");
                    sweetAlertDialog.setContentText("Removing selected contact");
                    sweetAlertDialog.show();

                    FirebaseFirestore.getInstance().collection("User_Emergency_Contact")
                            .document(selected_contactList.get(position).getContact_id())
                            .delete()
                            .addOnSuccessListener(unused -> sweetAlertDialog.dismissWithAnimation());

                    recyclerView.getAdapter().notifyDataSetChanged();
                }catch (Exception exception){
                    Log.d("ContactException", exception.getLocalizedMessage());
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }



        @Override
        public int getItemCount() {
            return selected_contactList.size();
        }

        public class ContactSelectedViewHolder extends RecyclerView.ViewHolder{

            TextView  txt_name, txt_number;
            ImageView remove;

            public ContactSelectedViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_number = itemView.findViewById(R.id.txt_number);
                txt_name = itemView.findViewById(R.id.txt_name);
                remove = itemView.findViewById(R.id.remove);

            }
        }
    }


}