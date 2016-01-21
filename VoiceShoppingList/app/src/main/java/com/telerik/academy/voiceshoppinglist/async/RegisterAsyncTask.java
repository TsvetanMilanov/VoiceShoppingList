package com.telerik.academy.voiceshoppinglist.async;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.telerik.academy.voiceshoppinglist.remote.RequestConstants;
import com.telerik.academy.voiceshoppinglist.remote.models.UserLoginRequestModel;
import com.telerik.academy.voiceshoppinglist.remote.models.UserRegisterRequestModel;
import com.telerik.academy.voiceshoppinglist.remote.models.UserRegisterResponseModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class RegisterAsyncTask extends AsyncTask<Void, Void, UserRegisterResponseModel> {

    private RegisterCommand registerCommand;
    private Context context;
    private UserRegisterRequestModel userRegisterRequestModel;

    public RegisterAsyncTask(Context context, UserRegisterRequestModel userRegisterRequestModel, RegisterCommand registerCommand) {
        this.registerCommand = registerCommand;
        this.context = context;
        this.userRegisterRequestModel = userRegisterRequestModel;
    }

    @Override
    protected UserRegisterResponseModel doInBackground(Void... params) {
        Gson gson = new Gson();
        String requestBody = gson.toJson(this.userRegisterRequestModel);

        URL url = null;
        try {
            url = new URL(RequestConstants.BASE_URL + RequestConstants.REGISTER_ROUTE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);
            urlConnection.setRequestMethod("POST");
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

            String resultString = sb.toString();

            UserRegisterResponseModel result = gson.fromJson(resultString, UserRegisterResponseModel.class);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(UserRegisterResponseModel userRegisterResponseModel) {
        this.registerCommand.execute(userRegisterResponseModel);
        super.onPostExecute(userRegisterResponseModel);
    }
}
