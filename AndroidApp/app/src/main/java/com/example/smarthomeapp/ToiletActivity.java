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

public class ToiletActivity extends AppCompatActivity {

    String nowtime;
    LocalDateTime localtime;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm");
    String temp_setvalue, humi_setvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference livingroomRef = myRef.child("toilet");

        Switch light = (Switch) findViewById(R.id.light);

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
            else {
                livingroomRef.child("light").child("status").setValue(status);
                if(status.equals("true")) {
                    livingroomRef.child("light").child("laston").setValue(nowtime);
                }
                else {
                    livingroomRef.child("light").child("lastoff").setValue(nowtime);
                }
            }
        }

        bundle = new Bundle();
        Intent toiletIntent = new Intent(this, ToiletActivity.class);
        toiletIntent.putExtras(bundle);

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