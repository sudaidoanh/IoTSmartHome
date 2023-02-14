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

public class KitchenActivity extends AppCompatActivity {

    String nowtime;
    LocalDateTime localtime;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm");
    String temp_setvalue, humi_setvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference livingroomRef = myRef.child("kitchen");

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
                livingroomRef.child("light").child("status").setValue(status);
                if(status.equals("true")) {
                    livingroomRef.child("light").child("laston").setValue(nowtime);
                }
                else livingroomRef.child("light").child("lastoff").setValue(nowtime);
            }
            else if(device.equals("fan")) {
                livingroomRef.child("fan").child("status").setValue(status);
                if(status.equals("true")) {
                    livingroomRef.child("fan").child("laston").setValue(nowtime);
                }
                else livingroomRef.child("fan").child("lastoff").setValue(nowtime);
            }
            else {
                livingroomRef.child("light").child("status").setValue(status);
                livingroomRef.child("fan").child("status").setValue(status);
                if(status.equals("true")) {
                    livingroomRef.child("light").child("laston").setValue(nowtime);
                    livingroomRef.child("fan").child("laston").setValue(nowtime);
                }
                else {
                    livingroomRef.child("light").child("lastoff").setValue(nowtime);
                    livingroomRef.child("fan").child("lastoff").setValue(nowtime);
                }
            }
        }

        bundle = new Bundle();
        Intent kitchenIntent = new Intent(this, KitchenActivity.class);
        kitchenIntent.putExtras(bundle);

        livingroomRef.child("current").addValueEventListener(new ValueEventListener() {
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

        livingroomRef.child("light").addValueEventListener(new ValueEventListener() {
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

        livingroomRef.child("fan").addValueEventListener(new ValueEventListener() {
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
                    livingroomRef.child("light").child("status").setValue("true");
                    localtime = LocalDateTime.now();
                    nowtime = localtime.format(dateTimeFormatter);
                    livingroomRef.child("light").child("laston").setValue(nowtime);
                }
                else {
                    livingroomRef.child("light").child("status").setValue("false");
                    localtime = LocalDateTime.now();
                    nowtime = localtime.format(dateTimeFormatter);
                    livingroomRef.child("light").child("lastoff").setValue(nowtime);
                }
            }
        });

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(fan.isChecked()) {
                    livingroomRef.child("fan").child("status").setValue("true");
                    localtime = LocalDateTime.now();
                    nowtime = localtime.format(dateTimeFormatter);
                    livingroomRef.child("fan").child("laston").setValue(nowtime);
                }
                else {
                    livingroomRef.child("fan").child("status").setValue("false");
                    localtime = LocalDateTime.now();
                    nowtime = localtime.format(dateTimeFormatter);
                    livingroomRef.child("fan").child("lastoff").setValue(nowtime);
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