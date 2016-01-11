package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.widget.TextView;
import android.widget.Toast;

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.utilities.commands.MainMenuCommands;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.StringsSimilarityCalculator;

import java.util.ArrayList;

public class MenuSpeechListener extends BaseSpeechListener {
    public MenuSpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent, Class intentClass) {
        super(activity, speechRecognizer, intent, intentClass);
        this.tag = MenuSpeechListener.class.getSimpleName();
    }

    @Override
    protected boolean handleResults(ArrayList<String> data) {
        TextView tvCommandResult = (TextView) this.activity.findViewById(R.id.tv_command_result);

        for (String commandString : data) {
            if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.ADD_SHOPPING_LIST_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                MainMenuCommands.navigateToAddNewShoppingListActivity(this.activity);
                tvCommandResult.setText(Constants.ADD_SHOPPING_LIST_COMMAND);
                return false;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.STOP_LISTENING_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                this.speechRecognizer.destroy();
                // Need to set speechRecognizer to null because the destroy() method doesn't work here.
                this.speechRecognizer = null;
                SpeechRecognitionHandler.stopListening();
                tvCommandResult.setText(Constants.STOP_LISTENING_COMMAND);
                return false;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.EXIT_APPLICATION_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                MainMenuCommands.exitApplication(this.activity);
                tvCommandResult.setText(Constants.EXIT_APPLICATION_COMMAND);
                return false;
            }
        }

        return true;
    }

    @Override
    protected SpeechRecognizer getSpeechRecognizer(Activity activity, Intent intent, Class intentClass) {
        return SpeechRecognizerFactory.createMenuSpeechRecognizer(this.activity, this.intent, this.intentClass);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Toast.makeText(this.activity, "Speak now!!!", Toast.LENGTH_SHORT).show();
        TextView commandResultTextView = (TextView) this.activity.findViewById(R.id.tv_command_result);

        if (commandResultTextView != null) {
            commandResultTextView.setText(R.string.waiting_for_command_label);
        }

        super.onReadyForSpeech(params);
    }

    @Override
    public void onEndOfSpeech() {
        TextView commandResultTextView = (TextView) this.activity.findViewById(R.id.tv_command_result);
        if (commandResultTextView != null) {
            commandResultTextView.setText(R.string.please_wait_label);
        }

        super.onEndOfSpeech();
    }
}
