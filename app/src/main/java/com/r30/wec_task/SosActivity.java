package com.r30.wec_task;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SosActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    String emergencyContactNo;
    TextView eCTV;
    FloatingActionButton fab;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor myEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_sos2);
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS}, REQUEST_LOCATION);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedpreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        myEditor = sharedpreferences.edit();
        //Get Emergency Contact from DataBase
        eCTV= findViewById(R.id.emNo);
        fab = findViewById(R.id.settingsfab);
        userRef.child("eContact").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emergencyContactNo = snapshot.getValue(String.class).toString();
                Toast.makeText(SosActivity.this,"Emergency contact is"+emergencyContactNo,Toast.LENGTH_SHORT);
                myEditor.putString("eContact",emergencyContactNo);
                eCTV.setText(emergencyContactNo);
               /* for(DataSnapshot contacts : snapshot.getChildren()){
                    if(!contacts.getValue(String.class).isEmpty())
                    myEditor.putString(contacts.getKey(),contacts.getValue(String.class));
                }
                myEditor.apply();*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SosActivity.this,"Error getting your emergency contact",Toast.LENGTH_SHORT);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        ImageButton sosBtn = findViewById(R.id.sosButton);
        //Button Click sends SOS signal + GPS Co-ordinates
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(SosActivity.this, SettingsActivity.class);
                startActivity(I);
            }
        });
        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Location of user
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    sendLocation();
                }
            }
        });
    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    String ans;
    private void sendLocation() {
        if (ActivityCompat.checkSelfPermission(
                SosActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SosActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                ans=location.getLatitude()+","+location.getLongitude();
                                Intent intent=new Intent(SosActivity.this,SosActivity.class);
                                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
                                //Get the SmsManager instance and call the sendTextMessage method to send message
                                SmsManager sms=SmsManager.getDefault();
                                //if(emergencyContactNo.isEmpty()){
                                    emergencyContactNo= sharedpreferences.getString("eContact","9632601656");

                                sms.sendTextMessage(emergencyContactNo, null, "SOS my location is http://maps.google.com/maps?q="+ans, pi,null);
                                Toast.makeText(getApplicationContext(), "Message Sent successfully!"+emergencyContactNo,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}