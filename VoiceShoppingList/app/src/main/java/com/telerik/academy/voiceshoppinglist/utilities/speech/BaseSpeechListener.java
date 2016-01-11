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

public abstract class BaseSpeechListener implements RecognitionListener {
    protected String tag = null;
    protected final Activity activity;
    protected Intent intent;
    protected SpeechRecognizer speechRecognizer;
    protected boolean isSpeechRecognizerAvailable;
    protected Class intentClass;
    protected int resultTextViewId;

    public BaseSpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent, Class intentClass, int resultTextViewId) {
        this.activity = activity;
        this.intent = intent;
        this.speechRecognizer = speechRecognizer;
        this.isSpeechRecognizerAvailable = false;
        this.tag = BaseSpeechListener.class.getSimpleName();
        this.intentClass = intentClass;
        this.resultTextViewId = resultTextViewId;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(tag, "Ready for speech!");

        TextView commandResultTextView = (TextView) this.activity.findViewById(this.resultTextViewId);

        if (commandResultTextView != null) {
            commandResultTextView.setText(R.string.waiting_for_command_label);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(tag, "Beginning of speech!");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.e(tag, "Buffer received!");
    }

    @Override
    public void onEndOfSpeech() {
        Log.e(tag, "onEndOfSpeech()");

        TextView commandResultTextView = (TextView) this.activity.findViewById(this.resultTextViewId);
        commandResultTextView.setText(R.string.please_wait_label);

        this.restartSpeechListener();
    }

    @Override
    public void onError(int error) {
        Log.e(tag, "Error! " + error);

        // TODO: Finish the error handling.

        TextView commandResultTextView = (TextView) this.activity.findViewById(this.resultTextViewId);

        if (commandResultTextView != null) {
            commandResultTextView.setText(R.string.please_wait_label);
        }

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
        Log.d(tag, "onResults()");
        if (results != null) {
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            if (data != null) {
                boolean continueListening = this.handleResults(data);

                if (!continueListening) {
                    return;
                }
            }
        }

        this.isSpeechRecognizerAvailable = true;

        this.restartSpeechListener();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(tag, "onPartialResults()");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(tag, "onEvent()");
    }

    protected void restartSpeechListener() {
        if (!this.isSpeechRecognizerAvailable) {
            return;
        }

        // This check is needed because in child speech listener when need to stop listening
        // the destroy() method doesn't work.
        if (this.speechRecognizer == null) {
            return;
        }

        if (!SpeechRecognizer.isRecognitionAvailable(this.activity)) {
            return;
        }

        this.speechRecognizer.destroy();

        this.intent = SpeechRecognizerFactory.createSpeechRecognitionIntent(this.activity, this.intentClass);
        this.speechRecognizer = this.getSpeechRecognizer(this.activity, this.intent, this.intentClass);
        this.speechRecognizer.startListening(this.intent);

        this.isSpeechRecognizerAvailable = false;
    }

    protected abstract boolean handleResults(ArrayList<String> data);

    protected abstract SpeechRecognizer getSpeechRecognizer(Activity activity, Intent intent, Class intentClass);
}
