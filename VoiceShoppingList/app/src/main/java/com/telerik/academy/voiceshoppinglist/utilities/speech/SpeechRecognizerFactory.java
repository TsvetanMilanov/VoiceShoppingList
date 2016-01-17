package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.telerik.academy.voiceshoppinglist.MainActivityFragment;
import com.telerik.academy.voiceshoppinglist.R;

public final class SpeechRecognizerFactory {
    private static SpeechRecognizer speechRecognizer;

    public static Intent createSpeechRecognitionIntent(Activity activity, Class intentClass) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, null, activity, intentClass);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity.getApplication().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

        return intent;
    }

    /**
     * Creates speech recognizer with custom intent.
     *
     * @param activity The context for the speech recognizer.
     * @param intent   The intent which starts the speech recognizer.
     * @return Returns new speech recognizer created with custom intent.
     */
    public static SpeechRecognizer createMenuSpeechRecognizer(Activity activity, Intent intent, Class intentClass) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        MenuSpeechListener listener = new MenuSpeechListener(activity, speechRecognizer, intent, intentClass);

        speechRecognizer.setRecognitionListener(listener);

        return speechRecognizer;
    }

    /**
     * Creates speech recognizer with custom intent.
     *
     * @param activity The context for the speech recognizer.
     * @param intent   The intent which starts the speech recognizer.
     * @return Returns new speech recognizer created with custom intent.
     */
    public static SpeechRecognizer createShoppingListSpeechRecognizer(Activity activity, Intent intent, Class intentClass) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        ShoppingListSpeechListener listener = new ShoppingListSpeechListener(activity, speechRecognizer, intent, intentClass);

        speechRecognizer.setRecognitionListener(listener);

        return speechRecognizer;
    }

    public static SpeechRecognizer getCurrentSpeechRecognizer() {
        return speechRecognizer;
    }
}
