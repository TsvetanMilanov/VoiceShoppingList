package com.telerik.academy.voiceshoppinglist.utilities;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public final class SpeechRecognizerFactory {
    public static Intent createSpeechRecognitionIntent(Activity activity) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity.getApplication().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        return intent;
    }

    public static SpeechRecognizer createSpeechRecognizer(Activity activity, Intent intent) {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        SpeechListener listener = new SpeechListener(activity, speechRecognizer, intent);

        speechRecognizer.setRecognitionListener(listener);

        return speechRecognizer;
    }
}
