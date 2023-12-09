package com.example.caza.handlers;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.caza.callbacks.TemperatureResultCallback;

public class TemperatureCommandHandler implements CommandHandler, SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TemperatureResultCallback callback;

    public TemperatureCommandHandler(TemperatureResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public String execute(String[] args, Activity activity) {
        sensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            return "Fetching temperature...";
        } else {
            return "Temperature sensor not available.";
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];
            sensorManager.unregisterListener(this);
            callback.onTemperatureResult("Current temperature is " + temperature + "°C");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can react to changes in sensor accuracy here if needed
    }

    @Override
    public String help() {
        return "/temperature :\n"
                + "Fetches the current ambient temperature from the device's temperature sensor.\n"
                + "No additional arguments required.\n"
                + "Functionality depends on the availability of an ambient temperature sensor in the device.";
    }
}
