package com.telerik.academy.voiceshoppinglist;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.telerik.academy.voiceshoppinglist.utilities.OnSwipeTouchListener;
import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognizerFactory;

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
        productNameInput = (EditText) findViewById(R.id.newProductNameInput);

         SpeechRecognitionHandler.startListeningForShoppingListCommands(this, AddNewShoppingListActivity.class);

        productNameInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    ShoppingListCommands.addProduct(AddNewShoppingListActivity.this, productNameInput, productsList, mainScrollView);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddNewShoppingListActivity.this, MainActivity.class);

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
