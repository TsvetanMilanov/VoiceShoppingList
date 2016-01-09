package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.speech.SpeechRecognizer;

public final class SpeechRecognitionHandler {
    public static void startListening(Activity activity) {
        Intent intent = SpeechRecognizerFactory.createSpeechRecognitionIntent(activity);
        SpeechRecognizer speechRecognizer = SpeechRecognizerFactory.createSpeechRecognizer(activity, intent);

        speechRecognizer.startListening(intent);
    }

    public static void stopListening() {
        SpeechRecognizerFactory
                .getCurrentSpeechRecognizer()
                .destroy();
    }
}
