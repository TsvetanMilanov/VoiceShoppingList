package com.telerik.academy.voiceshoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddNewShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shopping_list);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddNewShoppingListActivity.this, MainActivity.class);

        startActivity(intent);
        super.onBackPressed();
    }
}
