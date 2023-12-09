package com.example.caza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.caza.audio.VoiceRecorder;
import com.example.caza.callbacks.GPSResultCallback;
import com.example.caza.callbacks.TemperatureResultCallback;
import com.example.caza.handlers.GPSCommandHandler;
import com.example.caza.models.ChatMessage;
import android.Manifest;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GPSResultCallback, TemperatureResultCallback {



    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private EditText messageInput;
    private ImageButton sendButton;
    private ImageButton voiceRecordButton; // Button for voice recording (to be implemented later)
    public CommandExecutor executor ;
    private VoiceRecorder voiceRecorder;
    public static final int SMS_REQUEST_CODE = 101;
    public static final int GPS_REQUEST_CODE = 102;
    private static final int RECORD_AUDIO_REQUEST_CODE = 103;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        voiceRecordButton = findViewById(R.id.voice_record_button); // Initialize the voice record button

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        executor = new CommandExecutor(this);
        voiceRecorder = new VoiceRecorder(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // TODO: Implement the voice record button functionality
        voiceRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    voiceRecorder.stopRecording();
                    isRecording = false;
                    voiceRecordButton.setImageResource(android.R.drawable.ic_btn_speak_now); // Change to record icon
                    addAudioMessageToChat(voiceRecorder.getCurrentFilePath());
                } else {
                    voiceRecorder.startRecording();
                    isRecording = true;
                    voiceRecordButton.setImageResource(android.R.drawable.ic_media_pause); // Change to stop icon
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {

            addMessageToChat(messageText, true); // User message
            messageInput.setText("");

            String response = executor.executeCommand(messageText,this );
            addMessageToChat(response, false); // Display the response in chat

            // Simulate a random response from the phone
            // simulatePhoneResponse();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, you can retry sending the SMS
            } else {
                // Permission was denied, inform the user
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Audio recording permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Audio recording permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void simulatePhoneResponse() {
        // Delay the response to simulate typing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] responses = {"Hello!", "How are you?", "That's interesting.", "Can you tell me more?"};
                int randomIndex = new Random().nextInt(responses.length);
                addMessageToChat(responses[randomIndex], false); // Phone response
            }
        }, 1000); // 1-second delay
    }

    private void addMessageToChat(String messageText, boolean isUserMessage) {
        ChatMessage newMessage = new ChatMessage(messageText, isUserMessage); // Assuming ChatMessage has a constructor to distinguish user messages from phone responses
        chatMessages.add(newMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }
    private void addAudioMessageToChat(String audioFilePath) {
        ChatMessage audioMessage = new ChatMessage(audioFilePath, true, true); // Assuming ChatMessage constructor accepts a third parameter to indicate it's an audio message
        chatMessages.add(audioMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }
    @Override
    public void onGPSResult(String gpsData) {
        addMessageToChat(gpsData, false);
    }

    @Override
    public void onTemperatureResult(String temperatureData) {
        addMessageToChat(temperatureData, false);
    }

}
