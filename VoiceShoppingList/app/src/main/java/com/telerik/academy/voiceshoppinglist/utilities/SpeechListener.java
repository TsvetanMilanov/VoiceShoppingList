package com.telerik.academy.voiceshoppinglist.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SpeechListener implements RecognitionListener {
    private static final String TAG = SpeechListener.class.getSimpleName();
    private Context context;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;

    public SpeechListener(Context context, SpeechRecognizer speechRecognizer, Intent intent) {
        this.context = context;
        this.speechRecognizer = speechRecognizer;
        this.intent = intent;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "Ready for speech!");
        Toast.makeText(this.context, "Speak now!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "Beginning of speech!");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "Buffer received!");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "End of speech!");
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "Error!");
        Log.e(TAG, "" + error);

        if (error != SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
            this.speechRecognizer.stopListening();
            this.speechRecognizer.startListening(intent);
        }
    }

    @Override
    public void onResults(Bundle results) {
        if (results != null) {
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            if (data != null) {
                // TODO: Check for commands match.
                // TODO: Check how to continue to listen for commands.
                Toast.makeText(this.context, data.get(0), Toast.LENGTH_LONG).show();
                this.speechRecognizer.stopListening();
                this.speechRecognizer.startListening(this.intent);
            } else {
                Toast.makeText(this.context, "Data is null...", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.context, "Results are null...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "Partial results");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "" + eventType);
    }
}
