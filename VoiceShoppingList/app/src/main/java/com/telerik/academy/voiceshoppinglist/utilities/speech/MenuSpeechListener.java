package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.widget.EditText;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.utilities.Commands;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.StringsSimilarityCalculator;

import java.util.ArrayList;

public class MenuSpeechListener extends BaseSpeechListener {
    public MenuSpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent) {
        super(activity, speechRecognizer, intent);
        this.tag = MenuSpeechListener.class.getSimpleName();
    }

    @Override
    protected void handleResults(ArrayList<String> data) {
        TextView tvCommandResult = (TextView) this.activity.findViewById(R.id.tv_command_result);
        tvCommandResult.setText(data.get(0));

        for (String commandString : data) {
            if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.ADD_SHOPPING_LIST_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                Commands.navigateToAddNewShoppingListActivity(this.activity);
                return;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.STOP_LISTENING_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                this.speechRecognizer.destroy();
                // Need to set speechRecognizer to null because the destroy() method doesn't work here.
                this.speechRecognizer = null;
                return;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.EXIT_APPLICATION_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                Commands.exitApplication(this.activity);
                return;
            }
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        TextView commandResultTextView = (TextView) this.activity.findViewById(R.id.tv_command_result);
        commandResultTextView.setText(R.string.waiting_for_command_label);
        super.onReadyForSpeech(params);
    }

    @Override
    public void onEndOfSpeech() {
        TextView commandResultTextView = (TextView) this.activity.findViewById(R.id.tv_command_result);
        commandResultTextView.setText(R.string.please_wait_label);
        super.onEndOfSpeech();
    }
}
