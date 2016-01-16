package com.telerik.academy.voiceshoppinglist.async;

import android.content.Context;
import android.os.AsyncTask;

import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;

import java.util.ArrayList;

public class SaveShoppingListAsyncTask extends AsyncTask<Void, Void, Void> {
    private SaveShoppingListCommand command;
    private Context context;
    private ShoppingList shoppingList;
    private ArrayList<Product> products;

    public SaveShoppingListAsyncTask(
            Context context,
            ShoppingList shoppingList,
            ArrayList<Product> products,
            SaveShoppingListCommand command) {
        this.context = context;
        this.shoppingList = shoppingList;
        this.products = products;
        this.command = command;
    }

    @Override
    protected Void doInBackground(Void... params) {
        VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this.context);

        long savedShoppingListId = db.addShoppingList(this.shoppingList);

        for (Product product : products) {
            product.setShoppingListId(savedShoppingListId);
            db.addProduct(product);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        command.execute();

        super.onPostExecute(aVoid);
    }
}
