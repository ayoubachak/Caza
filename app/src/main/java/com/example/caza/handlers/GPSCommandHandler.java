package com.example.caza.handlers;

import static com.example.caza.MainActivity.GPS_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPSCommandHandler implements CommandHandler {
    @Override
    public String execute(String[] args, Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_REQUEST_CODE);
            return "Requesting GPS permission";
        }

        // Code to get GPS data
        return "Current coordinates are ...";
    }
}
