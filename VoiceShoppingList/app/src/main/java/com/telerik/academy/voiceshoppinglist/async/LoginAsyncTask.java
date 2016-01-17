package com.telerik.academy.voiceshoppinglist.async;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.telerik.academy.voiceshoppinglist.remote.RequestConstants;
import com.telerik.academy.voiceshoppinglist.remote.models.UserRequestModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class LoginAsyncTask extends AsyncTask<Void, Void, String> {
    private LoginCommand loginCommand;
    private Context context;
    private URI uri;
    private UserRequestModel userRequestModel;

    public LoginAsyncTask(Context context, URI uri, UserRequestModel userRequestModel, LoginCommand loginCommand) {
        this.loginCommand = loginCommand;
        this.context = context;
        this.uri = uri;
        this.userRequestModel = userRequestModel;
    }

    @Override
    protected String doInBackground(Void... params) {
        Gson gson = new Gson();
        String requestBody = gson.toJson(this.userRequestModel);

        URL url = null;
        try {
            url = new URL(RequestConstants.BASE_URL + RequestConstants.LOGIN_ROUTE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
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
        this.loginCommand.execute(s);
        super.onPostExecute(s);
    }
}
