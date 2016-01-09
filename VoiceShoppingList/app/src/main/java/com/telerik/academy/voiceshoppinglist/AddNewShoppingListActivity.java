package com.telerik.academy.voiceshoppinglist;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.math.MathContext;

public class AddNewShoppingListActivity extends AppCompatActivity {

    private LinearLayout productsList;
    private ScrollView mainScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shopping_list);

        productsList = (LinearLayout) findViewById(R.id.productsList);
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
    }

    public void onAddBtnClick(View view) {
        LinearLayout newItemRow = (LinearLayout)getLayoutInflater().inflate(R.layout.item_row_template, null);

        productsList.addView(newItemRow);

        mainScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
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
        EditText text = (EditText)parent.getChildAt(1);

        if (clickedBox.isChecked()) {
            text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            text.setPaintFlags(text.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
