package com.telerik.academy.voiceshoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.commands.MainMenuCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView commandsResultTextView;
    private boolean isListening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        commandsResultTextView = (TextView) findViewById(R.id.tv_command_result);
        commandsResultTextView.setVisibility(View.INVISIBLE);
        isListening = false;

        // testDatabase();

        Button addShoppingListBtn = (Button) findViewById(R.id.btn_add_new_shopping_list);
        Button myShoppingLists = (Button) findViewById(R.id.btn_my_shopping_lists);
        Button stopListeningBtn = (Button) findViewById(R.id.btn_stop_listening);
        Button exitBtn = (Button) findViewById(R.id.btn_exit);

        addShoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuCommands.navigateToAddNewShoppingListActivity(MainActivity.this);
            }
        });

        myShoppingLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogFactory.createInformationAlertDialog(MainActivity.this, "Implement me!", null).show();
            }
        });

        stopListeningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListening) {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    isListening = !isListening;
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuCommands.exitApplication(MainActivity.this);
            }
        });
    }

    private void testDatabase() {
        VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this);

        ShoppingList sl = new ShoppingList(new Date(), 1d, 1d, "First shopping list");

        Long slId = db.addShoppingList(sl);

        Product product = new Product("banana", 10d, 20d, slId, true);

        Long pId = db.addProduct(product);

        ArrayList<ShoppingList> allSl = db.getAllShoppingLists();
        ArrayList<Product> allP = db.getAllProducts();

        ShoppingList cSl = db.getShoppingListById(slId);
        Product cP = db.getProductById(pId);

        ArrayList<Product> pBySl = db.getProductsByShoppingListId(slId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.option_start_listening:
                if (!isListening) {
                    SpeechRecognitionHandler.startListeningForMenuCommands(this, AddNewShoppingListActivity.class);
                    commandsResultTextView.setVisibility(View.VISIBLE);
                    isListening = !isListening;
                } else {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    isListening = !isListening;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
