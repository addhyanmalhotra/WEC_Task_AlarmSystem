package com.r30.wec_task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {
    TextInputLayout tf0,tf1,tf2,tf3,tf4;
    TextInputEditText etf0,etf1,etf2,etf3,etf4;
    Button button,soBtn,resetBtn;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_settings);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("users").child(firebaseUser.getUid());
        tf0=findViewById(R.id.ePhoneTF1);
        tf1=findViewById(R.id.ePhoneTF2);
        tf2=findViewById(R.id.ePhoneTF3);
        tf3=findViewById(R.id.ePhoneTF4);
        tf4=findViewById(R.id.ePhoneTF5);
        //assigning ET's
        etf0=findViewById(R.id.EmergencyphoneNo1);
        etf1=findViewById(R.id.EmergencyphoneNo2);
        etf2=findViewById(R.id.EmergencyphoneNo3);
        etf3=findViewById(R.id.EmergencyphoneNo4);
        etf4=findViewById(R.id.EmergencyphoneNo5);
        //Button
        button=findViewById(R.id.upDateBtn);
        soBtn=findViewById(R.id.signOutBtn);
        resetBtn=findViewById(R.id.resetBtn);
        soBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(I);
                finish();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,"Dude i told you i didn't implement " +
                        "forgot password, text me on 9632601656 and ill send reset mail",Toast.LENGTH_LONG);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etf0.getText().toString().isEmpty()){
                userRef.child("eContact").setValue(etf0.getText().toString());
                }   else {
                tf0.setError("Dude Primary contact is Empty");
                }
            }
        });
        tf0.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 0);
            }
        });
        tf1.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 1);
            }
        });
        tf2.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 2);
            }
        });
        tf3.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 3);
            }
        });
        tf4.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 4);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    setContact(etf0,data);
                    break;
                case 1:
                    setContact(etf1,data);
                    break;
                case 2:
                    setContact(etf2,data);
                    break;
                case 3:
                    setContact(etf2,data);
                    break;
                case 4:
                    setContact(etf2,data);
                    break;
            }
        }
    }
    public void setContact(TextInputEditText ePhone,@Nullable Intent data){
        Cursor cursor = null;
        try {
            String contactNumber = null;
            String contactName = null;
            // getData() method will have the
            // Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            contactNumber = cursor.getString(phoneIndex);
            contactName = cursor.getString(nameIndex);
            // Set the value to the textviews
            ePhone.setText(contactNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}