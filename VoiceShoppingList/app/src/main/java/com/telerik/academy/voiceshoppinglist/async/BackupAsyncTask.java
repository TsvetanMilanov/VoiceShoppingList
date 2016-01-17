package com.telerik.academy.voiceshoppinglist.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.remote.RequestConstants;
import com.telerik.academy.voiceshoppinglist.remote.models.ShoppingListRequestModel;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class BackupAsyncTask extends AsyncTask<Void, Void, String> {
    private BackupCommand backupCommand;
    private Context context;

    public BackupAsyncTask(Context context, BackupCommand backupCommand) {
        this.backupCommand = backupCommand;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this.context);

        SharedPreferences settings = this.context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, 0);
        String token = settings.getString(Constants.TOKEN_SHARED_PREFERENCE_KEY, null);

        URL url = null;
        try {
            url = new URL(RequestConstants.BASE_URL + RequestConstants.BACKUP_ROUTE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList<ShoppingList> allShoppingLists = db.getAllShoppingLists();

        ArrayList<ShoppingListRequestModel> requestObject = new ArrayList<>();

        for (ShoppingList shoppingList : allShoppingLists) {
            ShoppingListRequestModel currentShoppingListRequestModel = new ShoppingListRequestModel();
            currentShoppingListRequestModel.name = shoppingList.getName();
            currentShoppingListRequestModel.createdOn = shoppingList.getCreatedOn();
            currentShoppingListRequestModel.sqliteId = shoppingList.get_ID();
            currentShoppingListRequestModel.products = db.getProductsByShoppingListId(shoppingList.get_ID());
            requestObject.add(currentShoppingListRequestModel);
        }

        Gson gson = new Gson();

        String requestBody = gson.toJson(requestObject);

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Accept", "application/json");

            OutputStream os = null;
            os = urlConnection.getOutputStream();
            os.write(requestBody.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            String result = sb.toString();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        this.backupCommand.execute(s);
        super.onPostExecute(s);
    }
}
