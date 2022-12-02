package org.vosk.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class setting extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    public void golanguage(View v) {
        Intent intent = new Intent(this, language_modals.class);
        startActivity(intent);
    }
    public void gospecial(View v) {
        Intent intent = new Intent(this, special.class);
        startActivity(intent);
    }
    public void gocomm(View v) {
        Intent intent = new Intent(this, comm.class);
        startActivity(intent);
    }
    public void backfirst(View v) {
        Intent intent = new Intent(this, VoskActivity.class);
        startActivity(intent);
    }
}