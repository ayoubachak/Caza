package com.example.caza.handlers;

import static com.example.caza.MainActivity.SMS_REQUEST_CODE;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import com.example.caza.MainActivity;


public class SMSCommandHandler implements CommandHandler {
    @Override
    public String execute(String[] args , Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, SMS_REQUEST_CODE);
            return "Requesting SMS permission";
        }
        // args[0] = number, args[1] = text
        // Check and request SMS permissions
        // Send SMS
        return "SMS sent to " + args[0];
    }
}

