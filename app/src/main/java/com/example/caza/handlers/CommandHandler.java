package com.example.caza.handlers;

import android.app.Activity;

public interface CommandHandler {
    String execute(String[] args, Activity activity);
    String help();
}
