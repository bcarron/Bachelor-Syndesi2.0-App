package ch.unige.carron8.bachelor.controllers;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import ch.unige.carron8.bachelor.models.BroadcastTypes;

/**
 * Prepares sensor listener for the right sensor.
 * Created by Blaise on 30.04.2015.
 */
public class SensorService extends IntentService {
    private SensorManager sensorManager;

    public SensorService() {
        super("SensorListener");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Integer.parseInt(workIntent.getAction()));
        if (sensor != null) {
            sensorManager.registerListener(new SensorListener(getApplicationContext(), sensorManager), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
