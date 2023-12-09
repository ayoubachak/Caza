package com.example.caza.models;

public class ChatMessage {
    private String text;
    private boolean isUserMessage;
    private boolean isAudioMessage; // Flag to indicate if the message is an audio message

    // Constructor for text messages
    public ChatMessage(String text, boolean isUserMessage) {
        this.text = text;
        this.isUserMessage = isUserMessage;
        this.isAudioMessage = false; // Default to false for text messages
    }

    // Constructor for audio messages
    public ChatMessage(String audioFilePath, boolean isUserMessage, boolean isAudioMessage) {
        this.text = audioFilePath; // For audio messages, text will hold the file path
        this.isUserMessage = isUserMessage;
        this.isAudioMessage = isAudioMessage;
    }

    public String getText() {
        return text;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }

    public boolean isAudioMessage() {
        return isAudioMessage;
    }

    // Optionally, add setters or other methods as needed.
}