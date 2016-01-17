package com.telerik.academy.voiceshoppinglist;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HelpManipulateProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_manipulate_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView pointer = (ImageView) findViewById(R.id.iv_pointer);
        Button showMeHowCheckUncheckBtn = (Button) findViewById(R.id.btn_show_me_how_check_uncheck);

        final AnimatorSet animatorSetCheckUncheck =
                (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animate_pointer_tap_check_uncheck_product);
        animatorSetCheckUncheck.setTarget(pointer);

        showMeHowCheckUncheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSetCheckUncheck.start();
            }
        });

        Button showMeHowDeleteBtn = (Button) findViewById(R.id.btn_show_me_how_delete);

        final AnimatorSet animatorSetDelete =
                (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animate_pointer_tap_delete_product);
        animatorSetDelete.setTarget(pointer);

        showMeHowDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSetDelete.start();
            }
        });
    }

}
