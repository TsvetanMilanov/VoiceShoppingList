package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class ShoppingListSpeechListener extends BaseSpeechListener {
    public ShoppingListSpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent) {
        super(activity, speechRecognizer, intent);
        this.tag = ShoppingListSpeechListener.class.getSimpleName();
    }

    @Override
    protected void handleResults(ArrayList<String> data) {
        Log.d(this.tag, "Shopping List Speech Listener handle results.");
    }
}
