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

import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.OnSwipeTouchListener;
import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListVoiceCommands;
import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListTouchCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognizerFactory;

import java.util.ArrayList;

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
                    ShoppingListTouchCommands.addProduct(AddNewShoppingListActivity.this, productNameInput, productsList, mainScrollView);
                    return true;
                }
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        int listIndex = -1;
        if (extras != null) {
            String extraText = extras.getString(Intent.EXTRA_TEXT);
            if (extraText != null) {
                listIndex = Integer.parseInt(extraText);
            }
        }

        if (listIndex >=0) {
            loadItemsList(listIndex);
        }
    }

    private void loadItemsList(int listIndex) {
        VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this);
        ArrayList<ShoppingList> allItemLists = db.getAllShoppingLists();

        ShoppingList loadedList = allItemLists.get(listIndex);
        String name = loadedList.getName();
        productNameInput.setText(name);
        ShoppingListTouchCommands.addProduct(this, productNameInput, productsList, mainScrollView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddNewShoppingListActivity.this, MainActivity.class);

        SpeechRecognitionHandler.stopListening();
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}
