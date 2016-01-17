package com.telerik.academy.voiceshoppinglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.async.BackupAsyncTask;
import com.telerik.academy.voiceshoppinglist.async.BackupCommand;
import com.telerik.academy.voiceshoppinglist.async.RestoreAsyncTask;
import com.telerik.academy.voiceshoppinglist.async.RestoreCommand;
import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.commands.MainMenuCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    private View micResultView;
    private TextView commandsResultTextView;
    private boolean isListening;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) findViewById(android.R.id.content);

        micResultView = contentFrameLayout.findViewWithTag(this.getResources().getString(R.string.mic_result_tag));
        commandsResultTextView = (TextView) findViewById(R.id.tv_command_result);
        commandsResultTextView.setVisibility(View.INVISIBLE);
        isListening = false;

        // testDatabase();

        Button addShoppingListBtn = (Button) findViewById(R.id.btn_add_new_shopping_list);
        Button myShoppingListsBtn = (Button) findViewById(R.id.btn_my_shopping_lists);
        Button loginBtn = (Button) findViewById(R.id.btn_login);
        Button logoutBtn = (Button) findViewById(R.id.btn_logout);
        Button backupBtn = (Button) findViewById(R.id.btn_backup);
        final Button restoreBtn = (Button) findViewById(R.id.btn_restore);
        Button helpBtn = (Button) findViewById(R.id.btn_help);
        Button exitBtn = (Button) findViewById(R.id.btn_exit);

        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, 0);
        String token = settings.getString(Constants.TOKEN_SHARED_PREFERENCE_KEY, null);

        if (token != null && token.length() > 0) {
            loginBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
            backupBtn.setVisibility(View.VISIBLE);
            restoreBtn.setVisibility(View.VISIBLE);
        } else {
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
            backupBtn.setVisibility(View.GONE);
            restoreBtn.setVisibility(View.GONE);
        }

        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupAsyncTask backupAsyncTask = new BackupAsyncTask(MainActivity.this, new BackupCommand() {
                    @Override
                    public void execute(String result) {
                        progressDialog.dismiss();
                        if (result != null && result.length() > 0) {
                            AlertDialogFactory.createInformationAlertDialog(MainActivity.this, "Backup successful.", "Success!", null).show();
                        } else {
                            AlertDialogFactory.createInformationAlertDialog(MainActivity.this, "Backup not successful.", "Error!", null).show();
                        }
                    }
                });

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Backing up shopping lists...");
                progressDialog.show();
                backupAsyncTask.execute();
            }
        });

        restoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestoreAsyncTask restoreAsyncTask = new RestoreAsyncTask(MainActivity.this, new RestoreCommand() {
                    @Override
                    public void execute(Boolean result) {
                        progressDialog.dismiss();
                        if (result) {
                            AlertDialogFactory.createInformationAlertDialog(MainActivity.this, "Restore successful.", "Success!", null).show();
                        } else {
                            AlertDialogFactory.createInformationAlertDialog(MainActivity.this, "Restore not successful.", "Error!", null).show();
                        }
                    }
                });

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Restoring shopping lists...");
                progressDialog.show();
                restoreAsyncTask.execute();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
                if (isListening) {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    micResultView.setVisibility(View.INVISIBLE);
                    isListening = !isListening;
                }

                MainActivity.this.finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, 0);

                SharedPreferences.Editor editor = settings.edit();
                editor.putString(Constants.TOKEN_SHARED_PREFERENCE_KEY, null);

                editor.commit();

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(intent);
                if (isListening) {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    micResultView.setVisibility(View.INVISIBLE);
                    isListening = !isListening;
                }

                MainActivity.this.finish();
            }
        });

        addShoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuCommands.navigateToAddNewShoppingListActivity(MainActivity.this);
            }
        });

        myShoppingListsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuCommands.navigateToLoadSavedListActivity(MainActivity.this);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);

                MainActivity.this.startActivity(intent);

                if (isListening) {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    micResultView.setVisibility(View.INVISIBLE);
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
                    SpeechRecognitionHandler.startListeningForMenuCommands(this, MainActivity.class);
                    commandsResultTextView.setVisibility(View.VISIBLE);
                    isListening = !isListening;
                } else {
                    SpeechRecognitionHandler.stopListening();
                    commandsResultTextView.setVisibility(View.INVISIBLE);
                    micResultView.setVisibility(View.INVISIBLE);
                    isListening = !isListening;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
