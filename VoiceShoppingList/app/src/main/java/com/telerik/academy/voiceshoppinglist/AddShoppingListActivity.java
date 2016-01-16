package com.telerik.academy.voiceshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;

public class AddShoppingListActivity extends AppCompatActivity {
    private TextView commandsResultTextView;

    private LinearLayout productsList;
    private ScrollView mainScrollView;
    private EditText productNameInput;
    private Activity context;
    private boolean isListening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.shopping_list_toolbar);
        setSupportActionBar(toolbar);

        commandsResultTextView = (TextView) findViewById(R.id.tv_shopping_list_commands_result);
        commandsResultTextView.setVisibility(View.INVISIBLE);
        isListening = false;

        productsList = (LinearLayout) findViewById(R.id.productsList);
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        productNameInput = (EditText) findViewById(R.id.newProductNameInput);
        context = this;

        productNameInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    ShoppingListCommands.addProduct(context, productNameInput, productsList, mainScrollView);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_finish_shopping_list:
                ShoppingListCommands.navigateToFinishShoppingListActivity(context, productsList);
                return true;
            case R.id.option_start_listening:
                if (!isListening) {
                    SpeechRecognitionHandler.startListeningForShoppingListCommands(context, AddNewShoppingListActivity.class);
                    commandsResultTextView.setVisibility(View.VISIBLE);
                    isListening = !isListening;
                } else {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    isListening = !isListening;
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MainActivity.class);

        SpeechRecognitionHandler.stopListening();
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void onCheckBoxClick(View view) {
        CheckBox clickedBox = (CheckBox) view;
        ViewGroup parent = (ViewGroup) view.getParent();
        TextView text = (TextView) parent.getChildAt(0);

        if (clickedBox.isChecked()) {
            text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            text.setPaintFlags(text.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
