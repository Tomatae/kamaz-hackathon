package org.nssae.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.vosk.demo.R;

public class SpecialAbbilitiesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        findViewById(R.id.button_settings).setOnClickListener(view -> changeActivity(SettingsActivity.class));
    }

    private void changeActivity(Class<?> c) {
        startActivity(new Intent(this, c));
    }
}