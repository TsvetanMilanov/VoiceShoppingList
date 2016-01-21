package com.telerik.academy.voiceshoppinglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telerik.academy.voiceshoppinglist.async.LoginAsyncTask;
import com.telerik.academy.voiceshoppinglist.async.LoginCommand;
import com.telerik.academy.voiceshoppinglist.remote.RequestConstants;
import com.telerik.academy.voiceshoppinglist.remote.models.UserLoginRequestModel;
import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.OkCommand;

import java.net.URI;
import java.net.URISyntaxException;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button loginBtn = (Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URI uri = null;
                try {
                    uri = new URI(RequestConstants.BASE_URL + RequestConstants.LOGIN_ROUTE);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                UserLoginRequestModel user = new UserLoginRequestModel();
                user.userName = ((EditText) LoginActivity.this.findViewById(R.id.et_username)).getText().toString();
                user.password = ((EditText) LoginActivity.this.findViewById(R.id.et_password)).getText().toString();

                LoginAsyncTask loginAsyncTask = new LoginAsyncTask(LoginActivity.this, uri, user, new LoginCommand() {
                    @Override
                    public void execute(String token) {
                        progressDialog.dismiss();
                        if (token != null) {
                            SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, 0);

                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(Constants.TOKEN_SHARED_PREFERENCE_KEY, token);

                            editor.commit();

                            AlertDialogFactory.createInformationAlertDialog(LoginActivity.this, "Login successful.", "Success", new OkCommand() {
                                @Override
                                public void execute() {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    LoginActivity.this.startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                            }).show();
                        } else {
                            AlertDialogFactory.createInformationAlertDialog(LoginActivity.this, "Login failed.", "Error", null).show();
                        }
                    }
                });

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                loginAsyncTask.execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
        super.onBackPressed();
    }
}
