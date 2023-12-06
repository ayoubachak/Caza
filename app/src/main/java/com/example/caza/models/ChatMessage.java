package com.example.caza.models;

public class ChatMessage {
    private String text;
    private boolean isUserMessage; // New field to distinguish message source

    public ChatMessage(String text, boolean isUserMessage) {
        this.text = text;
        this.isUserMessage = isUserMessage;
    }

    public String getText() {
        return text;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }

    // Optionally, add setters or other properties as needed.
}
