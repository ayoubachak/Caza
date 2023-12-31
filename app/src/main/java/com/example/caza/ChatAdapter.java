package com.example.caza;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caza.models.ChatMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final ArrayList<ChatMessage> chatMessages;

    // View Types for user and phone messages
    private static final int VIEW_TYPE_USER_MESSAGE = 1;
    private static final int VIEW_TYPE_PHONE_MESSAGE = 2;

    public ChatAdapter(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageButton playButton; // Add a play button in your item layout

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            playButton = itemView.findViewById(R.id.play_audio_button); // Assume you have a play button in your item layout
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // Inflate different layouts based on the view type
        if (viewType == VIEW_TYPE_USER_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_user, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_phone, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        if (message.isAudioMessage()) {
            // Set up the UI for an audio message
            holder.playButton.setVisibility(View.VISIBLE);
            holder.playButton.setOnClickListener(v -> playAudio(message.getText()));
            holder.messageText.setText("Audio Message"); // Or any other placeholder text
        } else {
            // Setup for a regular text message
            holder.messageText.setText(message.getText());
            holder.playButton.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemViewType(int position) {
        // Determine which view type to use based on the message origin
        ChatMessage message = chatMessages.get(position);
        if (message.isUserMessage()) {
            return VIEW_TYPE_USER_MESSAGE;
        } else {
            return VIEW_TYPE_PHONE_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    private void playAudio(String audioFilePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

