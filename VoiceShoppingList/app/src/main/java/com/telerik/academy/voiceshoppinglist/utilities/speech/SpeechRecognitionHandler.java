package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.speech.SpeechRecognizer;

public final class SpeechRecognitionHandler {
    public static void startListeningForMenuCommands(Activity activity, Class intentClass) {
        Intent intent = SpeechRecognizerFactory.createSpeechRecognitionIntent(activity, intentClass);
        SpeechRecognizer speechRecognizer = SpeechRecognizerFactory.createMenuSpeechRecognizer(activity, intent, intentClass);

        speechRecognizer.startListening(intent);
    }

    public static void startListeningForShoppingListCommands(Activity activity, Class intentClass) {
        Intent intent = SpeechRecognizerFactory.createSpeechRecognitionIntent(activity, intentClass);
        SpeechRecognizer speechRecognizer = SpeechRecognizerFactory.createShoppingListSpeechRecognizer(activity, intent, intentClass);

        speechRecognizer.startListening(intent);
    }

    public static void stopListening() {
        SpeechRecognizer currentSpeechRecognizer = SpeechRecognizerFactory
                .getCurrentSpeechRecognizer();

        if (currentSpeechRecognizer != null) {
            currentSpeechRecognizer.destroy();
        }
    }
}
