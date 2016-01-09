package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.telerik.academy.voiceshoppinglist.R;

import java.util.ArrayList;

public class SpeechListener implements RecognitionListener {
    private static final String TAG = SpeechListener.class.getSimpleName();
    private boolean isSpeechRecognizerAvailable;
    private Activity activity;
    private Intent intent;
    private SpeechRecognizer speechRecognizer;

    public SpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent) {
        this.activity = activity;
        this.intent = intent;
        this.speechRecognizer = speechRecognizer;
        this.isSpeechRecognizerAvailable = false;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "Ready for speech!");
        Toast.makeText(this.activity, "Speak now!!!", Toast.LENGTH_SHORT).show();
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
        Log.e(TAG, "Buffer received!");
    }

    @Override
    public void onEndOfSpeech() {
        Log.e(TAG, "onEndOfSpeech()");
        this.restartSpeechListener();
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "Error! " + error);

        // TODO: Finish the error handling.

        if (error != SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
            if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT ||
                    error == SpeechRecognizer.ERROR_NO_MATCH ||
                    error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) {
                this.isSpeechRecognizerAvailable = true;
            }

            this.restartSpeechListener();
        }
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults()");
        if (results != null) {
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            if (data != null) {
                TextView tvCommandResult = (TextView) this.activity.findViewById(R.id.tv_command_result);
                tvCommandResult.setText(data.get(0));
            }
        }

        this.isSpeechRecognizerAvailable = true;

        this.restartSpeechListener();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults()");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent()");
    }



    private void restartSpeechListener() {
        if (!this.isSpeechRecognizerAvailable) {
            return;
        }

        this.speechRecognizer.destroy();

        this.intent = SpeechRecognizerFactory.createSpeechRecognitionIntent(this.activity);
        this.speechRecognizer = SpeechRecognizerFactory.createSpeechRecognizer(this.activity, this.intent);
        this.speechRecognizer.startListening(this.intent);

        this.isSpeechRecognizerAvailable = false;
    }
}
