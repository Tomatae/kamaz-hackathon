package org.vosk.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdditionalCommandSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);
    }
    public void gosett(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);


    }
}