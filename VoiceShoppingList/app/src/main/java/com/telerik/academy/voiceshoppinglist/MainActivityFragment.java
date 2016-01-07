package com.telerik.academy.voiceshoppinglist;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telerik.academy.voiceshoppinglist.utilities.SpeechListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private SpeechRecognizer recognizer;
    private Intent intent;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getActivity().getApplication().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start talking!!!");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        recognizer = SpeechRecognizer.createSpeechRecognizer(this.getActivity());

        SpeechListener listener = new SpeechListener(this.getActivity(), recognizer, intent);

        recognizer.setRecognitionListener(listener);

        recognizer.startListening(intent);

        return view;
    }
}
