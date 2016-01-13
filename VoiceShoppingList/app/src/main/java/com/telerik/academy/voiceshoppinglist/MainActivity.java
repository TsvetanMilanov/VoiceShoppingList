package com.telerik.academy.voiceshoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.commands.MainMenuCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                SpeechRecognitionHandler.stopListening();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuCommands.exitApplication(MainActivity.this);
            }
        });
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
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
