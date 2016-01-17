package com.telerik.academy.voiceshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListTouchCommands;
import com.telerik.academy.voiceshoppinglist.utilities.commands.ShoppingListVoiceCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;

import java.util.ArrayList;

public class ViewShoppingListActivity extends AppCompatActivity {
    private TextView commandsResultTextView;
    private ShoppingList shoppingList;
    private LinearLayout productsList;
    private ScrollView mainScrollView;
    private EditText productNameInput;
    private Activity context;
    private boolean isListening;
    private View micResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.shopping_list_toolbar);
        setSupportActionBar(toolbar);

        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) findViewById(android.R.id.content);

        micResultView = contentFrameLayout.findViewWithTag(this.getResources().getString(R.string.mic_result_tag));

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
                    ShoppingListTouchCommands.addProduct(context, productNameInput, productsList, mainScrollView);
                    return true;
                }
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            shoppingList = (ShoppingList) getIntent().getExtras().get(Constants.SHOPPING_LIST_BUNDLE_KEY);
        }

        if (shoppingList != null) {
            loadItemsList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_add_product:
                ShoppingListTouchCommands.addProduct(this, productNameInput, productsList, mainScrollView);
                return true;
            case R.id.option_finish_shopping_list:
                if (shoppingList != null) {
                    return true;
                }

                ShoppingListVoiceCommands.navigateToFinishShoppingListActivity(context, productsList);
                return true;
            case R.id.option_start_listening:
                if (!isListening) {
                    SpeechRecognitionHandler.startListeningForShoppingListCommands(context, ViewShoppingListActivity.class);
                    commandsResultTextView.setVisibility(View.VISIBLE);
                    isListening = !isListening;
                } else {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    micResultView.setVisibility(View.INVISIBLE);
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

    private void loadItemsList() {
        VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this);

        ArrayList<Product> products = db.getProductsByShoppingListId(shoppingList.get_ID());

        for (Product product : products) {
            productNameInput.setText(product.getName());
            ShoppingListTouchCommands.addProduct(this, productNameInput, productsList, mainScrollView);

            if (product.getIsChecked()) {
                View lastChild = productsList.getChildAt(productsList.getChildCount() - 1);
                CheckBox checkBox = (CheckBox) lastChild.findViewWithTag(context.getResources().getString(R.string.product_checkbox_tag));
                TextView textView = (TextView) lastChild.findViewWithTag(context.getResources().getString(R.string.tv_product_name_container_tag));

                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                checkBox.setChecked(true);
            }
        }
    }
}
