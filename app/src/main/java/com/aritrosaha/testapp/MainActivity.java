package com.aritrosaha.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
// HELLO
public class MainActivity extends AppCompatActivity {
    Button sayButton;
    EditText ttsContentField;

    EditText sttText;
    Button speechButton;

    SpeechRecognizer mySpeechRecognizer;
    TextToSpeech textToSpeech;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sayButton = findViewById(R.id.sayButton);
        ttsContentField = findViewById(R.id.ttsContent);
        sttText = findViewById(R.id.sttText);
        speechButton = findViewById(R.id.sttButton);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.CANADA);
                }
            }
        });

        mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d("STT", "Ready!");
            }

            @Override
            public void onBeginningOfSpeech() {
                sttText.setText("");
                sttText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                Log.d("STT", "Error!!");
            }

            @Override
            public void onResults(Bundle bundle) {
                Toast.makeText(getApplicationContext(), "Got results...", Toast.LENGTH_SHORT).show();
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                sttText.setText(data.get(0));
                Log.d("STT", data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        speechButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                mySpeechRecognizer.stopListening();
                Log.d("STT", "Stopping recording...");
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                mySpeechRecognizer.startListening(speechRecognizerIntent);
                Log.d("STT", "Recording...");
            }
            return false;
        });

        sayButton.setOnClickListener(view -> {
            String myText = ttsContentField.getText().toString();
            Log.d("TTS", myText);
            Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_LONG).show();

            textToSpeech.speak(myText, TextToSpeech.QUEUE_ADD, null, "asdf");
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

}