package com.example.caza;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;

public class ModalBottomSheetFragment extends BottomSheetDialogFragment {

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modal_bottom_sheet, container, false);

        // Setup close button
        view.findViewById(R.id.buttonClose).setOnClickListener(v -> dismiss());

        // Setup record button
        view.findViewById(R.id.buttonRecord).setOnClickListener(v -> recordNewCommand());

        // Initialize SpeechRecognizer
        initSpeechRecognizer();

        return view;
    }

    private void initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    handleVoiceCommand(matches.get(0));
                }
            }

            // Implement other necessary methods of RecognitionListener
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {}

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    public void recordNewCommand() {
        // Start voice recognition
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    private void handleVoiceCommand(String command) {
        // Handle the recognized voice command
        // Example: Update UI or perform some action based on the command
    }

    @Override
    public void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
