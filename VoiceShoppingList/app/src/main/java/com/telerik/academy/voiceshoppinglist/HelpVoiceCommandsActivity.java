package com.telerik.academy.voiceshoppinglist;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HelpVoiceCommandsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_voice_commands);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView pointer = (ImageView) findViewById(R.id.iv_pointer);
        Button showMeHowBtn = (Button) findViewById(R.id.btn_show_me_how);
        Button showMeHowBottomBtn = (Button) findViewById(R.id.btn_show_me_how_bottom);

        final AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animate_pointer_tap_voice_commands);
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
    }
}
