package com.telerik.academy.voiceshoppinglist.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telerik.academy.voiceshoppinglist.MainActivity;
import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.remote.RequestConstants;
import com.telerik.academy.voiceshoppinglist.remote.models.ShoppingListResponseModel;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RestoreAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private RestoreCommand restoreCommand;
    private Context context;

    public RestoreAsyncTask(MainActivity context, RestoreCommand restoreCommand) {
        this.restoreCommand = restoreCommand;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        SharedPreferences settings = this.context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, 0);
        String token = settings.getString(Constants.TOKEN_SHARED_PREFERENCE_KEY, null);

        URL url = null;
        try {
            url = new URL(RequestConstants.BASE_URL + RequestConstants.RESTORE_ROUTE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Accept", "application/json");

            StringBuilder sb = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            String result = sb.toString();
            Gson gson = new Gson();

            ArrayList<ShoppingListResponseModel> responseObjects =
                    gson.fromJson(result, new TypeToken<ArrayList<ShoppingListResponseModel>>() {
                    }.getType());

            VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this.context);
            for (ShoppingListResponseModel responseObject : responseObjects) {
                ShoppingList shoppingList = new ShoppingList(responseObject.createdOn, 0d, 0d, responseObject.name);
                long newId = db.addShoppingList(shoppingList);

                ArrayList<Product> products = responseObject.products;
                for (Product product : products) {
                    product.setShoppingListId(newId);
                    db.addProduct(product);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean s) {
        this.restoreCommand.execute(s);
        super.onPostExecute(s);
    }
}
