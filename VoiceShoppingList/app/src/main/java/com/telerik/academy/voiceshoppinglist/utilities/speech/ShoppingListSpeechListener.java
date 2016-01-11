package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.StringsSimilarityCalculator;
import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListCommands;

import java.util.ArrayList;

public class ShoppingListSpeechListener extends BaseSpeechListener {

    public ShoppingListSpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent, Class intentClass) {
        super(activity, speechRecognizer, intent, intentClass);
        this.tag = ShoppingListSpeechListener.class.getSimpleName();
    }

    @Override
    protected boolean handleResults(ArrayList<String> data) {
        Log.d(this.tag, "Shopping List Speech Listener handle results.");
        TextView tvCommandResult = (TextView) this.activity.findViewById(R.id.tv_shopping_list_commands_result);

        for (String commandString : data) {
            EditText productNameContainer = (EditText) this.activity.findViewById(R.id.newProductNameInput);
            LinearLayout productsList = (LinearLayout) this.activity.findViewById(R.id.productsList);
            ScrollView mainScrollView = (ScrollView) this.activity.findViewById(R.id.mainScrollView);

            // All commands without parameters must be before the commands with parameters.
            if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.ADD_PRODUCT_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                ShoppingListCommands.addProduct(this.activity, productNameContainer, productsList, mainScrollView);
                return true;
            }

            // All commands with parameters must be after the commands without parameters.
            int indexOfLastWhitespace = commandString.lastIndexOf(' ');

            if (indexOfLastWhitespace < 0 || indexOfLastWhitespace >= commandString.length()) {
                continue;
            }

            String commandType = commandString.substring(0, indexOfLastWhitespace);
            String commandParameter = commandString.substring(indexOfLastWhitespace + 1);

            if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.SET_PRODUCT_NAME_COMMAND, commandType) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                ShoppingListCommands.setProductName(productNameContainer, commandParameter);
                return true;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.CHECK_PRODUCT_COMMAND, commandType) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {

                CheckBox checkBox = findProductsCheckboxByTag(commandParameter, Constants.CHECKBOX_TAG, productsList);

                if (checkBox == null) {
                    continue;
                }

                checkBox.setChecked(true);

                return true;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.UNCHECK_PRODUCT_COMMAND, commandType) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {

                CheckBox checkBox = findProductsCheckboxByTag(commandParameter, Constants.CHECKBOX_TAG, productsList);

                if (checkBox == null) {
                    continue;
                }

                checkBox.setChecked(false);

                return true;
            }
        }

        return true;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        TextView commandResultTextView = (TextView) this.activity.findViewById(R.id.tv_shopping_list_commands_result);
        commandResultTextView.setText(R.string.waiting_for_command_label);
        super.onReadyForSpeech(params);
    }

    @Override
    public void onEndOfSpeech() {
        TextView commandResultTextView = (TextView) this.activity.findViewById(R.id.tv_shopping_list_commands_result);
        commandResultTextView.setText(R.string.please_wait_label);
        super.onEndOfSpeech();
    }

    @Override
    protected SpeechRecognizer getSpeechRecognizer(Activity activity, Intent intent, Class intentClass) {
        return SpeechRecognizerFactory.createShoppingListSpeechRecognizer(this.activity, this.intent, this.intentClass);
    }

    private static CheckBox findProductsCheckboxByTag(String commandParameter, String tag, LinearLayout productsList) {
        int itemNumberInParent = 0;

        try {
            itemNumberInParent = Integer.parseInt(commandParameter);
        } catch (NumberFormatException e) {
            return null;
        }

        View currentProductContainer = productsList.getChildAt(itemNumberInParent - 1);

        CheckBox checkBox = (CheckBox) currentProductContainer.findViewWithTag(tag);

        return checkBox;
    }
}
