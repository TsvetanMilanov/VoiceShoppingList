package com.telerik.academy.voiceshoppinglist;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Layout;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.telerik.academy.voiceshoppinglist.utilities.OnSwipeTouchListener;

import java.math.MathContext;

public class AddNewShoppingListActivity extends AppCompatActivity {

    private LinearLayout productsList;
    private ScrollView mainScrollView;
    private EditText productNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shopping_list);

        productsList = (LinearLayout) findViewById(R.id.productsList);
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        productNameInput = (EditText)findViewById(R.id.newProductNameInput);

        productNameInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    onAddBtnClick(productNameInput);
                    return true;
                }
                return false;
            }
        });
    }

    public void onAddBtnClick(View view) {
        LinearLayout row = (LinearLayout)getLayoutInflater().inflate(R.layout.item_row_template, null);

        EditText textInput = (EditText)row.getChildAt(2);

        final String productName = productNameInput.getText().toString();
        productNameInput.setText("");

        textInput.setText(productName);

        View dragIcon = row.getChildAt(0);
        dragIcon.setOnTouchListener(new RowContentTouchListener());
        textInput.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft(View v) {
                onDeleteBtnClick(v);
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

        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddNewShoppingListActivity.this, MainActivity.class);

        startActivity(intent);
        super.onBackPressed();
    }

    public void onDeleteBtnClick(View view) {
        ((ViewManager)view.getParent().getParent()).removeView((View)view.getParent());
    }

    public void onCheckBoxClick(View view) {
        CheckBox clickedBox = (CheckBox)view;
        ViewGroup parent = (ViewGroup)view.getParent();
        EditText text = (EditText)parent.getChildAt(2);

        if (clickedBox.isChecked()) {
            text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            text.setPaintFlags(text.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    private final class RowContentTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            View text = ((ViewGroup)view.getParent()).getChildAt(2);
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

    private class RowContentDragListener implements View.OnDragListener {
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

                    EditText targetText = (EditText)target.getChildAt(2);
                    EditText sourceText = (EditText)source.getChildAt(2);
                    CheckBox sourceCheck = (CheckBox)source.getChildAt(1);
                    CheckBox targetCheck = (CheckBox)target.getChildAt(1);

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
