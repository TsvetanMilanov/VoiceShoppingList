package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.RmsDBStateParams;

import java.util.ArrayList;

public abstract class BaseSpeechListener implements RecognitionListener {
    protected String tag = null;
    protected final Activity activity;
    protected Intent intent;
    protected SpeechRecognizer speechRecognizer;
    protected boolean isSpeechRecognizerAvailable;
    protected Class intentClass;
    protected TextView commandsResult;
    private final View micResultView;
    protected RmsDBStateParams rmsDBStateParams;

    public BaseSpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent, Class intentClass) {
        this.activity = activity;
        this.intent = intent;
        this.speechRecognizer = speechRecognizer;
        this.isSpeechRecognizerAvailable = false;
        this.tag = BaseSpeechListener.class.getSimpleName();
        this.intentClass = intentClass;

        ContentFrameLayout layoutContent = (ContentFrameLayout) activity.findViewById(android.R.id.content);
        Resources resources = activity.getResources();

        this.commandsResult = (TextView) layoutContent.findViewWithTag(resources.getString(R.string.commands_result_text_view_tag));
        this.micResultView = layoutContent.findViewWithTag(resources.getString(R.string.mic_result_tag));
        this.rmsDBStateParams = new RmsDBStateParams(layoutContent, this.micResultView);
        this.micResultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(tag, "Ready for speech!");


        if (this.commandsResult != null) {
            this.commandsResult.setText(R.string.waiting_for_command_label);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(tag, "Beginning of speech!");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.e("RMS", "onRmsChanged: " + rmsdB);
        if (this.micResultView != null) {
            if (rmsdB < 0) {
                rmsdB = 1;
            }

            this.micResultView.setLayoutParams(this.rmsDBStateParams.getLayoutParams(rmsdB));
        }
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.e(tag, "Buffer received!");
    }

    @Override
    public void onEndOfSpeech() {
        Log.e(tag, "onEndOfSpeech()");

        if (this.commandsResult != null) {
            this.commandsResult.setText(R.string.please_wait_label);
        }

        this.restartSpeechListener();
    }

    @Override
    public void onError(int error) {
        Log.e(tag, "Error! " + error);

        switch (error) {
            case SpeechRecognizer.ERROR_SERVER:
                AlertDialogFactory.createInformationAlertDialog(this.activity, "There is something wrong with Google's server. Please try again later.", null, null).show();
                this.commandsResult.setVisibility(View.INVISIBLE);
                this.micResultView.setVisibility(View.INVISIBLE);
                return;
            case SpeechRecognizer.ERROR_CLIENT:
                AlertDialogFactory.createInformationAlertDialog(this.activity, "There is something wrong with your device. Please try again later.", null, null).show();
                this.commandsResult.setVisibility(View.INVISIBLE);
                this.micResultView.setVisibility(View.INVISIBLE);
                return;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                AlertDialogFactory.createInformationAlertDialog(this.activity, "You should allow access to Microphone and Audio record on your device for this application to recognize your speech.", null, null).show();
                this.commandsResult.setVisibility(View.INVISIBLE);
                this.micResultView.setVisibility(View.INVISIBLE);
                return;
            case SpeechRecognizer.ERROR_NETWORK:
                AlertDialogFactory.createInformationAlertDialog(this.activity, "There is no internet connection. Please try again later.", null, null).show();
                this.commandsResult.setVisibility(View.INVISIBLE);
                this.micResultView.setVisibility(View.INVISIBLE);
                return;
        }

        if (this.commandsResult != null) {
            this.commandsResult.setText(R.string.please_wait_label);
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
