package org.vosk.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class language_modals extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_modals);
    }
    public void goset(View v){
        Intent intent = new Intent(this, setting.class);
        startActivity(intent);


    }
}