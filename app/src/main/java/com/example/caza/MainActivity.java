package com.example.caza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Toast;

import com.example.caza.models.ChatMessage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver voiceReceiver;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private FloatingActionButton fab;
    private boolean isRecording = false;


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private void requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                // Permission denied. Handle the functionality that depends on this permission.
                Toast.makeText(this, "Permission denied to record audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestMicrophonePermission();
        initSpeechRecognizer();

        // Start voice recognition service
        Intent serviceIntent = new Intent(this, VoiceService.class);
        startService(serviceIntent);

        // Register Broadcast Receiver
        voiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                if ("Hi".equals(message)) {
                    addMessageToChat("Hi");
                    showModalBottomSheet();
                }
            }
        };
        IntentFilter filter = new IntentFilter("com.example.caza.VOICE_RECOGNITION");
        registerReceiver(voiceReceiver, filter);
    }



    private void initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    // Update chat with the recognized text
                    addMessageToChat(matches.get(0));
                }
            }

            // Implement other necessary methods of RecognitionListener
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {
//                fab.setImageResource(R.drawable.arretez); // Change to your recording icon
                isRecording = true;
            }

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
//                fab.setImageResource(R.drawable.microphone); // Change to your default icon
                isRecording = false;
            }

            @Override
            public void onError(int error) {
                // Handle error here
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    // Update chat with real-time recognized text
                    addMessageToChat(matches.get(0));
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    public void recordVoiceCommand(View view) {
        if (!isRecording) {
            speechRecognizer.startListening(speechRecognizerIntent);
        } else {
            speechRecognizer.stopListening();
//            fab.setImageResource(R.drawable.microphone); // Change to your default icon
            isRecording = false;
        }
    }
    private void addMessageToChat(String messageText) {
        ChatMessage newMessage = new ChatMessage(messageText);
        chatMessages.add(newMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }
    private void showModalBottomSheet() {
        ModalBottomSheetFragment bottomSheet = new ModalBottomSheetFragment();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }


    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        unregisterReceiver(voiceReceiver);
        super.onDestroy();
    }
}