package com.telerik.academy.voiceshoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

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
        EditText newItem = new EditText(this);
        productsList.addView(newItem);

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
}
