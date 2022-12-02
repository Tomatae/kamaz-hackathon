package org.vosk.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
    public void goMain(View v){
        Intent intent = new Intent(this, VoskActivity.class);
        startActivity(intent);


    }
    public void startlanguage(View v) {
        Intent intent = new Intent(this, language_modals.class);
        startActivity(intent);
    }
    public void startspecial(View v) {
        Intent intent = new Intent(this, special.class);
        startActivity(intent);
    }
    public void startcomm(View v) {
        Intent intent = new Intent(this, comm.class);
        startActivity(intent);
    }
}