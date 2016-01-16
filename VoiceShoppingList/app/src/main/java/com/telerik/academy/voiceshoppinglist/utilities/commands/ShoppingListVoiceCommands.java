package com.telerik.academy.voiceshoppinglist.utilities.commands;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.FinishShoppingListActivity;
import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.OnSwipeTouchListener;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;

import java.util.ArrayList;

public final class ShoppingListVoiceCommands {


    public static boolean deleteProduct(String commandParameter, ViewGroup productsList) {
        View product = findViewByNumberInParent(commandParameter, productsList);

        if (product == null) {
            return false;
        }

        productsList.removeView(product);

        return true;
    }

    public static void setProductName(EditText nameContainer, String productName) {
        nameContainer.setText(productName);
    }

    public static boolean checkProduct(Activity activity, LinearLayout productsList, String commandParameter) {
        CheckBox checkBox = findProductsCheckboxByTag(activity, productsList, commandParameter);

        if (checkBox == null) {
            return false;
        }

        checkBox.setChecked(!checkBox.isChecked());

        checkBox.callOnClick();

        return true;
    }

    public static boolean uncheckProduct(Activity activity, LinearLayout productsList, String commandParameter) {
        CheckBox checkBox = findProductsCheckboxByTag(activity, productsList, commandParameter);

        if (checkBox == null) {
            return false;
        }

        checkBox.setChecked(!checkBox.isChecked());

        checkBox.callOnClick();

        return true;
    }

    private static CheckBox findProductsCheckboxByTag(Activity activity, LinearLayout productsList, String commandParameter) {
        View currentProductContainer = findViewByNumberInParent(commandParameter, productsList);

        if (currentProductContainer == null) {
            return null;
        }

        CheckBox checkBox = (CheckBox) currentProductContainer.findViewWithTag(activity.getResources().getString(R.string.product_checkbox_tag));

        return checkBox;
    }

    private static View findViewByNumberInParent(String commandParameter, ViewGroup productsList) {
        int itemNumberInParent = 0;

        try {
            itemNumberInParent = Integer.parseInt(commandParameter);
        } catch (NumberFormatException e) {
            return null;
        }

        View foundView = productsList.getChildAt(itemNumberInParent - 1);

        return foundView;
    }

    public static void navigateToFinishShoppingListActivity(Context context, ViewGroup productsList) {
        ArrayList<Product> products = getAllProductsFromProductsList(productsList, context);

        Intent intent = new Intent(context, FinishShoppingListActivity.class);

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.PRODUCTS_LIST_BUNDLE_KEY, products);

        intent.putExtras(bundle);

        context.startActivity(intent);

        //speechRecognizer.destroy();
        SpeechRecognitionHandler.stopListening();
    }

    private static ArrayList<Product> getAllProductsFromProductsList(ViewGroup productsList, Context context) {
        ArrayList<Product> products = new ArrayList<>();

        for (int i = 0; i < productsList.getChildCount(); i++) {
            View child = productsList.getChildAt(i);

            CheckBox checkbox = (CheckBox) child.findViewWithTag(context.getResources().getString(R.string.product_checkbox_tag));
            TextView editText = (TextView) child.findViewWithTag(context.getResources().getString(R.string.tv_product_name_container_tag));

            Product product = new Product(editText.getText().toString(), 0d, 0d, 0l, checkbox.isChecked());
            products.add(product);
        }

        return products;
    }


}