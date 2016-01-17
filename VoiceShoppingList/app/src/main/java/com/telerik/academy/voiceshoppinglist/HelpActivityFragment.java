package com.telerik.academy.voiceshoppinglist;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class HelpActivityFragment extends Fragment {

    private Context context;

    public HelpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = this.getContext();
        View view = inflater.inflate(R.layout.fragment_commands_help, container, false);

        ImageView pointer = (ImageView) view.findViewById(R.id.iv_pointer);
        Button showMeHowBtn = (Button) view.findViewById(R.id.btn_show_me_how);
        Button showMeHowBottomBtn = (Button) view.findViewById(R.id.btn_show_me_how_bottom);

        final AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animate_pointer_to_start_listening_commands);
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

        return view;
    }
}
