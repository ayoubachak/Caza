package com.example.caza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.caza.models.ChatMessage;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private EditText messageInput;
    private ImageButton sendButton;
    private ImageButton voiceRecordButton; // Button for voice recording (to be implemented later)

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
                // Placeholder for voice recording functionality
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            addMessageToChat(messageText, true); // User message
            messageInput.setText("");

            // Simulate a random response from the phone
            simulatePhoneResponse();
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

    // Other methods and functionalities remain the same
}
