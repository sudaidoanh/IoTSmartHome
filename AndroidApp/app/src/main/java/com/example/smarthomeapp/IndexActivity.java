package com.example.smarthomeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IndexActivity extends AppCompatActivity {

    TextView textView;

    Intent livingRoomIntent;
    Intent bedroomIntent;
    Intent kitchenIntent;
    Intent toiletIntent;

    Bundle bundle;
    FirebaseDatabase database;
    DatabaseReference myRef, livingroomRef, bedroomRef, kitchenRef, toiletRef;
    String nowtime;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Button livingroom = (Button) findViewById(R.id.livingroom);
        Button bedroom = (Button) findViewById(R.id.bedroom);
        Button kitchen = (Button) findViewById(R.id.kitchen);
        Button toilet = (Button) findViewById(R.id.toilet);

        textView = findViewById(R.id.textView);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        livingroomRef = myRef.child("livingroom");
        bedroomRef = myRef.child("bedroom");
        kitchenRef = myRef.child("kitchen");
        toiletRef = myRef.child("toilet");

        livingRoomIntent = new Intent(this, LivingRoomActivity.class);
        bedroomIntent = new Intent(this, BedroomActivity.class);
        kitchenIntent = new Intent(this, KitchenActivity.class);
        toiletIntent = new Intent(this, ToiletActivity.class);

        livingroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(livingRoomIntent);
            }
        });

        bedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(bedroomIntent);
            }
        });

        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(kitchenIntent);
            }
        });

        toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toiletIntent);
            }
        });
    }

    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK) {
            textView.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            String command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);

            String status, device, room;
            status = command.contains("on")? "true" : command.contains("open")? "true" : "false";
            device = command.contains("door")? "door": command.contains("light")? "light" : command.contains("fan") ? "fan" : command.contains("all devices")? "all devices" : "unrecognizable";
            room = command.contains("living room")? "livingroom" : command.contains("bedroom")? "bedroom" : command.contains("kitchen")? "kitchen" : command.contains("toilet")? "toilet" : "allrooms";
            bundle = new Bundle();
            bundle.putString("device", device);
            bundle.putString("status", status);
            if (device.equals("door")) {
                myRef.child("door").child("status").setValue(status);
            } else if(room.equals("livingroom")) {
                livingRoomIntent.putExtras(bundle);
                startActivity(livingRoomIntent);
            } else if(room.equals("bedroom")) {
                bedroomIntent.putExtras(bundle);
                startActivity(bedroomIntent);
            } else if(room.equals("kitchen")) {
                kitchenIntent.putExtras(bundle);
                startActivity(kitchenIntent);
            } else if(room.equals("toilet")) {
                toiletIntent.putExtras(bundle);
                startActivity(toiletIntent);
            } else {
                nowtime = LocalDateTime.now().format(dateTimeFormatter);
                livingroomRef.child("light").child("status").setValue(status);
                livingroomRef.child("fan").child("status").setValue(status);
                bedroomRef.child("light").child("status").setValue(status);
                bedroomRef.child("fan").child("status").setValue(status);
                kitchenRef.child("light").child("status").setValue(status);
                kitchenRef.child("fan").child("status").setValue(status);
                toiletRef.child("light").child("status").setValue(status);
                if (status.equals("true")) {
                    livingroomRef.child("light").child("laston").setValue(nowtime);
                    livingroomRef.child("fan").child("laston").setValue(nowtime);
                    bedroomRef.child("light").child("laston").setValue(nowtime);
                    bedroomRef.child("fan").child("laston").setValue(nowtime);
                    kitchenRef.child("light").child("laston").setValue(nowtime);
                    kitchenRef.child("fan").child("laston").setValue(nowtime);
                    toiletRef.child("light").child("laston").setValue(nowtime);
                } else {
                    livingroomRef.child("light").child("lastoff").setValue(nowtime);
                    livingroomRef.child("fan").child("lastoff").setValue(nowtime);
                    bedroomRef.child("light").child("lastoff").setValue(nowtime);
                    bedroomRef.child("fan").child("lastoff").setValue(nowtime);
                    kitchenRef.child("light").child("lastoff").setValue(nowtime);
                    kitchenRef.child("fan").child("lastoff").setValue(nowtime);
                    toiletRef.child("light").child("lastoff").setValue(nowtime);
                }
            }
        }

        data = null;
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
}