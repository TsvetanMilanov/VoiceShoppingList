package com.telerik.academy.voiceshoppinglist.utilities.commands;

import android.app.Activity;
import android.content.Intent;
import android.speech.SpeechRecognizer;

import com.telerik.academy.voiceshoppinglist.ViewShoppingListActivity;
import com.telerik.academy.voiceshoppinglist.LoadSavedListActivity;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognizerFactory;

public final class MainMenuCommands {
    public static void navigateToAddNewShoppingListActivity(Activity activity) {
        Intent intent = new Intent(activity, ViewShoppingListActivity.class);

        // Need to stop the current speech recognizer because it will try to destroy it in the next
        // activity where the intent class is not the same and the will cause service not registered exception.
        SpeechRecognizer currentSpeechRecognizer = SpeechRecognizerFactory.getCurrentSpeechRecognizer();

        if (currentSpeechRecognizer != null) {
            currentSpeechRecognizer.destroy();
        }

        activity.startActivity(intent);
        activity.finish();
    }

    public static void navigateToLoadSavedListActivity(Activity activity) {
        Intent intent = new Intent(activity, LoadSavedListActivity.class);

        // Need to stop the current speech recognizer because it will try to destroy it in the next
        // activity where the intent class is not the same and the will cause service not registered exception.
        SpeechRecognizer currentSpeechRecognizer = SpeechRecognizerFactory.getCurrentSpeechRecognizer();

        if (currentSpeechRecognizer != null) {
            currentSpeechRecognizer.destroy();
        }

        activity.startActivity(intent);
        activity.finish();
    }

    public static void exitApplication(Activity activity) {
        activity.finish();
        System.exit(0);
    }
}
