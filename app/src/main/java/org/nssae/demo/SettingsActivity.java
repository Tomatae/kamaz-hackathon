package org.nssae.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.vosk.demo.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.button_main_menu).setOnClickListener(view -> changeActivity(VoskActivity.class));
        findViewById(R.id.button_language_models).setOnClickListener(view -> changeActivity(LanguageModelsActivity.class));
        findViewById(R.id.button_special_abilities).setOnClickListener(view -> changeActivity(SpecialAbbilitiesActivity.class));
        findViewById(R.id.button_command_settings).setOnClickListener(view -> changeActivity(AdditionalCommandSettingsActivity.class));
    }

    public void changeActivity(Class<?> c) {
        startActivity(new Intent(this, c));
    }
}