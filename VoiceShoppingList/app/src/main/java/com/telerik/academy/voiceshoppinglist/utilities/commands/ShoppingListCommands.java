package com.telerik.academy.voiceshoppinglist.utilities.commands;

import android.app.Activity;
import android.content.ClipData;
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

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.utilities.OnSwipeTouchListener;

public final class ShoppingListCommands {
    public static void addProduct(Activity activity, final EditText productNameInput, LinearLayout productsList, final ScrollView mainScrollView) {
        LinearLayout row = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_row_template, null);

        TextView textInput = (TextView) row.getChildAt(0);

        final String productName = productNameInput.getText().toString();
        productNameInput.setText("");

        textInput.setText(productName);

//        row.setOnLongClickListener(new RowLongTouchListener());
        row.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onSwipeLeft(View v) {
                ((ViewGroup) v.getParent()).removeView((View) v);
            }
        });

        row.setOnDragListener(new RowContentDragListener());
        View topLayout = activity.findViewById(R.id.topLayout);
        topLayout.setOnDragListener(new RowContentDragListener());

        productsList.addView(row);

        mainScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                productNameInput.requestFocus();
            }
        }, 200);

//        Toast.makeText(activity, "Item added", Toast.LENGTH_SHORT).show();
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
                    if (sourceRow == target || target.getId() == R.id.topLayout) {
                        sourceRow.setVisibility(View.VISIBLE);
                        break;
                    }

                    int index = container.indexOfChild(target);
                    container.removeView(sourceRow);
                    container.addView(sourceRow, index);
                    sourceRow.setVisibility(View.VISIBLE);

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
}
