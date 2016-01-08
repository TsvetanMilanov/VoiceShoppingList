package com.telerik.academy.voiceshoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telerik.academy.voiceshoppinglist.utilities.SpeechRecognizerFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Intent intent = SpeechRecognizerFactory.createSpeechRecognitionIntent(this.getActivity());
        SpeechRecognizer speechRecognizer = SpeechRecognizerFactory.createSpeechRecognizer(this.getActivity(), intent);

        // TODO: Find a way to continue listening after 5 seconds of silence when only onRmsChanged is called and nothing happens.
        speechRecognizer.startListening(intent);

        return view;
    }
}
