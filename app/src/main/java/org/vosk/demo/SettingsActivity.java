package org.vosk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void goMain(View v) {
        Intent intent = new Intent(this, VoskActivity.class);
        startActivity(intent);
    }

    public void startlanguage(View v) {
        Intent intent = new Intent(this, LanguageModelsActivity.class);
        startActivity(intent);
    }

    public void startspecial(View v) {
        Intent intent = new Intent(this, SpecialAbbilitiesActivity.class);
        startActivity(intent);
    }

    public void startcomm(View v) {
        Intent intent = new Intent(this, AdditionalCommandSettings.class);
        startActivity(intent);
    }
}