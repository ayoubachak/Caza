package com.example.caza.handlers;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.caza.callbacks.GPSResultCallback;
import com.example.caza.handlers.CommandHandler;

import static com.example.caza.MainActivity.GPS_REQUEST_CODE;

public class GPSCommandHandler implements CommandHandler, LocationListener {

    private GPSResultCallback callback;
    private LocationManager locationManager;

    public GPSCommandHandler(GPSResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public String execute(String[] args, Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_REQUEST_CODE);
            return "Requesting GPS permission";
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        return "Fetching GPS data...";
    }

    @Override
    public String help() {
        return "/gps :\n"
                + "Fetches the current GPS coordinates of the device.\n"
                + "No additional arguments required.\n"
                + "Requires GPS permission to function.";
    }

    @Override
    public void onLocationChanged(Location location) {
        String gpsData = "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
        callback.onGPSResult(gpsData);
        locationManager.removeUpdates(this); // Stop requesting location updates
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status change if needed
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider being enabled if needed
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider being disabled if needed
    }
}
