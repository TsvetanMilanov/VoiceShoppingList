package com.telerik.academy.voiceshoppinglist.utilities.commands;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.utilities.OnSwipeTouchListener;

/**
 * Created by DevNinja on 16-Jan-16.
 */
public final class ShoppingListTouchCommands {

    public static void addProduct(final Activity activity, final EditText productNameInput, LinearLayout productsList, final ScrollView mainScrollView) {
        LinearLayout row = getProductRowElement(activity, productNameInput);

        productsList.addView(row);

        mainScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                productNameInput.requestFocus();
            }
        }, 200);
    }

    @NonNull
    private static LinearLayout getProductRowElement(final Activity activity, EditText productNameInput) {
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

        int itemsCount = ((ViewGroup) activity.findViewById(R.id.productsList)).getChildCount();
        TextView productIndexView = (TextView) row.findViewWithTag(activity.getResources().getString(R.string.product_index_tag));
        productIndexView.setText("");
        productIndexView.setText((itemsCount + 1) + ". ");
        return row;
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
            productIndexView.setText((i + 1) + ". ");
        }
    }
}
