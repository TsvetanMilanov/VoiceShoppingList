package com.telerik.academy.voiceshoppinglist;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;

import java.security.PrivateKey;

public class HelpVoiceCommandsActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_voice_commands);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        ImageView pointer = (ImageView) findViewById(R.id.iv_pointer);
        Button showMeHowBtn = (Button) findViewById(R.id.btn_show_me_how);
        Button showMeHowBottomBtn = (Button) findViewById(R.id.btn_show_me_how_bottom);
        Button showAllCommandsBtn = (Button) findViewById(R.id.btn_show_commands);

        final AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animate_pointer_tap_voice_commands);
        animatorSet.setTarget(pointer);

        showMeHowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSet.start();
            }
        });

        showMeHowBottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSet.start();
            }
        });

        showAllCommandsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogFactory.createInformationAlertDialog(context, context.getResources().getString(R.string.all_commands_list), "All commands", null).show();
            }
        });
    }
}
