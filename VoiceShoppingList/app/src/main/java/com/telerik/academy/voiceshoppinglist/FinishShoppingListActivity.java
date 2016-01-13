package com.telerik.academy.voiceshoppinglist;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telerik.academy.voiceshoppinglist.async.SaveShoppingListAsyncTask;
import com.telerik.academy.voiceshoppinglist.async.SaveShoppingListCallback;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;

import java.util.ArrayList;
import java.util.Date;

public class FinishShoppingListActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Product> productsList;
    private EditText shoppingListNameEditText;
    private Button saveToLocalDbBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_shopping_list);

        productsList = (ArrayList<Product>) getIntent().getExtras().get(Constants.PRODUCTS_LIST_BUNDLE_KEY);

        context = this;

        shoppingListNameEditText = (EditText) findViewById(R.id.et_shopping_list_name);
        saveToLocalDbBtn = (Button) findViewById(R.id.btn_save_to_local_db);
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving shopping list to local database.");

        saveToLocalDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Get Location
                ShoppingList shoppingList = new ShoppingList(new Date(), 0d, 0d, shoppingListNameEditText.getText().toString());
                SaveShoppingListAsyncTask saveShoppingListAsyncTask = new SaveShoppingListAsyncTask(context, shoppingList, productsList, new SaveShoppingListCallback() {
                    @Override
                    public void resolve() {
                        progressDialog.dismiss();
                    }
                });

                progressDialog.show();
                saveShoppingListAsyncTask.execute();
                AlertDialogFactory.createInformationAlertDialog(context, "Shopping list saved successfully.", null).show();
            }
        });
    }
}
