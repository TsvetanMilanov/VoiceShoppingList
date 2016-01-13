package com.telerik.academy.voiceshoppinglist.utilities.speech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.telerik.academy.voiceshoppinglist.FinishShoppingListActivity;
import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.StringsSimilarityCalculator;
import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListCommands;

import java.util.ArrayList;

public class ShoppingListSpeechListener extends BaseSpeechListener {

    public ShoppingListSpeechListener(Activity activity, SpeechRecognizer speechRecognizer, Intent intent, Class intentClass, int resultTextViewId) {
        super(activity, speechRecognizer, intent, intentClass, resultTextViewId);
        this.tag = ShoppingListSpeechListener.class.getSimpleName();
    }

    @Override
    protected boolean handleResults(ArrayList<String> data) {
        Log.d(this.tag, "Shopping List Speech Listener handle results.");

        for (String commandString : data) {
            EditText productNameContainer = (EditText) this.activity.findViewById(R.id.newProductNameInput);
            LinearLayout productsList = (LinearLayout) this.activity.findViewById(R.id.productsList);
            ScrollView mainScrollView = (ScrollView) this.activity.findViewById(R.id.mainScrollView);

            // All commands without parameters must be before the commands with parameters.
            if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.ADD_PRODUCT_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                ShoppingListCommands.addProduct(this.activity, productNameContainer, productsList, mainScrollView);
                return true;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.FINISH_SHOPPING_LIST_COMMAND, commandString) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {
                ArrayList<Product> products = getAllProductsFromProductsList(productsList);

                Intent intent = new Intent(this.activity, FinishShoppingListActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable(Constants.PRODUCTS_LIST_BUNDLE_KEY, products);

                intent.putExtras(bundle);

                this.activity.startActivity(intent);

                this.speechRecognizer.destroy();
                SpeechRecognitionHandler.stopListening();
                return false;
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
                ShoppingListCommands.addProduct(this.activity, productNameContainer, productsList, mainScrollView);
                return true;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.CHECK_PRODUCT_COMMAND, commandType) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {

                if (!ShoppingListCommands.checkProduct(this.activity, productsList, commandParameter)) {
                    continue;
                }

                return true;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.UNCHECK_PRODUCT_COMMAND, commandType) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {

                if (!ShoppingListCommands.uncheckProduct(this.activity, productsList, commandParameter)) {
                    continue;
                }

                return true;
            } else if (StringsSimilarityCalculator.calculateSimilarityCoefficient(Constants.DELETE_PRODUCT_COMMAND, commandType) >=
                    Constants.ACCEPTABLE_SIMILARITY_COEFFICIENT) {

                if (!ShoppingListCommands.deleteProduct(commandParameter, productsList)) {
                    continue;
                }

                return true;
            }
        }

        return true;
    }

    private ArrayList<Product> getAllProductsFromProductsList(ViewGroup productsList) {
        ArrayList<Product> products = new ArrayList<>();

        for (int i = 0; i < productsList.getChildCount(); i++) {
            View child = productsList.getChildAt(i);

            CheckBox checkbox = (CheckBox) child.findViewWithTag(this.activity.getResources().getString(R.string.product_checkbox_tag));
            EditText editText = (EditText) child.findViewWithTag(this.activity.getResources().getString(R.string.edit_text_tag));

            Product product = new Product(editText.getText().toString(), 0d, 0d, 0l, checkbox.isChecked());
            products.add(product);
        }

        return products;
    }

    @Override
    protected SpeechRecognizer getSpeechRecognizer(Activity activity, Intent intent, Class intentClass) {
        return SpeechRecognizerFactory.createShoppingListSpeechRecognizer(this.activity, this.intent, this.intentClass);
    }
}
