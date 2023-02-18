package org.nssae.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.vosk.demo.R;

public class LanguageModelsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_models);
        findViewById(R.id.settings).setOnClickListener(view -> changeActivity(SettingsActivity.class));
    }

    public void changeActivity(Class<?> c) {
        startActivity(new Intent(this, c));
    }
}