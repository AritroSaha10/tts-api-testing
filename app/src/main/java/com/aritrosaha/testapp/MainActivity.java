package com.aritrosaha.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button sayButton;
    EditText ttsContentField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sayButton = findViewById(R.id.sayButton);
        ttsContentField = findViewById(R.id.ttsContent);

        sayButton.setOnClickListener(view -> {
            String myText = ttsContentField.getText().toString();
            Log.d("TTS", myText);
            Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_LONG).show();

            // TODO: Add functionality to use TTS
        });
    }
}