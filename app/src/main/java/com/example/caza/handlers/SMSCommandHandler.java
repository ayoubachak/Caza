package com.example.caza.handlers;

import static com.example.caza.MainActivity.SMS_REQUEST_CODE;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.telephony.SmsManager;



public class SMSCommandHandler implements CommandHandler {
    @Override
    public String execute(String[] args, Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, SMS_REQUEST_CODE);
            return "Requesting SMS permission";
        }

        // Check if the args are valid
        if (args.length < 2) {
            return "Invalid arguments for SMS command. Required: Number and Message";
        }

        try {
            String phoneNumber = args[0];
            String message = args[1];
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            return "SMS sent to " + phoneNumber;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send SMS to " + args[0];
        }
    }

    @Override
    public String help() {
        return "/sms <phone number> <message> :\n"
                + "Sends an SMS to the specified <phone number> with the <message>.\n"
                + "Usage example: '/sms 1234567890 Hello World' sends 'Hello World' to 1234567890.\n"
                + "Requires SMS permission to function.";
    }
}
