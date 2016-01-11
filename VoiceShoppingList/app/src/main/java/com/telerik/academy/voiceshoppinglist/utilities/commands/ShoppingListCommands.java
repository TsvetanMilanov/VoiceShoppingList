package com.telerik.academy.voiceshoppinglist.utilities.commands;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Paint;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.OnSwipeTouchListener;

public final class ShoppingListCommands {
    public static void addProduct(Activity activity, final EditText productNameInput, LinearLayout productsList, final ScrollView mainScrollView) {
        LinearLayout row = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.item_row_template, null);

        EditText textInput = (EditText) row.getChildAt(2);

        final String productName = productNameInput.getText().toString();
        productNameInput.setText("");

        textInput.setText(productName);

        View dragIcon = row.getChildAt(0);
        dragIcon.setOnTouchListener(new RowContentTouchListener());
        textInput.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onSwipeLeft(View v) {
                deleteEditTextParent(v);
            }
        });
        row.setOnDragListener(new RowContentDragListener());

        productsList.addView(row);

        mainScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                productNameInput.requestFocus();
            }
        }, 200);

        Toast.makeText(activity, "Item added", Toast.LENGTH_SHORT).show();
    }

    public static void setProductName(EditText nameContainer, String productName) {
        nameContainer.setText(productName);
    }

    public static boolean checkProduct(String commandParameter, LinearLayout productsList) {
        CheckBox checkBox = findProductsCheckboxByTag(commandParameter, Constants.CHECKBOX_TAG, productsList);

        if (checkBox == null) {
            return false;
        }

        checkBox.setChecked(!checkBox.isChecked());
        
        checkBox.callOnClick();

        return true;
    }

    public static boolean uncheckProduct(String commandParameter, LinearLayout productsList) {
        CheckBox checkBox = findProductsCheckboxByTag(commandParameter, Constants.CHECKBOX_TAG, productsList);

        if (checkBox == null) {
            return false;
        }

        checkBox.setChecked(!checkBox.isChecked());

        checkBox.callOnClick();

        return true;
    }

    public static void deleteEditTextParent(View view) {
        ((ViewManager) view.getParent().getParent()).removeView((View) view.getParent());
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

    private static final class RowContentTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            View text = ((ViewGroup) view.getParent()).getChildAt(2);
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(text);
                text.startDrag(data, shadowBuilder, text, 0);
                text.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private static class RowContentDragListener implements View.OnDragListener {
//        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
//        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
//                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
//                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();

                    ViewGroup source = (ViewGroup) view.getParent();
                    LinearLayout target = (LinearLayout) v;
                    if (source == target) {
                        break;
                    }

                    EditText targetText = (EditText) target.getChildAt(2);
                    EditText sourceText = (EditText) source.getChildAt(2);
                    CheckBox sourceCheck = (CheckBox) source.getChildAt(1);
                    CheckBox targetCheck = (CheckBox) target.getChildAt(1);

                    if (sourceCheck.isChecked() != targetCheck.isChecked()) {
                        if (sourceCheck.isChecked()) {
                            sourceText.setPaintFlags(sourceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            targetText.setPaintFlags(targetText.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                        } else {
                            targetText.setPaintFlags(targetText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            sourceText.setPaintFlags(sourceText.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                        }

                        sourceCheck.setChecked(!sourceCheck.isChecked());
                        targetCheck.setChecked(!targetCheck.isChecked());
                    }

                    source.removeView(sourceText);
                    target.removeView(targetText);
                    target.addView(sourceText, 2);
                    source.addView(targetText, 2);

//                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
//                    v.setBackgroundDrawable(normalShape);
                    View view2 = (View) event.getLocalState();
                    view2.setVisibility(View.VISIBLE);

                default:
                    break;
            }
            return true;
        }
    }
}
