package org.nssae.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.vosk.demo.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void mainActivity(View v) {
        Intent intent = new Intent(this, VoskActivity.class);
        startActivity(intent);
    }

    public void languageModelsActivity(View v) {
        Intent intent = new Intent(this, LanguageModelsActivity.class);
        startActivity(intent);
    }

    public void specialAbbilitiesActivity(View v) {
        Intent intent = new Intent(this, SpecialAbbilitiesActivity.class);
        startActivity(intent);
    }

    public void additionalCommandSettingsActivity(View v) {
        Intent intent = new Intent(this, AdditionalCommandSettingsActivity.class);
        startActivity(intent);
    }
}