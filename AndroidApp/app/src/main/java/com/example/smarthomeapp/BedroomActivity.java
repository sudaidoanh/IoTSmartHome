package com.example.smarthomeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BedroomActivity extends AppCompatActivity {

    String nowtime;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm");
    String temp_setvalue, humi_setvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference bedroomRef = myRef.child("bedroom");

        TextView temp_value = (TextView) findViewById(R.id.temp_value);
        TextView humi_value = (TextView) findViewById(R.id.humi_value);
        Switch light = (Switch) findViewById(R.id.light);
        Switch fan = (Switch) findViewById(R.id.fan);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            String status = bundle.getString("status");
            String device = bundle.getString("device");
            nowtime = LocalDateTime.now().format(dateTimeFormatter);
            if(device.equals("light")) {
                bedroomRef.child("light").child("status").setValue(status);
                if(status.equals("true")) {
                    bedroomRef.child("light").child("laston").setValue(nowtime);
                }
                else bedroomRef.child("light").child("lastoff").setValue(nowtime);
            }
            else if(device.equals("fan")) {
                bedroomRef.child("fan").child("status").setValue(status);
                if(status.equals("true")) {
                    bedroomRef.child("fan").child("laston").setValue(nowtime);
                }
                else bedroomRef.child("fan").child("lastoff").setValue(nowtime);
            }
            else {
                bedroomRef.child("light").child("status").setValue(status);
                bedroomRef.child("fan").child("status").setValue(status);
                if(status.equals("true")) {
                    bedroomRef.child("light").child("laston").setValue(nowtime);
                    bedroomRef.child("fan").child("laston").setValue(nowtime);
                }
                else {
                    bedroomRef.child("light").child("lastoff").setValue(nowtime);
                    bedroomRef.child("fan").child("lastoff").setValue(nowtime);
                }
            }
        }

        bundle = new Bundle();
        Intent bedroomIntent = new Intent(this, BedroomActivity.class);
        bedroomIntent.putExtras(bundle);

        bedroomRef.child("current").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    temp_setvalue = snapshot.child("temperature").getValue().toString();
                    humi_setvalue = snapshot.child("humidity").getValue().toString();
                    temp_value.setText(temp_setvalue + " °C");
                    humi_value.setText(humi_setvalue + " %");
                }
                else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bedroomRef.child("light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.child("status").getValue().toString().equals("true")) {
                        light.setChecked(true);
                    }
                    else {
                        light.setChecked(false);
                    }
                }
                else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bedroomRef.child("fan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.child("status").getValue().toString().equals("true")) {
                        fan.setChecked(true);
                    }
                    else {
                        fan.setChecked(false);
                    }
                }
                else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(light.isChecked()) {
                    bedroomRef.child("light").child("status").setValue("true");
                    nowtime = LocalDateTime.now().format(dateTimeFormatter);
                    bedroomRef.child("light").child("laston").setValue(nowtime);
                }
                else {
                    bedroomRef.child("light").child("status").setValue("false");
                    nowtime = LocalDateTime.now().format(dateTimeFormatter);
                    bedroomRef.child("light").child("lastoff").setValue(nowtime);
                }
            }
        });

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(fan.isChecked()) {
                    bedroomRef.child("fan").child("status").setValue("true");
                    nowtime = LocalDateTime.now().format(dateTimeFormatter);
                    bedroomRef.child("fan").child("laston").setValue(nowtime);
                }
                else {
                    bedroomRef.child("fan").child("status").setValue("false");
                    nowtime = LocalDateTime.now().format(dateTimeFormatter);
                    bedroomRef.child("fan").child("lastoff").setValue(nowtime);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}