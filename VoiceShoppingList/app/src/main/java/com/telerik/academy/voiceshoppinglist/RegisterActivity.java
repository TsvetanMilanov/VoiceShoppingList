package com.telerik.academy.voiceshoppinglist;

import android.app.Activity;
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
import com.telerik.academy.voiceshoppinglist.async.RegisterAsyncTask;
import com.telerik.academy.voiceshoppinglist.async.RegisterCommand;
import com.telerik.academy.voiceshoppinglist.remote.RequestConstants;
import com.telerik.academy.voiceshoppinglist.remote.models.UserLoginRequestModel;
import com.telerik.academy.voiceshoppinglist.remote.models.UserRegisterRequestModel;
import com.telerik.academy.voiceshoppinglist.remote.models.UserRegisterResponseModel;
import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.OkCommand;

import java.net.URI;
import java.net.URISyntaxException;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button registerBtn = (Button) findViewById(R.id.btn_register);
        activity = this;

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegisterRequestModel user = new UserRegisterRequestModel();
                user.userName = ((EditText) activity.findViewById(R.id.et_username)).getText().toString();
                user.password = ((EditText) activity.findViewById(R.id.et_password)).getText().toString();
                user.firstName = ((EditText) activity.findViewById(R.id.et_first_name)).getText().toString();
                user.lastName = ((EditText) activity.findViewById(R.id.et_last_name)).getText().toString();

                RegisterAsyncTask loginAsyncTask = new RegisterAsyncTask(activity, user, new RegisterCommand() {
                    @Override
                    public void execute(UserRegisterResponseModel user) {
                        progressDialog.dismiss();
                        if (user != null) {
                            SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, 0);

                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(Constants.TOKEN_SHARED_PREFERENCE_KEY, user.token);

                            editor.commit();

                            AlertDialogFactory.createInformationAlertDialog(activity, "Register successful.", "Success", new OkCommand() {
                                @Override
                                public void execute() {
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            }).show();
                        } else {
                            AlertDialogFactory.createInformationAlertDialog(activity, "Register failed.", "Error", null).show();
                        }
                    }
                });

                progressDialog = new ProgressDialog(activity);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Registering user...");
                progressDialog.show();
                loginAsyncTask.execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity, MainActivity.class);

        activity.startActivity(intent);
        activity.finish();
        super.onBackPressed();
    }

}
