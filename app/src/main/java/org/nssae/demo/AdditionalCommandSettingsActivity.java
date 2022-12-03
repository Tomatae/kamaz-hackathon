package org.nssae.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import org.vosk.demo.R;

public class AdditionalCommandSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

        Button button = (Button) findViewById(R.id.apply);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addUsersCommand();
            }
        });
    }

    public void settingsActivity(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void addUsersCommand() {
        EditText activityId = (EditText)findViewById(R.id.activityId);
        EditText userString = (EditText)findViewById(R.id.usersString);
        Switch hasGesture = (Switch)findViewById(R.id.hasGesture);
        Switch hasAudio = (Switch)findViewById(R.id.hasAudio);
        CheckBox finger1 = (CheckBox)findViewById(R.id.finger1);
        CheckBox finger2 = (CheckBox)findViewById(R.id.finger2);
        CheckBox finger3 = (CheckBox)findViewById(R.id.finger3);
        CheckBox finger4 = (CheckBox)findViewById(R.id.finger4);
        CheckBox finger5 = (CheckBox)findViewById(R.id.finger5);

        if (!hasAudio.isChecked() && !hasGesture.isChecked()) return;

        if (hasAudio.isChecked()) {
            if (activityId.getText().length() == 0) return;
            int id = Integer.parseInt(activityId.getText().toString());
            //TODO Проверить есть ли в списке
            if (userString.getText().length() < 3) return;
            String body = userString.getText().toString();
            //TODO Проверить не существует ли уже такой строки
        }

        //TODO Добавить в списки, если всё норм

        if (hasGesture.isChecked()) {
            int gestureValue = 0;
            if (finger1.isChecked()) gestureValue += 16;
            if (finger2.isChecked()) gestureValue += 8;
            if (finger3.isChecked()) gestureValue += 4;
            if (finger4.isChecked()) gestureValue += 2;
            if (finger5.isChecked()) gestureValue += 1;
        }
        //TODO Проверить не существует ли уже такой жест


        //TODO Добавить в списки, если всё норм

    }
}