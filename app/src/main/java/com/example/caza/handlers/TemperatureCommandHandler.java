package com.example.caza.handlers;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class TemperatureCommandHandler implements CommandHandler {
    @Override
    public String execute(String[] args, Activity activity) {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (temperatureSensor != null) {
            // Code to read from the sensor and return the temperature
            return "Current temperature is ...";
        } else {
            return "Temperature sensor not available.";
        }
    }
}
