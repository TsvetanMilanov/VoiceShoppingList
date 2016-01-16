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

public final class ShoppingListCommands {
    public static void addProduct(final Activity activity, final EditText productNameInput, LinearLayout productsList, final ScrollView mainScrollView) {
        LinearLayout row = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_row_template, null);

        final TextView textInput = (TextView) row.findViewWithTag(activity.getResources().getString(R.string.tv_product_name_container_tag));

        final String productName = productNameInput.getText().toString();
        productNameInput.setText("");

        textInput.setText(productName);

        row.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onSwipeLeft(View v) {
                ((ViewGroup) v.getParent()).removeView((View) v);
                reindexProducts(activity);
            }

            @Override
            public void onSwipeRight(View v) {
                CheckBox clickedBox = (CheckBox) ((ViewGroup) v).getChildAt(2);
                clickedBox.performClick();
            }
        });

        row.setOnDragListener(new RowContentDragListener());
        View list = activity.findViewById(R.id.productsList);
        list.setOnDragListener(new RowContentDragListener());

        TextView checkBox = (CheckBox) row.findViewWithTag(activity.getResources().getString(R.string.product_checkbox_tag));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox clickedBox = (CheckBox) v;
                ViewGroup parent = (ViewGroup) v.getParent();
//                TextView text = (TextView) parent.findViewWithTag(activity.getResources().getString(R.string.tv_product_name_container_tag));

                if (clickedBox.isChecked()) {
                    textInput.setPaintFlags(textInput.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textInput.setPaintFlags(textInput.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

        Number itemsCount = ((ViewGroup) activity.findViewById(R.id.productsList)).getChildCount();
        TextView productIndexView = (TextView) row.findViewWithTag(activity.getResources().getString(R.string.product_index_tag));
        productIndexView.setText("");
        productIndexView.setText(itemsCount + ". ");

        productsList.addView(row);

        mainScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                productNameInput.requestFocus();
            }
        }, 200);
    }

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

    private static class RowContentDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View target, DragEvent event) {

            View sourceRow = (View) event.getLocalState();
            ViewGroup container = (ViewGroup) target.getParent();


            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;

                case DragEvent.ACTION_DROP:
                    if (sourceRow == target || target.getId() == R.id.productsList || target.getId() == R.id.newProductNameInput) {
                        sourceRow.setVisibility(View.VISIBLE);
                        break;
                    }

                    int index = container.indexOfChild(target);
                    container.removeView(sourceRow);
                    container.addView(sourceRow, index);
                    sourceRow.setVisibility(View.VISIBLE);

                    reindexProducts((Activity) target.getContext());

                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    sourceRow.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

            return true;
        }
    }

    private static void reindexProducts(Activity activity) {
        ViewGroup productsList = (ViewGroup) activity.findViewById(R.id.productsList);

        for (int i = 0; i < productsList.getChildCount(); i++) {
            ViewGroup row = (ViewGroup) productsList.getChildAt(i);
            TextView productIndexView = (TextView) row.findViewWithTag(activity.getResources().getString(R.string.product_index_tag));
            productIndexView.setText("");
            productIndexView.setText(i + ". ");
        }
    }
}