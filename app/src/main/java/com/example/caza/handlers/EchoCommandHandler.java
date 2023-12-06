package com.example.caza.handlers;

import android.app.Activity;

public class EchoCommandHandler implements CommandHandler {
    @Override
    public String execute(String[] args, Activity activity) {
        // Join the args array back into a string
        String response = String.join(" ", args);
        return "Echo: " + response;
    }
}
