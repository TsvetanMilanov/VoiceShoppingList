package com.telerik.academy.voiceshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        Button voiceCommandsBtn = (Button) findViewById(R.id.btn_voice_commands);
        voiceCommandsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HelpVoiceCommandsActivity.class);
                context.startActivity(intent);
            }
        });

        Button addProductBtn = (Button) findViewById(R.id.btn_add_product);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HelpAddProductActivity.class);
                context.startActivity(intent);
            }
        });

        Button manipulateProductBtn = (Button) findViewById(R.id.btn_manipulate_product);
        manipulateProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HelpManipulateProductActivity.class);
                context.startActivity(intent);
            }
        });
    }

}
